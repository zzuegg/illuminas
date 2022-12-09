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

    @Getter
    @Setter
    boolean volumetricCasting;

    @Getter
    @Setter
    float volumetricIntensity;

    @Override
    public String toString() {
        return "ExtendedPointLight{" +
                "shadowCasting=" + shadowCasting +
                ", shadowMode=" + shadowMode +
                ", shadowMapSize=" + shadowMapSize +
                ", volumetricCasting=" + volumetricCasting +
                ", volumetricIntensity=" + volumetricIntensity +
                ", position=" + position +
                ", radius=" + radius +
                ", invRadius=" + invRadius +
                ", color=" + color +
                ", lastDistance=" + lastDistance +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                '}';
    }
}
