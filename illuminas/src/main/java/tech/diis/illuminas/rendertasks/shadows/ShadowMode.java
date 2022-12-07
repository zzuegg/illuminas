package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.shader.VarType;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;
import lombok.Data;
import lombok.Getter;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.CollectShadowData;
import tech.diis.illuminas.rendertasks.common.GaussianFilter;

@Data
public abstract class ShadowMode {


    @Getter
    RenderPipeline renderPipeline;

    @Getter
    Texture2dDefinition shadowMapTexture;

    @Getter
    int shadowMode;

    public void setMaterialParameters(PipelineContext renderPipeline, Material material, Camera camera) {
        material.setParam("ShadowViewProjectionMatrix", VarType.Matrix4, camera.getViewProjectionMatrix());
        material.setInt("ShadowMode", getShadowMode());
        material.setTexture("ShadowMap", Constants.OutputResult.get(renderPipeline));
    }
}
