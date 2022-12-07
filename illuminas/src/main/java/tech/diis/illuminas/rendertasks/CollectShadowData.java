package tech.diis.illuminas.rendertasks;

import com.jme3.anim.AnimComposer;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import tech.diis.illuminas.*;

public class CollectShadowData extends RenderTask {
    ObjectDefinition<ShadowData> shadowDataDefinition = ShadowData.ShadowData;
    boolean newDataEachFrame;

    public CollectShadowData(boolean newDataEachFrame) {
        super("WorldData collector");

        this.newDataEachFrame = newDataEachFrame;
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.produces(shadowDataDefinition);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        ShadowData shadowData = shadowDataDefinition.get(renderPipeline);
        if (shadowData == null || this.newDataEachFrame) {
            shadowData = new ShadowData();
            shadowData.populate(renderPipeline,renderPipeline.getScene(), renderPipeline.getCamera());
        } else {
            shadowData.clear();
        }
        shadowDataDefinition.set(renderPipeline, shadowData);
    }
}
