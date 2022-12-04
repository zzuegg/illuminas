package tech.diis.illuminas.jme.lights;

import com.jme3.light.PointLight;
import lombok.Getter;
import lombok.Setter;
import tech.diis.illuminas.rendertasks.shadows.ShadowMode;

public class ExtendedPointLight extends PointLight {
    @Getter
    @Setter
    boolean shadowCasting;

    @Getter
    @Setter
    ShadowMode shadowMode;

    @Getter
    @Setter
    int shadowMapSize;
}
