package tech.diis.illuminas.rendertasks;

import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import tech.diis.illuminas.*;
import tech.diis.illuminas.jme.NullLightFilter;
import tech.diis.illuminas.rendertasks.lights.LightMode;

public final class OpaqueToGBuffer extends RenderTask {
    final LightMode lightMode;
    final ObjectDefinition<WorldData> worldDataDefinition = WorldData.worldData;
    final RenderState renderState;

    public OpaqueToGBuffer(LightMode lightMode) {
        super("OpaqueToGbuffer");
        this.lightMode = lightMode;
        this.renderState = new RenderState();
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.requires(worldDataDefinition);
        pipelineLayout.produces(lightMode.getRequiredResources());
        pipelineLayout.produces(lightMode.getRenderTargetDefinition());
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        WorldData worldData = worldDataDefinition.get(renderPipeline);
        lightMode.getRenderTargetDefinition().bind(renderPipeline);
        renderPipeline.getRenderManager().setCamera(renderPipeline.getCamera(), false);
        renderPipeline.getRenderManager().getRenderer().clearBuffers(true, true, true);
        renderPipeline.getRenderManager().setForcedTechnique("IlluminasDeferred");
        NullLightFilter.applyTo(renderPipeline.getRenderManager());
        renderPipeline.getRenderManager().renderGeometryList(worldData.getOpaqueGeometries());
        renderPipeline.getRenderManager().setForcedTechnique(null);
        NullLightFilter.revert();
    }
}
