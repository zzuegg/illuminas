package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.shader.VarType;
import lombok.extern.slf4j.Slf4j;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipelineLayout;
import tech.diis.illuminas.RenderTask;

@Slf4j
public class FilterVSM extends RenderTask {

    private final RenderState renderState;
    private Material material;

    public FilterVSM() {
        super("GaussianFilter");
        this.renderState = new RenderState();
        this.renderState.setBlendMode(RenderState.BlendMode.Off);
        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Back);
        this.renderState.setDepthTest(false);
        this.renderState.setDepthWrite(false);
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        this.material = new Material(assetManager, "Materials/Illuminas/Shadow/FilterVSM.j3md");
        this.material.setInt("Samples", 8);
        this.material.setFloat("Radius", 1);
        this.material.getAdditionalRenderState().setDepthTest(false);
        this.material.getAdditionalRenderState().setDepthWrite(false);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        String forcedTechnique = renderPipeline.getRenderManager().getForcedTechnique();
        renderPipeline.getRenderManager().setForcedTechnique(null);
        RenderState forcedRenderState = renderPipeline.getRenderManager().getForcedRenderState();
        renderPipeline.getRenderManager().setForcedRenderState(null);
        Geometry fsQuad = Constants.FS_QUAD;
        fsQuad.setMaterial(material);

        renderPipeline.swapPostProcessorTargets();
        Constants.OutputTarget.bind(renderPipeline);
        material.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));
        material.setInt("Horizontal", 1);
        renderPipeline.getRenderManager().renderGeometry(fsQuad);
        renderPipeline.swapPostProcessorTargets();
        Constants.OutputTarget.bind(renderPipeline);
        material.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));


        material.setInt("Horizontal", 0);
        renderPipeline.getRenderManager().renderGeometry(fsQuad);

        renderPipeline.getRenderManager().setForcedRenderState(forcedRenderState);
        renderPipeline.getRenderManager().setForcedTechnique(forcedTechnique);
    }
}
