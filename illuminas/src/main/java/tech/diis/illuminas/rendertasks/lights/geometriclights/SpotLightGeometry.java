package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.lang.reflect.Field;

public class SpotLightGeometry {
    private static Field vertexCountField;

    public static void updateGeometryForRendering(Geometry geometry, int count) {
        if (vertexCountField == null) {
            try {
                vertexCountField = geometry.getMesh().getClass().getDeclaredField("vertCount");
                vertexCountField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        try {
            vertexCountField.set(geometry.getMesh(), count * 18);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static Geometry createGeometry(int maxCount) {
        Mesh mesh = new Mesh();
        Vector3f[] positions = new Vector3f[5];
        positions[0] = new Vector3f(0, 0, 0);
        positions[1] = new Vector3f(-1, -1, -1);
        positions[2] = new Vector3f(1, -1, -1);
        positions[3] = new Vector3f(1, -1, 1);
        positions[4] = new Vector3f(-1, -1, 1);
        int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3,
                0, 3, 4,
                0, 4, 1,
                1, 3, 2,
                4, 3, 1,
        };
        Vector3f[] pTmp = new Vector3f[maxCount * indices.length];
        for (int i = 0; i < maxCount; i++) {
            for (int j = 0; j < indices.length; j++) {
                pTmp[i * indices.length + j] = positions[indices[j]];
            }
        }
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(pTmp));
        Geometry tmp = new Geometry("SpotLight", mesh);
        return tmp;
    }
}
