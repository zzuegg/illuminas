package tech.diis.illuminas.jme;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import tech.diis.illuminas.jme.lights.ExtendedPointLight;
import tech.diis.illuminas.jme.lights.ExtendedSpotLight;
import tech.diis.illuminas.rendertasks.shadows.ShadowMode;
import tech.diis.illuminas.rendertasks.shadows.ShadowModes;

public class IntegrationUtil {
    public static Material MetallicRoughtness;
    public static Material BlinnPhong;

    public static void initialize(AssetManager assetManager) {
        MetallicRoughtness = new Material(assetManager, "Materials/Illuminas/MetallicRoughness.j3md");
        //MetallicRoughtness.setFloat("AlphaDiscardThreshold", 0.5f);
        BlinnPhong = new Material(assetManager, "Materials/Illuminas/BlinnPhong.j3md");
    }

    public static Spatial convertToIlluminas(Material material, Spatial loadModel) {

        for (Light light : loadModel.getLocalLightList()) {
            if (light instanceof DirectionalLight) {
                loadModel.removeLight(light);
            }
            if (light instanceof SpotLight spotLight) {
                loadModel.removeLight(light);
                ExtendedSpotLight extendedSpotLight = new ExtendedSpotLight();
                extendedSpotLight.setVolumetricIntensity(0.02f);
                extendedSpotLight.setShadowMapSize(1024);
                extendedSpotLight.setShadowMode(ShadowModes.VSM);
                extendedSpotLight.setCastingVolumetric(false);
                extendedSpotLight.setShadowCasting(false);
                extendedSpotLight.setColor(light.getColor());
                extendedSpotLight.setSpotRange(spotLight.getSpotRange());
                extendedSpotLight.setSpotOuterAngle(spotLight.getSpotOuterAngle());
                extendedSpotLight.setSpotInnerAngle(spotLight.getSpotInnerAngle());
                extendedSpotLight.setDirection(spotLight.getDirection());
                extendedSpotLight.setPosition(spotLight.getPosition());
                loadModel.addLight(extendedSpotLight);
            }
            if (light instanceof PointLight pointLight) {
                loadModel.removeLight(light);
                ExtendedPointLight extendedSpotLight = new ExtendedPointLight();
                extendedSpotLight.setVolumetricIntensity(0.001f);
                extendedSpotLight.setShadowMapSize(1024);
                extendedSpotLight.setShadowMode(ShadowModes.VSM);
                extendedSpotLight.setVolumetricCasting(false);
                extendedSpotLight.setShadowCasting(false);
                if (Float.isInfinite(pointLight.getRadius())) {
                    extendedSpotLight.setRadius(10f);
                } else {
                    extendedSpotLight.setRadius(pointLight.getRadius());
                }
                extendedSpotLight.setColor(light.getColor());
                extendedSpotLight.setPosition(pointLight.getPosition());
                loadModel.addLight(extendedSpotLight);
                System.out.println(extendedSpotLight);
            }
        }
        if (loadModel instanceof Geometry geometry) {
            //geometry.setQueueBucket(RenderQueue.Bucket.Opaque);
            //geometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            Material material1 = geometry.getMaterial();
            Material currentMaterial = material.clone();
            currentMaterial.getAdditionalRenderState().setBlendMode(material1.getAdditionalRenderState().getBlendMode());
            currentMaterial.getAdditionalRenderState().setFaceCullMode(material1.getAdditionalRenderState().getFaceCullMode());
            for (MatParam param : material1.getParams()) {
                copyParam(param.getName(), material1, currentMaterial);
            }
            geometry.setMaterial(currentMaterial);
        } else {
            if (loadModel instanceof Node node) {
                for (Spatial child : node.getChildren()) {
                    convertToIlluminas(material, child);
                }
            }
        }
        return loadModel;
    }

    private static void copyParam(String param, Material source, Material destination) {
        MatParam param1 = source.getParam(param);

        if (destination.getMaterialDef().getMaterialParam(param) != null) {
            destination.setParam(param1.getName(), param1.getVarType(), param1.getValue());
        } else {
            System.out.println("Missing param: " + param1);
        }

    }
}
