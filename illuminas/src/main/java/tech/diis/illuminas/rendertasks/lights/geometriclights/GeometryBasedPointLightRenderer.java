package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.lights.LightMode;

import java.util.Set;

public class GeometryBasedPointLightRenderer {
    protected final Material material;
    private final LightMode lightMode;
    private final int maxLights;
    Geometry pointLightGeometry;


    public GeometryBasedPointLightRenderer(AssetManager assetManager, LightMode lightMode, int maxLight) {
        material = new Material(assetManager, "Materials/Illuminas/BlinnPhongLightVolumes.j3md");
        this.lightMode = lightMode;
        this.maxLights = maxLight;
        pointLightGeometry = PointLightGeometry.createGeometry(maxLights);
        pointLightGeometry.setMaterial(material);
    }

    public void renderLights(PipelineContext renderPipeline, Set<PointLight> pointLights) {
        int count = 0;
        Vector3f[] lightPositions = new Vector3f[Math.min(pointLights.size(), maxLights)];
        Vector3f[] lightColors = new Vector3f[Math.min(pointLights.size(), maxLights)];
        float[] lightRadii = new float[Math.min(pointLights.size(), maxLights)];
        PointLight lastPointlight = null;
        for (PointLight pointLight : pointLights) {
            lastPointlight = pointLight;
            Vector3f position = pointLight.getPosition();
            lightPositions[count] = new Vector3f(position);
            lightColors[count] = pointLight.getColor().toVector3f();
            lightRadii[count] = pointLight.getRadius();
            count++;
            if (count == maxLights) {
                renderPointLights(renderPipeline, lastPointlight, count, lightPositions, lightColors, lightRadii);
                count = 0;
            }
        }
        if (count > 0) {
            renderPointLights(renderPipeline, lastPointlight, count, lightPositions, lightColors, lightRadii);
        }


    }

    protected void renderPointLights(PipelineContext renderPipeline, PointLight pointLight, int count, Vector3f[] lightPositions, Vector3f[] lightColors, float[] lightRadii) {
        PointLightGeometry.updateGeometryForRendering(pointLightGeometry, count);
        GeometryBasedLightsMaterial.setLightMode(renderPipeline, material, lightMode);
        GeometryBasedLightsMaterial.setLightCount(material, count);
        GeometryBasedLightsMaterial.setLightPositions(material, lightPositions);
        GeometryBasedLightsMaterial.setLightColors(material, lightColors);
        GeometryBasedLightsMaterial.setLightRadii(material, lightRadii);
        renderPipeline.getRenderManager().setForcedTechnique("PointLightVolumes");
        renderPipeline.getRenderManager().renderGeometry(pointLightGeometry);
        renderPipeline.getRenderManager().setForcedTechnique(null);
    }
}
