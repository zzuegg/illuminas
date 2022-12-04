package tech.diis.illuminas;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import lombok.Getter;

@Getter
public class Texture2dDefinition extends ResizableResourceDefinition<Texture> {
    private final Image.Format format;
    private final ColorSpace colorSpace;
    private final int samples;
    private final Texture.MagFilter magFilter;
    private final Texture.MinFilter minFilter;
    private final Texture.WrapMode wrapModeS;
    private final Texture.WrapMode wrapModeT;

    protected Texture2dDefinition(String name, Image.Format format, ColorSpace colorSpace, int samples, Texture.MagFilter magFilter, Texture.MinFilter minFilter, Texture.WrapMode wrapModeS, Texture.WrapMode wrapModeT) {
        super(name);
        this.format = format;
        this.colorSpace = colorSpace;
        this.samples = samples;
        this.magFilter = magFilter;
        this.minFilter = minFilter;
        this.wrapModeS = wrapModeS;
        this.wrapModeT = wrapModeT;
    }

    public Texture2dDefinition(String name, Image.Format format, ColorSpace colorSpace, Texture.MagFilter magFilter, Texture.MinFilter minFilter) {
        this(name, format, colorSpace, 1, magFilter, minFilter, Texture.WrapMode.EdgeClamp, Texture.WrapMode.EdgeClamp);
    }

    public Texture2dDefinition(String name) {
        this(name, Image.Format.RGB8,ColorSpace.Linear, Texture.MagFilter.Nearest, Texture.MinFilter.NearestNoMipMaps);
    }

    public Texture get(PipelineContext renderPipeline) {
        return renderPipeline.getPipelineResource(this);
    }

    @Override
    protected void resize(int width, int height, PipelineContext renderPipeline) {
        Texture texture = new Texture2D(width, height, format);
        texture.setWrap(Texture.WrapAxis.S, wrapModeS);
        texture.setWrap(Texture.WrapAxis.T, wrapModeT);
        texture.setMagFilter(magFilter);
        texture.setMinFilter(minFilter);
        renderPipeline.setPipelineResource(this, texture);
    }
}
