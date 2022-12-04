package tech.diis.illuminas.rendertasks.debug;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import tech.diis.illuminas.*;

public class DebugBlinnPhongGBuffer extends RenderTask {

    private Material debugMaterial;

    public DebugBlinnPhongGBuffer() {
        super("Debug GBUFFER");

    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        this.debugMaterial = new Material(assetManager, "Materials/debug/GbufferDebugPhong.j3md");
        debugMaterial.getAdditionalRenderState().setDepthTest(false);
        debugMaterial.getAdditionalRenderState().setDepthWrite(false);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        renderPipeline.swapPostProcessorTargets();
        Constants.OutputTarget.bind(renderPipeline);
        debugMaterial.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));
        debugMaterial.setTexture("NormalDepth", Constants.WorldNormals.get(renderPipeline));
        debugMaterial.setTexture("AlbedoSpecular", Constants.BaseColorsSpecular.get(renderPipeline));
        debugMaterial.setTexture("Depth", Constants.DepthStencil.get(renderPipeline));
        Constants.FS_QUAD.setMaterial(debugMaterial);
        renderPipeline.getRenderManager().renderGeometry(Constants.FS_QUAD);
    }
}
