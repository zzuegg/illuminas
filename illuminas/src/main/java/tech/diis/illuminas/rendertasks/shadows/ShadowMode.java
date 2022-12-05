package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;
import lombok.Data;
import lombok.Getter;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.CollectShadowData;
import tech.diis.illuminas.rendertasks.common.GaussianFilter;

@Data
public class ShadowMode {
    @Getter
    RenderPipeline renderPipeline;

    @Getter
    Texture2dDefinition shadowMapTexture;

    @Getter
    int shadowMode;

    public static ShadowMode VSM;

    public static ShadowMode VSM_GF;

    static {
        VSM = new ShadowMode();
        VSM.shadowMode=1;
        VSM.shadowMapTexture = new Texture2dDefinition("VSM-ShadowMap", Image.Format.RG32F, ColorSpace.Linear, Texture.MagFilter.Bilinear, Texture.MinFilter.BilinearNoMipMaps);
        VSM.renderPipeline = new RenderPipeline("VSM-Pipeline", VSM.shadowMapTexture, Constants.Depth);
        VSM.renderPipeline.addRenderTask(new CollectShadowData(true))
                .addRenderTask(new RenderShadowMap("ShadowVSM"));

        VSM_GF = new ShadowMode();
        VSM_GF.shadowMode=1;
        VSM_GF.shadowMapTexture = VSM.shadowMapTexture;
        VSM_GF.renderPipeline = new RenderPipeline("VMS-GF-Pipeline", VSM.shadowMapTexture, Constants.Depth);
        VSM_GF.renderPipeline.addRenderTask(new CollectShadowData(true))
                .addRenderTask(new RenderShadowMap("ShadowVSM"))
                .addRenderTask(new GaussianFilter());
    }
}
