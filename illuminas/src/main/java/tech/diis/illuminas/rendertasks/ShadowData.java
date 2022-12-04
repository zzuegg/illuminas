package tech.diis.illuminas.rendertasks;

import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;
import tech.diis.illuminas.ObjectDefinition;

@Getter
public class ShadowData {
    public static final ObjectDefinition<ShadowData> ShadowData = new ObjectDefinition<>("ShadowData");
    private GeometryList shadowCasters;


    public ShadowData() {
        shadowCasters = new GeometryList(new OpaqueComparator());

    }

    public void clear() {
        shadowCasters.clear();

    }

    public void populate(Spatial scene, Camera camera) {
        populate(scene, RenderQueue.Bucket.Opaque, camera);
    }

    public void populate(Spatial scene, RenderQueue.Bucket inherited, Camera camera) {
        if (camera.contains(scene.getWorldBound()) != Camera.FrustumIntersect.Outside) {
            if (scene.getQueueBucket() != RenderQueue.Bucket.Inherit) {
                inherited = scene.getQueueBucket();
            }
            if (scene instanceof Node node) {
                camera.setPlaneState(0);
                for (Spatial child : node.getChildren()) {
                    populate(child, inherited, camera);
                    camera.setPlaneState(0);
                }
            } else if (scene instanceof Geometry geometry) {
                populateGeometry(geometry, inherited);
            }
        }
    }

    private void populateGeometry(Geometry geometry, RenderQueue.Bucket inherited) {
        switch (geometry.getQueueBucket()) {
            case Opaque, Transparent -> shadowCasters.add(geometry);
            case Inherit -> populateGeometry(geometry, inherited);
            case Gui -> {
            }
            default -> {

            }
        }
    }
}
