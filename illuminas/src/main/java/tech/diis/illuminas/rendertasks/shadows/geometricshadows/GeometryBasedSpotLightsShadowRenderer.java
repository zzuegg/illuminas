package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.texture.Texture;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.jme.StaticDebugTexture;
import tech.diis.illuminas.jme.lights.ExtendedSpotLight;
import tech.diis.illuminas.rendertasks.lights.LightMode;
import tech.diis.illuminas.rendertasks.lights.geometriclights.GeometryBasedLightsMaterial;
import tech.diis.illuminas.rendertasks.lights.geometriclights.GeometryBasedSpotLightRenderer;
import tech.diis.illuminas.rendertasks.shadows.IlluminasShadowUtils;

public class GeometryBasedSpotLightsShadowRenderer extends GeometryBasedSpotLightRenderer {

    private final GeometryBasedShadows geometryBasedShadows;
    private final RenderState renderState;

    public GeometryBasedSpotLightsShadowRenderer(GeometryBasedShadows geometryBasedShadows, AssetManager assetManager, LightMode lightMode, int maxLights, RenderState renderState) {
        super(assetManager, lightMode, maxLights);
        this.geometryBasedShadows = geometryBasedShadows;
        this.renderState = renderState;
    }

    @Override
    protected void renderSpotLights(PipelineContext renderPipeline, SpotLight spotLight, int count, Vector3f[] lightPositions, Vector3f[] lightDirections, Vector3f[] lightColors, Vector2f[] lightAngles, float[] lightRadii, float angularFallOfFactor) {
        if (spotLight instanceof ExtendedSpotLight extendedSpotLight) {
            PipelineContext contextForShadowMode = geometryBasedShadows.getContextForShadowMode(renderPipeline, extendedSpotLight.getShadowMode(), extendedSpotLight.getShadowMapSize());
            Camera camera = new Camera(extendedSpotLight.getShadowMapSize(), extendedSpotLight.getShadowMapSize());
            IlluminasShadowUtils.spotLightToCamera(extendedSpotLight, camera);
            contextForShadowMode.execute(camera, renderPipeline.getScene(), null);
            contextForShadowMode.swapPostProcessorTargets();
            GeometryBasedLightsMaterial.setShadowMap(renderPipeline, material, contextForShadowMode, extendedSpotLight.getShadowMode(), camera);
            renderPipeline.getRenderManager().setForcedRenderState(renderState);
            renderPipeline.getRenderManager().setCamera(renderPipeline.getCamera(), false);
            Constants.OutputTarget.bind(renderPipeline);
            super.renderSpotLights(renderPipeline, spotLight, count, lightPositions, lightDirections, lightColors, lightAngles, lightRadii, angularFallOfFactor);
        }
    }
}
