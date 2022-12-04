package tech.diis.illuminas.rendertasks.shadows.geometricshadows;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipelineLayout;
import tech.diis.illuminas.RenderTask;
import tech.diis.illuminas.jme.lights.ExtendedPointLight;
import tech.diis.illuminas.jme.lights.ExtendedSpotLight;
import tech.diis.illuminas.rendertasks.WorldData;
import tech.diis.illuminas.rendertasks.lights.LightMode;
import tech.diis.illuminas.rendertasks.shadows.ShadowMode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GeometryBasedShadows extends RenderTask {

    private final RenderState renderState;
    Map<ShadowMode, Map<Integer, PipelineContext>> shadowContextsCache;
    GeometryBasedSpotLightsShadowRenderer geometryBasedSpotLightsShadows;
    GeometryBasedPointLightsShadowRenderer geometryBasedPointLightsShadowRenderer;
    final LightMode lightMode;

    public GeometryBasedShadows(LightMode lightMode) {
        super("GeometryBasedShadows");
        shadowContextsCache = new HashMap<>();

        this.lightMode = lightMode;
        this.renderState = new RenderState();
        this.renderState.setBlendMode(RenderState.BlendMode.Additive);
        this.renderState.setFaceCullMode(RenderState.FaceCullMode.Front);
        this.renderState.setDepthFunc(RenderState.TestFunction.GreaterOrEqual);
        this.renderState.setDepthTest(true);
        this.renderState.setWireframe(false);
        this.renderState.setDepthWrite(false);
    }

    @Override
    protected void initialize(RenderPipelineLayout pipelineLayout, AssetManager assetManager) {
        pipelineLayout.requires(WorldData.worldData);
        geometryBasedSpotLightsShadows = new GeometryBasedSpotLightsShadowRenderer(this, assetManager, lightMode, 1, renderState);
        geometryBasedPointLightsShadowRenderer = new GeometryBasedPointLightsShadowRenderer(this, assetManager, lightMode, 1, renderState);
    }

    @Override
    protected void execute(PipelineContext renderPipeline) {
        RenderState tmp = renderPipeline.getRenderManager().getForcedRenderState();
        renderPipeline.getRenderManager().setForcedRenderState(renderState);
        WorldData worldData = WorldData.worldData.get(renderPipeline);
        Set<SpotLight> shadowCastingSpotlights = worldData.getSpotLights().stream().filter(sl -> sl instanceof ExtendedSpotLight).filter(sl -> ((ExtendedSpotLight) sl).isShadowCasting()).collect(Collectors.toSet());
        geometryBasedSpotLightsShadows.renderLights(renderPipeline, shadowCastingSpotlights);
        //Set<PointLight> shadowCastingPointlights = worldData.getPointLights().stream().filter(sl -> sl instanceof ExtendedPointLight).filter(sl -> ((ExtendedPointLight) sl).isShadowCasting()).collect(Collectors.toSet());
        //geometryBasedPointLightsShadowRenderer.renderLights(renderPipeline,shadowCastingPointlights);
        geometryBasedPointLightsShadowRenderer.renderLights(renderPipeline, spotlightsFromPointlights(worldData.getPointLights()));
        renderPipeline.getRenderManager().setForcedRenderState(tmp);
    }

    private Vector3f[] spotLightDirections = new Vector3f[]{
            Vector3f.UNIT_X, Vector3f.UNIT_X.negate(),
            Vector3f.UNIT_Y, Vector3f.UNIT_Y.negate(),
            Vector3f.UNIT_Z, Vector3f.UNIT_Z.negate()
    };

    private static Quaternion rotate = new Quaternion().fromAngles(1, 1, 1);

    private Set<SpotLight> spotlightsFromPointlights(Set<PointLight> collect) {
        Set<SpotLight> spotLights = new HashSet<>();
        for (PointLight pointLight : collect) {
            for (int i = 0; i < 6; i++) {
                if (pointLight instanceof ExtendedPointLight) {
                    ExtendedSpotLight spotLight = new ExtendedSpotLight();
                    spotLight.setShadowCasting(true);
                    spotLight.setShadowMode(((ExtendedPointLight) pointLight).getShadowMode());
                    spotLight.setShadowMapSize(((ExtendedPointLight) pointLight).getShadowMapSize());
                    spotLight.setPosition(pointLight.getPosition());
                    spotLight.setSpotRange(pointLight.getRadius());
                    spotLight.setSpotOuterAngle(FastMath.HALF_PI / 2f);
                    spotLight.setSpotInnerAngle(FastMath.HALF_PI / 4f);
                    spotLight.setDirection(spotLightDirections[i]);
                    spotLight.setColor(pointLight.getColor());
                    spotLights.add(spotLight);
                }
            }
        }
        return spotLights;
    }

    protected PipelineContext getContextForShadowMode(PipelineContext pipelineContext, ShadowMode shadowMode, int size) {
        Map<Integer, PipelineContext> integerPipelineContextMap = shadowContextsCache.get(shadowMode);
        if (integerPipelineContextMap == null) {
            integerPipelineContextMap = new HashMap<>();
            shadowContextsCache.put(shadowMode, integerPipelineContextMap);
        }
        PipelineContext shadowPipelineContext = integerPipelineContextMap.get(size);
        if (shadowPipelineContext == null) {
            shadowPipelineContext = pipelineContext.createNewContext(shadowMode.getRenderPipeline());
            integerPipelineContextMap.put(size, shadowPipelineContext);
        }
        return shadowPipelineContext;
    }
}
