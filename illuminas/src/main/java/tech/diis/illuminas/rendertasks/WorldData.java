package tech.diis.illuminas.rendertasks;

import com.jme3.light.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.ShadowUtil;
import com.jme3.util.TempVars;
import lombok.Getter;
import tech.diis.illuminas.ObjectDefinition;

import java.util.HashSet;
import java.util.Set;

@Getter
public class WorldData {
    public static final ObjectDefinition<WorldData> worldData = new ObjectDefinition<>("WorldData");
    private GeometryList opaqueGeometries;
    private GeometryList transparentGeometries;
    private GeometryList translucentGeometries;
    private GeometryList skyGeometries;

    private Set<DirectionalLight> directionalLights;
    private Set<AmbientLight> ambientLights;
    private Set<PointLight> pointLights;
    private Set<SpotLight> spotLights;
    private Set<LightProbe> lightProbes;

    public WorldData() {
        opaqueGeometries = new GeometryList(new OpaqueComparator());
        translucentGeometries = new GeometryList(new TransparentComparator());
        transparentGeometries = new GeometryList(new TransparentComparator());
        skyGeometries = new GeometryList(new NullComparator());

        directionalLights = new HashSet<>();
        ambientLights = new HashSet<>();
        pointLights = new HashSet<>();
        spotLights = new HashSet<>();
        lightProbes = new HashSet<>();
    }

    public void clear() {
        opaqueGeometries.clear();
        translucentGeometries.clear();
        transparentGeometries.clear();
        skyGeometries.clear();
        directionalLights.clear();
        ambientLights.clear();
        pointLights.clear();
        spotLights.clear();
        lightProbes.clear();
    }

    public void populate(Spatial scene, Camera camera) {
        TempVars tempVars = TempVars.get();
        populate(scene, RenderQueue.Bucket.Opaque, camera, tempVars);
        tempVars.release();
    }

    public void populate(Spatial scene, RenderQueue.Bucket inherited, Camera camera, TempVars tempVars) {

        if (camera.contains(scene.getWorldBound()) != Camera.FrustumIntersect.Outside) {
            if (scene.getQueueBucket() != RenderQueue.Bucket.Inherit) {
                inherited = scene.getQueueBucket();
            }
            populateLights(scene, tempVars, camera, scene.getLocalLightList());
            if (scene instanceof Node node) {
                camera.setPlaneState(0);
                for (Spatial child : node.getChildren()) {
                    populate(child, inherited, camera, tempVars);
                    camera.setPlaneState(0);
                }
            } else if (scene instanceof Geometry geometry) {
                populateGeometry(geometry, inherited);
            }
        }
    }

    private void populateGeometry(Geometry geometry, RenderQueue.Bucket inherited) {
        switch (geometry.getQueueBucket()) {
            case Opaque -> opaqueGeometries.add(geometry);
            case Transparent -> transparentGeometries.add(geometry);
            case Sky -> skyGeometries.add(geometry);
            case Translucent -> translucentGeometries.add(geometry);
            case Inherit -> populateGeometry(geometry, inherited);
            case Gui -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + geometry.getQueueBucket());
        }
    }

    private void populateLights(Spatial holder, TempVars tempVars, Camera camera, LightList lightList) {

        for (int i = 0; i < lightList.size(); i++) {
            Light light = lightList.get(i);
            if (light.isEnabled()) {
                if (light.getColor().toVector4f().equals(Vector4f.NAN)) {
                    continue;
                }
                if (light.intersectsFrustum(camera, tempVars)) {
                    switch (light.getType()) {
                        case Directional -> directionalLights.add((DirectionalLight) light);
                        case Point -> pointLights.add((PointLight) light);
                        case Spot -> spotLights.add((SpotLight) light);
                        case Ambient -> ambientLights.add((AmbientLight) light);
                        case Probe -> lightProbes.add((LightProbe) light);
                    }
                }
            }
        }
    }
}
