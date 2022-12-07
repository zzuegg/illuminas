package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.lights.LightMode;

import java.util.Set;

public class GeometryBasedSpotLightRenderer {
    protected final Material material;
    private final int maxLights;
    protected Geometry geometry;
    final LightMode lightMode;

    public GeometryBasedSpotLightRenderer(AssetManager assetManager, LightMode lightMode, int maxLights) {
        material = new Material(assetManager, "Materials/Illuminas/BlinnPhongLightVolumes.j3md");
        this.lightMode = lightMode;
        this.maxLights = maxLights;
        geometry = SpotLightGeometry.createGeometry(maxLights);
        geometry.setMaterial(material);
    }

    public void renderLights(PipelineContext renderPipeline, Set<SpotLight> spotLights) {

        int count = 0;
        Vector3f[] spotLightPositions = new Vector3f[Math.min(spotLights.size(), maxLights)];
        Vector3f[] spotLightDirections = new Vector3f[Math.min(spotLights.size(), maxLights)];
        Vector3f[] spotLightColors = new Vector3f[Math.min(spotLights.size(), maxLights)];
        Vector2f[] spotLightAngles = new Vector2f[Math.min(spotLights.size(), maxLights)];
        float[] spotLightRadii = new float[Math.min(spotLights.size(), maxLights)];
        SpotLight lastSpotlight = null;
        for (SpotLight spotLight : spotLights) {
            lastSpotlight = spotLight;
            Vector3f position = spotLight.getPosition();
            Vector3f direction = spotLight.getDirection();
            Vector3f color = spotLight.getColor().toVector3f();
            spotLightPositions[count] = new Vector3f(position);
            spotLightDirections[count] = new Vector3f(direction);
            spotLightColors[count] = color;
            spotLightAngles[count] = new Vector2f(spotLight.getSpotOuterAngle(), spotLight.getSpotInnerAngle());
            spotLightRadii[count] = spotLight.getSpotRange();
            count++;
            if (count == maxLights) {
                renderPipeline.getRenderManager().setForcedTechnique("SpotLightVolumes");
                renderSpotLights(renderPipeline, lastSpotlight, count, spotLightPositions, spotLightDirections, spotLightColors, spotLightAngles, spotLightRadii, 0);
                count = 0;
            }
        }
        if (count > 0) {
            renderPipeline.getRenderManager().setForcedTechnique("SpotLightVolumes");
            renderSpotLights(renderPipeline, lastSpotlight, count, spotLightPositions, spotLightDirections, spotLightColors, spotLightAngles, spotLightRadii, 0);
        }

    }

    protected void renderSpotLights(PipelineContext renderPipeline, SpotLight spotLights, int count, Vector3f[] lightPositions, Vector3f[] lightDirections, Vector3f[] lightColors, Vector2f[] lightAngles, float[] lightRadii, float angularFallOfFactor) {
        SpotLightGeometry.updateGeometryForRendering(geometry, count);
        GeometryBasedLightsMaterial.setLightMode(renderPipeline, material, lightMode);
        GeometryBasedLightsMaterial.setLightPositions(material, lightPositions);
        GeometryBasedLightsMaterial.setLightAngles(material, lightAngles);
        GeometryBasedLightsMaterial.setLightDirections(material, lightDirections);
        GeometryBasedLightsMaterial.setLightColors(material, lightColors);
        GeometryBasedLightsMaterial.setLightRadii(material, lightRadii);
        GeometryBasedLightsMaterial.setLightCount(material, count);
        GeometryBasedLightsMaterial.setLightAngularFallOfFactor(material, angularFallOfFactor);
        renderPipeline.getRenderManager().renderGeometry(geometry);
        renderPipeline.getRenderManager().setForcedTechnique(null);
    }


}
