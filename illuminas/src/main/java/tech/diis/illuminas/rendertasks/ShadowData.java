package tech.diis.illuminas.rendertasks;

import com.jme3.anim.AnimComposer;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;
import tech.diis.illuminas.ObjectDefinition;
import tech.diis.illuminas.PipelineContext;

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

    public void populate(PipelineContext renderPipeline, Spatial scene, Camera camera) {
        populate(renderPipeline,scene, RenderQueue.Bucket.Opaque, camera);
    }

    public void populate(PipelineContext renderPipeline,Spatial scene, RenderQueue.Bucket inherited, Camera camera) {
        if (camera.contains(scene.getWorldBound()) != Camera.FrustumIntersect.Outside) {
            if(scene.getControl(AnimComposer.class)!=null){
                scene.runControlRender(renderPipeline.getRenderManager(),null);
            }
            if (scene.getQueueBucket() != RenderQueue.Bucket.Inherit) {
                inherited = scene.getQueueBucket();
            }
            if (scene instanceof Node node) {
                camera.setPlaneState(0);
                for (Spatial child : node.getChildren()) {
                    populate(renderPipeline,child, inherited, camera);
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
