package tech.diis.illuminas.rendertasks.lights;

import tech.diis.illuminas.Constants;

public class LightModes {
    public static BlinnPhong BlinnPhong = new BlinnPhong(Constants.WorldNormals, Constants.BaseColorsSpecular, Constants.DepthStencil, Constants.GBufferBlinnPhong);
}
