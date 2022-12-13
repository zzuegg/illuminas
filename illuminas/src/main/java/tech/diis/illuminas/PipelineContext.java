package tech.diis.illuminas;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PipelineContext implements RenderPipelineLayout {
    @Getter
    private final String name;
    private final RenderPipeline renderPipeline;
    int width;
    int height;
    private boolean initialized;
    @Setter(AccessLevel.PACKAGE)
    private boolean resizeRequired;
    private List<ResourceDefinition<?>> orderedResources;
    private Map<ResourceDefinition<?>, PipelineContext.Resource<?>> pipelineResources;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private AssetManager assetManager;
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PACKAGE)
    private RenderManager renderManager;
    @Getter(AccessLevel.PUBLIC)
    private Camera camera;
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PACKAGE)
    private Spatial scene;

    private Texture2dDefinition[] postProcessingTextures;
    private RenderTargetDefinition[] postProcessingRenderTargets;
    private int currentPostprocessingTarget;
    private int currentStage = 0;
    private RenderTargetDefinition pipelineTargetDefinition;

    public PipelineContext(String name, RenderPipeline renderPipeline) {
        this.name = name;
        this.renderPipeline = renderPipeline;
        this.pipelineResources = new IdentityHashMap<>();
        this.orderedResources = new ArrayList<>();
        this.initialized = false;
        this.resizeRequired = true;
        postProcessingTextures = new Texture2dDefinition[]{
                new Texture2dDefinition("PostProcessingA", renderPipeline.getPostProcessingTexture().getFormat(), renderPipeline.getPostProcessingTexture().getColorSpace(), renderPipeline.getPostProcessingTexture().getMagFilter(), renderPipeline.getPostProcessingTexture().getMinFilter()),
                new Texture2dDefinition("PostProcessingB", renderPipeline.getPostProcessingTexture().getFormat(), renderPipeline.getPostProcessingTexture().getColorSpace(), renderPipeline.getPostProcessingTexture().getMagFilter(), renderPipeline.getPostProcessingTexture().getMinFilter())

        };
        postProcessingRenderTargets = new RenderTargetDefinition[]{
                new RenderTargetDefinition("PostProcessingTargetA", false, 1, postProcessingTextures[0], renderPipeline.getPostprocessingDepthTexture()),
                new RenderTargetDefinition("PostProcessingTargetB", false, 1, postProcessingTextures[1], renderPipeline.getPostprocessingDepthTexture())
        };
    }

    public void execute(Camera camera, Spatial scene, RenderTargetDefinition renderTargetDefinition) {
        log.trace("Pipeline begin: {}", name);
        this.pipelineTargetDefinition = renderTargetDefinition;
        this.currentPostprocessingTarget = 0;
        this.currentStage = 0;
        this.camera = camera;
        if (renderTargetDefinition != null) {
            if (!pipelineResources.containsKey(renderTargetDefinition)) {
                this.pipelineResources.put(renderTargetDefinition, new PipelineContext.Resource<>());
                if (renderTargetDefinition.getTargets() != null && renderTargetDefinition.getTargets().length != 0) {
                    for (Texture2dDefinition target : renderTargetDefinition.getTargets()) {
                        pipelineResources.put(target, new PipelineContext.Resource<>());
                        target.resize(camera.getWidth(), camera.getHeight(), this);
                    }
                    renderTargetDefinition.resize(camera.getWidth(), camera.getHeight(), this);
                }
            }
        }
        if (width != camera.getWidth() || height != camera.getHeight()) {
            width = camera.getWidth();
            height = camera.getHeight();
            resizeRequired = true;
        }
        this.camera = camera;
        this.scene = scene;
        if (!initialized) {
            log.trace("Initializing Pipeline: {}", name);
            initializeRenderTasks();
            initialized = true;
        }
        if (resizeRequired) {
            log.trace("Resizing Pipeline: {}", name);
            resize();
            resizeRequired = false;
        }
        executeRenderTasks();
    }

    private void executeRenderTasks() {
        for (int i = 0; i < renderPipeline.getRenderTasks().size(); i++) {
            currentStage++;
            renderPipeline.getRenderTasks().get(i).execute(this);
        }
    }

    private void resize() {
        for (ResourceDefinition<?> resourceDefinition : this.orderedResources) {
            if (resourceDefinition instanceof ResizableResourceDefinition<?> resizableResourceDefinition) {
                log.trace("Resizing: " + resizableResourceDefinition.getName());
                resizableResourceDefinition.resize(width, height, this);
            }
        }
    }

    private void initializeRenderTasks() {
        for (int i = 0; i < renderPipeline.getRenderTasks().size(); i++) {
            renderPipeline.getRenderTasks().get(i).initialize(this, assetManager);
        }
        produces(postProcessingTextures[0], postProcessingTextures[1]);
        if (renderPipeline.getPostprocessingDepthTexture() != null) {
            if (!pipelineResources.containsKey(renderPipeline.getPostprocessingDepthTexture())) {
                produces(renderPipeline.getPostprocessingDepthTexture());
            }
        }
        produces(postProcessingRenderTargets[0], postProcessingRenderTargets[1]);
    }

    public <RESOURCE> RESOURCE getPipelineResource(ResourceDefinition<RESOURCE> resourceDefinition) {
        if (resourceDefinition == Constants.OutputTarget) {
            if (currentStage == renderPipeline.getRenderTasks().size()) {
                if (this.pipelineTargetDefinition == null) {
                    resourceDefinition = (ResourceDefinition<RESOURCE>) postProcessingRenderTargets[currentPostprocessingTarget % 2];
                } else {
                    resourceDefinition = (ResourceDefinition<RESOURCE>) this.pipelineTargetDefinition;
                }
            } else {
                resourceDefinition = (ResourceDefinition<RESOURCE>) postProcessingRenderTargets[currentPostprocessingTarget % 2];
            }
        } else if (resourceDefinition == Constants.OutputResult) {
            resourceDefinition = (ResourceDefinition<RESOURCE>) postProcessingTextures[(currentPostprocessingTarget + 1) % 2];
        }

        PipelineContext.Resource<?> resource = pipelineResources.get(resourceDefinition);
        if (resource == null) {
            log.error("Required resource {} not available in pipeline {}", resourceDefinition.getName(), name);
            throw new RuntimeException("Resource does not exist");
        }
        log.trace("{}",resourceDefinition.getName());
        return (RESOURCE) resource.object;
    }

    public void swapPostProcessorTargets() {
        currentPostprocessingTarget++;
    }


    public <RESOURCE> void setPipelineResource(ResourceDefinition<RESOURCE> resourceDefinition, Object o) {
        PipelineContext.Resource<?> resource = pipelineResources.get(resourceDefinition);
        if (resource == null) {
            log.error("Required resource {} not available in pipeline {}", resourceDefinition.getName(), name);
            throw new RuntimeException("Resource does not exist");
        }
        resource.object = o;
    }

    @Override
    public void requires(ResourceDefinition<?>... resourceDefinitions) {
        for (ResourceDefinition<?> resourceDefinition : resourceDefinitions) {
            if (!pipelineResources.containsKey(resourceDefinition)) {
                log.error("Required resource {} not available in pipeline {}", resourceDefinition.getName(), name);
                throw new RuntimeException("Resource does not exist");
            }
        }
    }

    @Override
    public void produces(ResourceDefinition<?>... resourceDefinitions) {
        for (ResourceDefinition<?> resourceDefinition : resourceDefinitions) {
            if (pipelineResources.containsKey(resourceDefinition)) {
                log.error("Resource {} already existing in pipeline {}", resourceDefinition.getName(), name);
                throw new RuntimeException("Resource does already exist");
            } else {
                pipelineResources.put(resourceDefinition, new PipelineContext.Resource<>());
                orderedResources.add(resourceDefinition);
            }
        }
    }

    public PipelineContext createNewContext(RenderPipeline renderPipeline) {
        PipelineContext pipelineContext = new PipelineContext(renderPipeline.getName(), renderPipeline);
        pipelineContext.setAssetManager(assetManager);
        pipelineContext.setRenderManager(renderManager);
        pipelineContext.setScene(scene);
        return pipelineContext;
    }

    public void printDebug() {
        System.out.println("********************");
        for (ResourceDefinition<?> resourceDefinition : pipelineResources.keySet()) {
            System.out.println(resourceDefinition.getName());
        }
        System.out.println("********************");
    }

    static class Resource<T> {
        Object object;
    }
}
