package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.jme.lights.ExtendedSpotLight;
import tech.diis.illuminas.rendertasks.lights.LightMode;
import tech.diis.illuminas.rendertasks.lights.geometriclights.GeometryBasedLightsMaterial;
import tech.diis.illuminas.rendertasks.lights.geometriclights.GeometryBasedSpotLightRenderer;
import tech.diis.illuminas.rendertasks.shadows.IlluminasShadowUtils;

public class GeometryBasedPointLightsShadowRenderer extends GeometryBasedSpotLightsShadowRenderer {

    private final GeometryBasedShadows geometryBasedShadows;
    private final RenderState renderState;

    public GeometryBasedPointLightsShadowRenderer(GeometryBasedShadows geometryBasedShadows, AssetManager assetManager, LightMode lightMode, int maxLights, RenderState renderState) {
        super(geometryBasedShadows, assetManager, lightMode, 1, renderState);


        this.geometryBasedShadows = geometryBasedShadows;
        this.renderState = renderState;
    }


    @Override
    protected void renderSpotLights(PipelineContext renderPipeline, SpotLight spotLight, int count, Vector3f[] lightPositions, Vector3f[] lightDirections, Vector3f[] lightColors, Vector2f[] lightAngles, float[] lightRadii, float angularFallOfFactor) {
        super.renderSpotLights(renderPipeline, spotLight, 1, lightPositions, lightDirections, lightColors, lightAngles, lightRadii, 1);
    }
}
