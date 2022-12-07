package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.shader.VarType;
import com.jme3.texture.Image;
import com.jme3.texture.TextureCubeMap;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.rendertasks.lights.LightMode;
import tech.diis.illuminas.rendertasks.shadows.ShadowMode;

public class GeometryBasedLightsMaterial {


    public static void setLightPositions(Material material, Vector3f[] lightPositions) {
        material.setParam("LightPositions", VarType.Vector3Array, lightPositions);
    }

    public static void setLightDirections(Material material, Vector3f[] lightDirections) {
        material.setParam("LightDirections", VarType.Vector3Array, lightDirections);
    }

    public static void setLightColors(Material material, Vector3f[] lightColors) {
        material.setParam("LightColors", VarType.Vector3Array, lightColors);
    }

    public static void setLightAngles(Material material, Vector2f[] lightAngles) {
        material.setParam("LightAngles", VarType.Vector2Array, lightAngles);
    }

    public static void setLightRadii(Material material, float[] lightradii) {
        material.setParam("LightRadii", VarType.FloatArray, lightradii);
    }

    public static void setLightMode(PipelineContext renderPipeline, Material material, LightMode lightMode) {
        lightMode.setMaterialParameters(renderPipeline, material);
    }

    public static void setLightCount(Material material, int lightCount) {
        material.setInt("Lights", lightCount);
    }


    public static void setLightAngularFallOfFactor(Material material, float fallOfFactor) {
        material.setFloat("LightAngularFallofFactor", fallOfFactor);
    }
}
