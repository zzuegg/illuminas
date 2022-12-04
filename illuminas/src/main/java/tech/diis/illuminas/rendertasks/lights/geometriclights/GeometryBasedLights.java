package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import tech.diis.illuminas.*;
import tech.diis.illuminas.rendertasks.WorldData;
import tech.diis.illuminas.rendertasks.lights.LightMode;


public class GeometryBasedLights extends RenderTask {

    private final RenderState renderState;
    final LightMode lightMode;
    private DirectionalLightRenderer directionalLightRenderer;
    private GeometryBasedPointLightRenderer pointLightRenderer;
    private GeometryBasedSpotLightRenderer spotLightRenderer;

    public GeometryBasedLights(LightMode lightMode) {
        super("GeometryBasedLightRenderer");
        this.lightMode = lightMode;
        this.renderState = new RenderState();
        this.renderState.setBlendMode(RenderState.BlendMode.Additive);
        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Front);
        this.renderState.setDepthFunc(RenderState.TestFunction.GreaterOrEqual);
        this.renderState.setDepthTest(false);
        this.renderState.setDepthWrite(false);

    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.requires(WorldData.worldData);
        for (ResourceDefinition<?> requiredResource : lightMode.getRequiredResources()) {
            pipelineLayout.requires(requiredResource);
        }
        this.directionalLightRenderer = new DirectionalLightRenderer(assetManager, lightMode);
        this.pointLightRenderer = new GeometryBasedPointLightRenderer(assetManager, lightMode, 100);
        this.spotLightRenderer = new GeometryBasedSpotLightRenderer(assetManager, lightMode, 100);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        renderPipeline.swapPostProcessorTargets();
        Constants.OutputTarget.bind(renderPipeline);

        renderPipeline.getRenderManager().getRenderer().clearBuffers(true, false, false);
        RenderState tmp = renderPipeline.getRenderManager().getForcedRenderState();
        renderPipeline.getRenderManager().setForcedRenderState(renderState);
        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Back);
        WorldData worldData = WorldData.worldData.get(renderPipeline);
        directionalLightRenderer.renderLights(renderPipeline, worldData.getDirectionalLights(), worldData.getAmbientLights());

        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Front);
        renderState.setDepthTest(false);
        pointLightRenderer.renderLights(renderPipeline, worldData.getPointLights());
        spotLightRenderer.renderLights(renderPipeline, worldData.getSpotLights());
        renderState.setDepthTest(false);
        renderPipeline.getRenderManager().setForcedRenderState(tmp);
    }
}
