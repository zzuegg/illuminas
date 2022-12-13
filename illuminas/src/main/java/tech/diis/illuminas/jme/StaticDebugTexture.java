package tech.diis.illuminas.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

public class StaticDebugTexture {
    public static Texture[] texture;
    public Geometry[] geo;

    public StaticDebugTexture(AssetManager assetManager) {
        texture = new Texture[6];
        geo = new Geometry[6];
        for (int i = 0; i < 6; i++) {
            geo[i] = new Geometry("Debug", new Quad(1, 1));
            geo[i].setLocalTranslation(i * 200, 200, 0);
            geo[i].setLocalScale(200);
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            geo[i].setMaterial(material.clone());
        }

    }

    public void update() {
        for (int i = 0; i < 6; i++) {
            if (texture[i] != null) {
                geo[i].getMaterial().setTexture("ColorMap", texture[i]);
            }
        }
    }
}
