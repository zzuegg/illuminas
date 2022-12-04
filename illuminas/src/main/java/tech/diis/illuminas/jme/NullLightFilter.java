package tech.diis.illuminas.jme;

import com.jme3.light.LightFilter;
import com.jme3.light.LightList;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

public class NullLightFilter implements LightFilter {
    @Override
    public void setCamera(Camera camera) {

    }

    @Override
    public void filterLights(Geometry geometry, LightList filteredLightList) {

    }

    private static NullLightFilter nullLightFilter = new NullLightFilter();
    private static LightFilter currentLightFilter;
    private static RenderManager currentRenderManager;

    public static void applyTo(RenderManager renderManager) {
        currentRenderManager = renderManager;
        currentLightFilter = renderManager.getLightFilter();
        renderManager.setLightFilter(nullLightFilter);
    }

    public static void revert() {
        currentRenderManager.setLightFilter(currentLightFilter);
    }
}
