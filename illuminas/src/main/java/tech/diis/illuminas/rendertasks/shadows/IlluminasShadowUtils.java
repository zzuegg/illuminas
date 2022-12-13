package tech.diis.illuminas.rendertasks.shadows;

import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import tech.diis.illuminas.jme.lights.ExtendedDirectionalLight;
import tech.diis.illuminas.jme.lights.ExtendedPointLight;

public class IlluminasShadowUtils {
    public static void spotLightToCamera(SpotLight spotLight, Camera camera) {
        camera.setFrustumPerspective(spotLight.getSpotOuterAngle() * FastMath.RAD_TO_DEG * 2.0f, 1f, 0.1f, spotLight.getSpotRange());
        if(spotLight.getDirection().equals(Vector3f.UNIT_Y)){
            camera.getRotation().lookAt(spotLight.getDirection(), Vector3f.UNIT_X);
        }else if(spotLight.getDirection().equals(Vector3f.UNIT_Y.negate())){
            camera.getRotation().lookAt(spotLight.getDirection(), Vector3f.UNIT_X);
        }else{
            camera.getRotation().lookAt(spotLight.getDirection(), Vector3f.UNIT_Y);
        }

        camera.setLocation(spotLight.getPosition());
        camera.update();
        camera.updateViewProjection();
    }

    static private Vector3f[] spotLightDirections = new Vector3f[]{
            Vector3f.UNIT_X, Vector3f.UNIT_X.negate(),
            Vector3f.UNIT_Y, Vector3f.UNIT_Y.negate(),
            Vector3f.UNIT_Z, Vector3f.UNIT_Z.negate()
    };

    static private Vector3f[] spotLightDirectionsLeft = new Vector3f[]{
            Vector3f.UNIT_Y, Vector3f.UNIT_Y.negate(),
            Vector3f.UNIT_X, Vector3f.UNIT_X.negate(),
            Vector3f.UNIT_Y, Vector3f.UNIT_Y.negate()
    };


    public static void pointLightToCameras(ExtendedPointLight extendedPointLight, Camera[] cameras) {
        //bottom
        cameras[0].setAxes(Vector3f.UNIT_X.mult(-1f), Vector3f.UNIT_Z.mult(-1f), Vector3f.UNIT_Y.mult(-1f));

        for (int i = 0; i < cameras.length; i++) {
            cameras[i].setAxes(spotLightDirectionsLeft[i],spotLightDirections[i].cross(spotLightDirectionsLeft[i]),spotLightDirections[i]);
            cameras[i].setFrustumPerspective(FastMath.HALF_PI, 1.0f, 0.1f, extendedPointLight.getRadius());
            cameras[i].setLocation(extendedPointLight.getPosition());
            cameras[i].update();
            cameras[i].updateViewProjection();
        }

    }


}
