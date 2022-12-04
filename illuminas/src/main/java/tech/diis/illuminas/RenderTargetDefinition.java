package tech.diis.illuminas;

import com.jme3.renderer.RenderManager;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class RenderTargetDefinition extends ResizableResourceDefinition<FrameBuffer> {
    private final boolean srgb;
    private final int samples;
    private final Texture2dDefinition[] targets;

    public RenderTargetDefinition(String name, boolean srgb, int samples, Texture2dDefinition... targets) {
        super(name);
        this.targets = targets;
        this.srgb = srgb;
        this.samples = samples;
    }

    public void bind(PipelineContext renderPipeline) {
        log.trace("Context: {} Binding Framebuffer: {}",renderPipeline.getName(), getName());
        RenderManager renderManager = renderPipeline.getRenderManager();
        FrameBuffer frameBuffer = renderPipeline.getPipelineResource(this);
        renderManager.getRenderer().setFrameBuffer(frameBuffer);
    }

    @Override
    protected void resize(int width, int height, PipelineContext renderPipeline) {
        if (targets != null) {
            FrameBuffer frameBuffer = renderPipeline.getPipelineResource(this);
            if (frameBuffer != null) {
                frameBuffer.dispose();
            }
            frameBuffer = new FrameBuffer(width, height, samples);
            frameBuffer.setSrgb(srgb);
            int colorBuffers = 0;
            for (int i = 0; i < targets.length; i++) {
                Texture2dDefinition targetDefinition = targets[i];
                Texture targetTexture = renderPipeline.getPipelineResource(targetDefinition);
                if (targetDefinition.getFormat().isDepthFormat()) {
                    log.trace("Context: {}, Binding: {} as depth", getName(), targetDefinition.getName());
                    frameBuffer.setDepthTarget(FrameBuffer.FrameBufferTarget.newTarget(targetTexture));
                } else {
                    log.trace("Context: {}, Binding: {} as colortarget {}", getName(), targetDefinition.getName(), colorBuffers);
                    frameBuffer.addColorTarget(FrameBuffer.FrameBufferTarget.newTarget(targetTexture));
                    colorBuffers++;
                }
            }
            if (colorBuffers > 1) {
                frameBuffer.setMultiTarget(true);
            }
            renderPipeline.setPipelineResource(this, frameBuffer);
        }
    }
}
