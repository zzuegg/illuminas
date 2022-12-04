package tech.diis.illuminas.rendertasks.lights.geometriclights;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;

import java.lang.reflect.Field;

public class PointLightGeometry {
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
            vertexCountField.set(geometry.getMesh(), count * 144);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static Geometry createGeometry(int maxCount) {
        Vector3f[] positions = new Vector3f[30];
        Sphere sphere = new Sphere(10, 10, 1);
        positions[0] = new Vector3f(0.5877852f, 0.0f, -0.80901706f);
        positions[1] = new Vector3f(0.29389256f, 0.5090369f, -0.80901706f);
        positions[2] = new Vector3f(-0.29389262f, 0.5090369f, -0.80901706f);
        positions[3] = new Vector3f(-0.5877852f, -5.138581E-8f, -0.80901706f);
        positions[4] = new Vector3f(-0.29389253f, -0.5090369f, -0.80901706f);
        positions[5] = new Vector3f(0.29389253f, -0.5090369f, -0.80901706f);
        positions[6] = new Vector3f(0.5877852f, 0.0f, -0.80901706f);
        positions[7] = new Vector3f(0.95105654f, 0.0f, -0.30901697f);
        positions[8] = new Vector3f(0.47552824f, 0.82363915f, -0.30901697f);
        positions[9] = new Vector3f(-0.47552833f, 0.8236391f, -0.30901697f);
        positions[10] = new Vector3f(-0.95105654f, -8.3144E-8f, -0.30901697f);
        positions[11] = new Vector3f(-0.47552818f, -0.82363915f, -0.30901697f);
        positions[12] = new Vector3f(0.47552818f, -0.82363915f, -0.30901697f);
        positions[13] = new Vector3f(0.95105654f, 0.0f, -0.30901697f);
        positions[14] = new Vector3f(0.9510565f, 0.0f, 0.3090171f);
        positions[15] = new Vector3f(0.4755282f, 0.8236391f, 0.3090171f);
        positions[16] = new Vector3f(-0.4755283f, 0.82363904f, 0.3090171f);
        positions[17] = new Vector3f(-0.9510565f, -8.3143995E-8f, 0.3090171f);
        positions[18] = new Vector3f(-0.47552815f, -0.8236391f, 0.3090171f);
        positions[19] = new Vector3f(0.47552815f, -0.8236391f, 0.3090171f);
        positions[20] = new Vector3f(0.9510565f, 0.0f, 0.3090171f);
        positions[21] = new Vector3f(0.5877852f, 0.0f, 0.80901706f);
        positions[22] = new Vector3f(0.29389256f, 0.5090369f, 0.80901706f);
        positions[23] = new Vector3f(-0.29389262f, 0.5090369f, 0.80901706f);
        positions[24] = new Vector3f(-0.5877852f, -5.138581E-8f, 0.80901706f);
        positions[25] = new Vector3f(-0.29389253f, -0.5090369f, 0.80901706f);
        positions[26] = new Vector3f(0.29389253f, -0.5090369f, 0.80901706f);
        positions[27] = new Vector3f(0.5877852f, 0.0f, 0.80901706f);
        positions[28] = new Vector3f(0.0f, 0.0f, -1.0f);
        positions[29] = new Vector3f(0.0f, 0.0f, 1.0f);
        int[] indices = new int[]{
                0, 1, 7, 1, 8, 7, 1, 2, 8, 2, 9, 8, 2, 3, 9, 3, 10, 9, 3, 4, 10, 4, 11, 10, 4, 5, 11, 5, 12, 11, 5, 6, 12, 6, 13, 12, 7, 8, 14, 8, 15, 14, 8, 9, 15, 9, 16, 15, 9, 10, 16, 10, 17, 16, 10, 11, 17, 11, 18, 17, 11, 12, 18, 12, 19, 18, 12, 13, 19, 13, 20, 19, 14, 15, 21, 15, 22, 21, 15, 16, 22, 16, 23, 22, 16, 17, 23, 17, 24, 23, 17, 18, 24, 18, 25, 24, 18, 19, 25, 19, 26, 25, 19, 20, 26, 20, 27, 26, 0, 28, 1, 1, 28, 2, 2, 28, 3, 3, 28, 4, 4, 28, 5, 5, 28, 6, 21, 22, 29, 22, 23, 29, 23, 24, 29, 24, 25, 29, 25, 26, 29, 26, 27, 29};
        Vector3f[] pTmp = new Vector3f[maxCount * indices.length];
        for (int i = 0; i < maxCount; i++) {
            for (int j = 0; j < indices.length; j++) {
                pTmp[i * indices.length + j] = positions[indices[j]];
            }
        }
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(pTmp));
        Geometry tmp = new Geometry("PointLight", mesh);
        return tmp;
    }
}
