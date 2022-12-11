package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.shadow.PssmShadowUtil;
import com.jme3.shadow.ShadowUtil;
import com.jme3.texture.*;
import tech.diis.illuminas.Constants;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.jme.StaticDebugTexture;
import tech.diis.illuminas.jme.lights.ExtendedDirectionalLight;
import tech.diis.illuminas.rendertasks.ShadowData;
import tech.diis.illuminas.rendertasks.lights.LightMode;
import tech.diis.illuminas.rendertasks.lights.geometriclights.GeometryBasedLightsMaterial;
import tech.diis.illuminas.rendertasks.shadows.IlluminasShadowUtils;

import java.util.Arrays;
import java.util.Set;

public class DirectionalLightShadowRenderer {
    private final Material material;

    private final GeometryBasedShadows geometryBasedShadows;
    private final AssetManager assetManager;
    private final LightMode lightMode;
    private final RenderState renderState;

    public DirectionalLightShadowRenderer(GeometryBasedShadows geometryBasedShadows, AssetManager assetManager, LightMode lightMode, RenderState renderState) {
        this.geometryBasedShadows = geometryBasedShadows;
        this.assetManager = assetManager;
        this.lightMode = lightMode;
        this.renderState = renderState;
        this.material = new Material(assetManager, "Materials/Illuminas/BlinnPhongLightVolumes.j3md");
        this.material.getAdditionalRenderState().setDepthTest(false);
        this.material.getAdditionalRenderState().setDepthWrite(false);
        this.material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);

    }

    public void renderLights(PipelineContext renderPipeline, Set<ExtendedDirectionalLight> directionalLights) {
        for (ExtendedDirectionalLight directionalLight : directionalLights) {
            renderLight(renderPipeline, directionalLight);
        }
    }

    private void renderLight(PipelineContext renderPipeline, ExtendedDirectionalLight directionalLight) {
        PipelineContext contextForShadowMode = geometryBasedShadows.getContextForShadowMode(renderPipeline, directionalLight.getShadowMode(), directionalLight.getShadowMapSize());
        Camera camera = new Camera(directionalLight.getShadowMapSize(), directionalLight.getShadowMapSize());
        Vector3f[] vector3fs = new Vector3f[8];
        for (int i = 0; i < vector3fs.length; i++) {
            vector3fs[i] = new Vector3f();
        }
        ShadowUtil.updateFrustumPoints2(renderPipeline.getCamera(), vector3fs);
        Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        System.out.println(min);
        System.out.println(max);
        //camera.setLocation(center.add(directionalLight.getDirection().clone().mult(-20f)));
        camera.setLocation(directionalLight.getDirection().mult(-30));
        camera.setFrustum(0.001f, 30, -10, 10, 10, -10);
        camera.lookAtDirection(directionalLight.getDirection(), Vector3f.UNIT_Y);
        camera.update();
        camera.updateViewProjection();
        contextForShadowMode.execute(camera, renderPipeline.getScene(), null);
        //System.out.println(ShadowData.ShadowData.get(contextForShadowMode).getShadowCasters().size());
        contextForShadowMode.swapPostProcessorTargets();
        StaticDebugTexture.texture[0] = Constants.OutputResult.get(contextForShadowMode);

        Vector3f[] lightPositions = new Vector3f[1];
        Vector3f[] lightDirections = new Vector3f[1];
        Vector3f[] lightColors = new Vector3f[1];
        int count = 0;
        lightDirections[count] = directionalLight.getDirection();
        lightColors[count] = directionalLight.getColor().toVector3f();
        lightPositions[count] = camera.getLocation();

        material.setColor("AmbientLight", ColorRGBA.Black);
        GeometryBasedLightsMaterial.setLightPositions(material, lightPositions);
        GeometryBasedLightsMaterial.setLightCount(material, 1);
        GeometryBasedLightsMaterial.setLightDirections(material, lightDirections);
        GeometryBasedLightsMaterial.setLightColors(material, lightColors);
        GeometryBasedLightsMaterial.setLightMode(renderPipeline, material, lightMode);
        directionalLight.getShadowMode().setMaterialParameters(contextForShadowMode, material, camera);
        renderPipeline.getRenderManager().setForcedTechnique("AmbientDirectionalLightVolumes");
        Constants.FS_QUAD.setMaterial(material);
        Constants.OutputTarget.bind(renderPipeline);
        renderPipeline.getRenderManager().setCamera(renderPipeline.getCamera(),false);
        renderPipeline.getRenderManager().setForcedRenderState(null);
        renderPipeline.getRenderManager().renderGeometry(Constants.FS_QUAD);
        renderPipeline.getRenderManager().setForcedTechnique(null);
    }


}
