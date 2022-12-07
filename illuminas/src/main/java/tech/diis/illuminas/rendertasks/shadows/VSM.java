package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.Texture2dDefinition;
import tech.diis.illuminas.rendertasks.CollectShadowData;

public class VSM extends ShadowMode{
    public VSM() {
        shadowMode = 1;
        shadowMapTexture = new Texture2dDefinition("VSM-ShadowMap", Image.Format.RG32F, ColorSpace.Linear, Texture.MagFilter.Bilinear, Texture.MinFilter.BilinearNoMipMaps);
        renderPipeline = new RenderPipeline("VSM-Pipeline", shadowMapTexture, Constants.Depth);
        renderPipeline.addRenderTask(new CollectShadowData(true))
                .addRenderTask(new RenderShadowMap("ShadowVSM"));
    }
}
