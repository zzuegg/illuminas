package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.shadow.ShadowUtil;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.jme.lights.ExtendedDirectionalLight;
import tech.diis.illuminas.rendertasks.lights.LightMode;

import java.util.Set;

public class DirectionalLightShadowRenderer {

    private final GeometryBasedShadows geometryBasedShadows;
    private final AssetManager assetManager;
    private final LightMode lightMode;
    private final RenderState renderState;

    public DirectionalLightShadowRenderer(GeometryBasedShadows geometryBasedShadows, AssetManager assetManager, LightMode lightMode, RenderState renderState) {
        this.geometryBasedShadows = geometryBasedShadows;
        this.assetManager = assetManager;
        this.lightMode = lightMode;
        this.renderState = renderState;

    }

    public void renderLights(PipelineContext renderPipeline, Set<ExtendedDirectionalLight> directionalLights) {
        for (ExtendedDirectionalLight directionalLight : directionalLights) {
            renderLight(renderPipeline, directionalLight);
        }
    }

    private void renderLight(PipelineContext renderPipeline, ExtendedDirectionalLight directionalLight) {
        PipelineContext contextForShadowMode = geometryBasedShadows.getContextForShadowMode(renderPipeline, directionalLight.getShadowMode(), directionalLight.getShadowMapSize());
        int shadowMapSize = directionalLight.getShadowMapSize();
        Vector3f[] points = new Vector3f[8];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vector3f();
        }
        float frustumFar = renderPipeline.getCamera().getFrustumFar();
        float frustumNear = Math.max(renderPipeline.getCamera().getFrustumNear(), 0.001f);
        ShadowUtil.updateFrustumPoints2(renderPipeline.getCamera(), points);
        Vector3f center = new Vector3f(0, 0, 0);
        for (Vector3f point : points) {
            center.addLocal(point);
        }
        center.divideLocal(points.length);
        Camera camera = new Camera(shadowMapSize, shadowMapSize);
        camera.setLocation(center);
        camera.lookAt(directionalLight.getDirection(), Vector3f.UNIT_Y);


    }

    private float[] getSplits(int numberOfSplits, float near, float far, float lambda) {
        float splits[] = new float[numberOfSplits + 1];
        for (int i = 0; i < splits.length; i++) {
            float IDM = i / (float) splits.length;
            float log = near * FastMath.pow((far / near), IDM);
            float uniform = near + (far - near) * IDM;
            splits[i] = log * lambda + uniform * (1.0f - lambda);
        }
        splits[0] = near;
        splits[splits.length - 1] = far;
        return splits;
    }
}
