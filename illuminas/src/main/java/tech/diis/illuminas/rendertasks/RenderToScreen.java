package tech.diis.illuminas.rendertasks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import tech.diis.illuminas.*;


public class RenderToScreen extends RenderTask {
    private Material debugMaterial;

    public RenderToScreen() {
        super("Render To Screee");

    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        this.debugMaterial = new Material(assetManager, "Materials/Illuminas/RenderToScreen.j3md");
        debugMaterial.getAdditionalRenderState().setDepthTest(false);
        debugMaterial.getAdditionalRenderState().setDepthWrite(false);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        renderPipeline.swapPostProcessorTargets();
        Constants.OutputTarget.bind(renderPipeline);
        debugMaterial.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));
        Constants.FS_QUAD.setMaterial(debugMaterial);
        renderPipeline.getRenderManager().renderGeometry(Constants.FS_QUAD);
    }
}
