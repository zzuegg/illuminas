package tech.diis.illuminas;

import com.jme3.asset.AssetManager;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class RenderTask {
    @Getter(AccessLevel.PACKAGE)
    private final String name;

    public RenderTask(String name) {
        this.name = name;
    }

    protected abstract void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager);

    protected abstract void execute(PipelineContext renderPipeline);
}
