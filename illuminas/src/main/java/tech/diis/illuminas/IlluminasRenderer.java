package tech.diis.illuminas;

import com.jme3.asset.AssetManager;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;

public class IlluminasRenderer implements SceneProcessor {
    private final PipelineContext renderPipeline;
    private final AssetManager assetManager;
    private final Camera camera;
    private final Node node;
    private ViewPort viewPort;

    public IlluminasRenderer(RenderPipeline renderPipeline, AssetManager assetManager, Camera camera, Node node) {
        this.renderPipeline = new PipelineContext(renderPipeline.getName(), renderPipeline);
        this.assetManager = assetManager;
        this.camera = camera;
        this.node = node;
    }

    @Override
    public void initialize(RenderManager renderManager, ViewPort viewPort) {
        this.renderPipeline.setAssetManager(assetManager);
        this.renderPipeline.setRenderManager(renderManager);
        this.viewPort = viewPort;
    }

    @Override
    public void reshape(ViewPort viewPort, int i, int i1) {
        this.renderPipeline.setResizeRequired(true);
    }

    @Override
    public boolean isInitialized() {
        return viewPort != null;
    }

    @Override
    public void preFrame(float v) {

    }

    @Override
    public void postQueue(RenderQueue renderQueue) {
        this.renderPipeline.execute(viewPort.getCamera(), node, Constants.DisplayOut);
        renderQueue.clear();
    }

    @Override
    public void postFrame(FrameBuffer frameBuffer) {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void setProfiler(AppProfiler appProfiler) {

    }
}
