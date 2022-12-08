package tech.diis.illuminas;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;

public class Constants {
    public static Geometry FS_QUAD = new Geometry("FS_QUAD", new Quad(1, 1));
    public static Texture2dDefinition PostProcessingFP = new Texture2dDefinition("PostProcessingFP", Image.Format.RGB16F, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);

    public static Texture2dDefinition WorldNormals = new Texture2dDefinition("WorldNormals", Image.Format.RGB16F, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);
    public static Texture2dDefinition BaseColorsSpecular = new Texture2dDefinition("BaseColorsSpecular", Image.Format.RGBA8, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);
    public static Texture2dDefinition DepthStencil = new Texture2dDefinition("DepthStencil", Image.Format.Depth24Stencil8, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);
    public static Texture2dDefinition Depth = new Texture2dDefinition("Depth", Image.Format.Depth24, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);

    public static RenderTargetDefinition GBufferBlinnPhong = new RenderTargetDefinition("GBuffer BlinnPhong", false, 1, WorldNormals, BaseColorsSpecular, DepthStencil);
    public static RenderTargetDefinition DisplayOut = new RenderTargetDefinition("DisplayOut", false, 1);
    public static RenderTargetDefinition OutputTarget = new RenderTargetDefinition("OutputRenderTarget", false, 1);
    public static Texture2dDefinition OutputResult = new Texture2dDefinition("OutputResult");
    public static Texture2dDefinition VSM_ShadowMap = new Texture2dDefinition("VSM ShadowMap", Image.Format.RG32F, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);

    public static Texture2dDefinition BaseColor = new Texture2dDefinition("BaseColorsSpecular", Image.Format.RGB16F, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);
    public static Texture2dDefinition MetallicRoughness = new Texture2dDefinition("BaseColorsSpecular", Image.Format.RGB16F, ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);


    public static RenderTargetDefinition GBufferMetallicRoughness = new RenderTargetDefinition("GBuffer Metallic Roughness",false,1,WorldNormals,BaseColor,MetallicRoughness,DepthStencil);
}
