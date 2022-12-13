package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.scene.Geometry;
import com.jme3.shadow.ShadowUtil;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipelineLayout;
import tech.diis.illuminas.RenderTask;
import tech.diis.illuminas.jme.NullLightFilter;
import tech.diis.illuminas.rendertasks.ShadowData;

public class RenderShadowMap extends RenderTask {
    final int mode;
    private final RenderState renderShadowMapRenderState;

    public RenderShadowMap(int mode) {
        super("RenderShadowMap");
        this.mode = mode;
        this.renderShadowMapRenderState = new RenderState();
        this.renderShadowMapRenderState.setBlendMode(RenderState.BlendMode.Off);
        this.renderShadowMapRenderState.setFaceCullMode(RenderState.FaceCullMode.Back);
        this.renderShadowMapRenderState.setDepthTest(true);
        this.renderShadowMapRenderState.setDepthWrite(true);
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.requires(ShadowData.ShadowData);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        ShadowData shadowData = ShadowData.ShadowData.get(renderPipeline);
        Constants.OutputTarget.bind(renderPipeline);
        renderPipeline.getRenderManager().setCamera(renderPipeline.getCamera(), false);
        renderPipeline.getRenderManager().getRenderer().setBackgroundColor(new ColorRGBA(1, 1, 1, 1));
        renderPipeline.getRenderManager().getRenderer().clearBuffers(true, true, true);
        String forcedTechnique = renderPipeline.getRenderManager().getForcedTechnique();
        renderPipeline.getRenderManager().setForcedTechnique("ShadowVSM");
        RenderState forcedRenderState = renderPipeline.getRenderManager().getForcedRenderState();
        renderPipeline.getRenderManager().setForcedRenderState(renderShadowMapRenderState);
        NullLightFilter.applyTo(renderPipeline.getRenderManager());

        GeometryList shadowCasters = shadowData.getShadowCasters();
        for (Geometry shadowCaster : shadowCasters) {
            shadowCaster.getMaterial().setInt("ShadowMapMode", mode);
        }
        renderPipeline.getRenderManager().renderGeometryList(shadowCasters);

        NullLightFilter.revert();
        renderPipeline.getRenderManager().setForcedRenderState(forcedRenderState);
        renderPipeline.getRenderManager().setForcedTechnique(forcedTechnique);
    }
}
