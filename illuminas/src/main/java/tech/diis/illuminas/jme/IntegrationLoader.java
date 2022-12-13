package tech.diis.illuminas.jme;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.material.MaterialDef;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.util.blockparser.Statement;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class IntegrationLoader extends ClasspathLocator {
    public IntegrationLoader() {
        super();
    }


    @Override
    public void setRootPath(String s) {
        super.setRootPath(s);
    }

    @SneakyThrows
    @Override
    public AssetInfo locate(AssetManager assetManager, AssetKey assetKey) {
        if (assetKey.getName().equals("Common/MatDefs/Light/PBRLighting.j3md")) {
            Field name = assetKey.getClass().getDeclaredField("name");
            name.setAccessible(true);
            name.set(assetKey, "Materials/Illuminas/MetallicRoughness.j3md");

        }else{
            System.out.println(assetKey);
        }
        return super.locate(assetManager,assetKey);

    }
}
