package tech.diis.illuminas.jme.lights;

import com.jme3.light.SpotLight;
import lombok.Getter;
import lombok.Setter;
import tech.diis.illuminas.rendertasks.shadows.ShadowMode;

public class ExtendedSpotLight extends SpotLight {
    @Getter
    @Setter
    boolean shadowCasting;

    @Getter
    @Setter
    ShadowMode shadowMode;

    @Getter
    @Setter
    int shadowMapSize;

    @Getter
    @Setter
    boolean castingVolumetric;
}
