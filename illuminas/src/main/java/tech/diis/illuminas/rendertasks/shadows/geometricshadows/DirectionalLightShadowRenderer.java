package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.shader.VarType;
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

import javax.security.auth.login.AccountExpiredException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        int shadowMapSize = directionalLight.getShadowMapSize();
        PipelineContext contextForShadowMode = geometryBasedShadows.getContextForShadowMode(renderPipeline, directionalLight.getShadowMode(), shadowMapSize);
        Camera camera = new Camera(shadowMapSize, shadowMapSize);
        Vector3f[] vector3fs = new Vector3f[8];
        for (int i = 0; i < vector3fs.length; i++) {
            vector3fs[i] = new Vector3f();
        }
        float splits[] = new float[directionalLight.getNumberOfSplits()];
        PssmShadowUtil.updateFrustumSplits(splits, renderPipeline.getCamera().getFrustumNear(), renderPipeline.getCamera().getFrustumFar(), directionalLight.getLambda());
        List<Matrix4f> projectionMatrices = new ArrayList<>();
        for (int j = 0; j < splits.length - 1; j++) {
            //if (j > 1) {
            //    continue;
            //}
            Vector3f center = renderPipeline.getCamera().getLocation().clone();
            center.addLocal(renderPipeline.getCamera().getDirection().mult(splits[j+1]));
            center.addLocal(directionalLight.getDirection().mult(-30));

            camera.setLocation(center);
            //camera.setFrustum(0.0001f, 30, -left, left, top, -top);*/
            camera.lookAtDirection(directionalLight.getDirection(), Vector3f.UNIT_Y);
            camera.setFrustum(0.1f, 300, -splits[j + 1], splits[j + 1], splits[j + 1], -splits[j + 1]);
            System.out.println("SIZE: " + splits[j + 1]);
            //ShadowUtil.updateShadowCamera(camera, vector3fs);
            //Vector3f leftDownNear = camera.getWorldCoordinates(new Vector2f(0, 0), 0);
            //Vector3f rightupNear = camera.getWorldCoordinates(new Vector2f(shadowMapSize, shadowMapSize), 0);
            //System.out.println("**************");
            //System.out.println(camera.getWorldCoordinates(new Vector2f(0, 0), 1));
            //System.out.println(camera.getWorldCoordinates(new Vector2f(shadowMapSize, 0), 1));
            //System.out.println(leftDownNear);
            //System.out.println(rightupNear);
            //camera.setFrustum(0.0001f, 50, -rightupNear.x, leftDownNear.x, 10, -10);
            /*System.out.println("*****************************");
            System.out.println(j);
            System.out.println(left);
            System.out.println(top);
            System.out.println(min);
            System.out.println(max);
            System.out.println(center);*/
            //System.out.println(extents);
            //System.out.println(cameraPosition);
            //camera.setLocation(center.add(directionalLight.getDirection().clone().mult(-20f)));
            //camera.setLocation(directionalLight.getDirection().mult(-30));
            //camera.setFrustum(0.001f, 30, -10, 10, 10, -10);
            //camera.lookAtDirection(directionalLight.getDirection(), Vector3f.UNIT_Y);
            //camera.update();
            //camera.updateViewProjection();
            contextForShadowMode.execute(camera, renderPipeline.getScene(), null);
            System.out.println(ShadowData.ShadowData.get(contextForShadowMode).getShadowCasters().size());
            contextForShadowMode.swapPostProcessorTargets();
            projectionMatrices.add(camera.getViewProjectionMatrix().clone());
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
            Matrix4f[] matrix4fs = projectionMatrices.toArray(new Matrix4f[projectionMatrices.size()]);
            System.out.println("*****");
            System.out.println(matrix4fs.length);
            material.setParam("ProjectionMatrices", VarType.Matrix4Array, matrix4fs);
            material.setParam("ProjectionMatricesCount", VarType.Int, matrix4fs.length);
            directionalLight.getShadowMode().setMaterialParameters(contextForShadowMode, material, camera);
            renderPipeline.getRenderManager().setForcedTechnique("AmbientDirectionalLightVolumes");
            Constants.FS_QUAD.setMaterial(material);
            Constants.OutputTarget.bind(renderPipeline);
            renderPipeline.getRenderManager().setCamera(renderPipeline.getCamera(), false);
            renderPipeline.getRenderManager().setForcedRenderState(null);
            renderPipeline.getRenderManager().renderGeometry(Constants.FS_QUAD);
            renderPipeline.getRenderManager().setForcedTechnique(null);
        }
    }


}
