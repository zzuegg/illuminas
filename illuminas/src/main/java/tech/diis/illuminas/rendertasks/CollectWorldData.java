package tech.diis.illuminas.rendertasks;

import com.jme3.asset.AssetManager;
import tech.diis.illuminas.*;

public class CollectWorldData extends RenderTask {
    ObjectDefinition<WorldData> worldDataDefinition = WorldData.worldData;
    boolean newDataEachFrame;

    public CollectWorldData(boolean newDataEachFrame) {
        super("WorldData collector");

        this.newDataEachFrame = newDataEachFrame;
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.produces(worldDataDefinition);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        WorldData worldData = worldDataDefinition.get(renderPipeline);
        if (worldData == null || this.newDataEachFrame) {
            worldData = new WorldData();
            worldData.populate(renderPipeline.getScene(), renderPipeline.getCamera());
        } else {
            worldData.clear();
        }
        worldDataDefinition.set(renderPipeline, worldData);
    }
}
