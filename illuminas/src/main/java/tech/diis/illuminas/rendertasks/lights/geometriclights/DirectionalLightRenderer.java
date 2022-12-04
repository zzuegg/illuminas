package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shader.VarType;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.lights.LightMode;

import java.util.Set;

public class DirectionalLightRenderer {
    final Material material;
    final LightMode lightMode;

    public DirectionalLightRenderer(AssetManager assetManager, LightMode lightMode) {
        material = new Material(assetManager, "Materials/Illuminas/BlinnPhongLightVolumes.j3md");
        this.lightMode = lightMode;
    }

    public void renderLights(PipelineContext renderPipeline, Set<DirectionalLight> directionalLights, Set<AmbientLight> ambientLights) {
        ColorRGBA ambientLight = new ColorRGBA(0, 0, 0, 0);
        for (AmbientLight light : ambientLights) {
            ambientLight.addLocal(light.getColor());
        }

        Vector3f[] lightDirections = new Vector3f[directionalLights.size()];
        Vector3f[] lightColors = new Vector3f[directionalLights.size()];
        int count = 0;
        for (DirectionalLight directionalLight : directionalLights) {
            lightDirections[count] = directionalLight.getDirection();
            lightColors[count] = directionalLight.getColor().toVector3f();
            count++;
        }
        material.setColor("AmbientLight", ambientLight);
        GeometryBasedLightsMaterial.setLightCount(material, directionalLights.size());
        GeometryBasedLightsMaterial.setLightDirections(material, lightDirections);
        GeometryBasedLightsMaterial.setLightColors(material, lightColors);
        GeometryBasedLightsMaterial.setLightMode(renderPipeline, material, lightMode);
        renderPipeline.getRenderManager().setForcedTechnique("AmbientDirectionalLightVolumes");
        Constants.FS_QUAD.setMaterial(material);
        if (directionalLights.size() != 0 && ambientLights.size() != 0) {
            renderPipeline.getRenderManager().renderGeometry(Constants.FS_QUAD);
        }
        renderPipeline.getRenderManager().setForcedTechnique(null);
    }
}
