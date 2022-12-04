package tech.diis.illuminas.rendertasks.common;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import tech.diis.illuminas.*;

public class GaussianFilter extends RenderTask {

    private final RenderState renderState
            ;
    private Material material;

    public GaussianFilter() {
        super("GaussianFilter");
        this.renderState = new RenderState();
        this.renderState.setBlendMode(RenderState.BlendMode.Color);
        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Front);
        this.renderState.setDepthTest(false);
        this.renderState.setDepthWrite(false);
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        this.material = new Material(assetManager, "Materials/Process/GaussianBlur.j3md");
        this.material.getAdditionalRenderState().setDepthTest(false);
        this.material.getAdditionalRenderState().setDepthWrite(false);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        Geometry fsQuad = Constants.FS_QUAD;
        fsQuad.setMaterial(material);
        renderPipeline.swapPostProcessorTargets();

            Constants.OutputTarget.bind(renderPipeline);
            material.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));
            material.setVector2("Direction", new Vector2f(1, 0));
            renderPipeline.getRenderManager().renderGeometry(fsQuad);
            //Filter 1
            renderPipeline.swapPostProcessorTargets();
            Constants.OutputTarget.bind(renderPipeline);
            material.setTexture("CurrentResult", Constants.OutputResult.get(renderPipeline));
            material.setVector2("Direction", new Vector2f(0, 1));
            renderPipeline.getRenderManager().renderGeometry(fsQuad);

        //Filter 2
    }
}
