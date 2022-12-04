package tech.diis.illuminas;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class RenderPipeline {
    @Getter(AccessLevel.PUBLIC)
    final String name;
    @Getter(AccessLevel.PACKAGE)
    private List<RenderTask> renderTasks;
    @Getter(AccessLevel.PACKAGE)
    private final Texture2dDefinition postprocessingDepthTexture;
    @Getter(AccessLevel.PACKAGE)
    private final Texture2dDefinition postProcessingTexture;


    public RenderPipeline(String name, Texture2dDefinition postProcessingTexture, Texture2dDefinition postprocessingDepthTexture) {
        this.name = name;
        renderTasks = new ArrayList<>();
        this.postprocessingDepthTexture = postprocessingDepthTexture;
        this.postProcessingTexture = postProcessingTexture;
    }

    public RenderPipeline addRenderTask(RenderTask renderTask) {
        log.trace("Adding RenderTask: {}, to Pipeline: {}", name, renderTask.getName());
        this.renderTasks.add(renderTask);
        return this;
    }
}
