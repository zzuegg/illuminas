package tech.diis.illuminas.rendertasks.shadows;

import tech.diis.illuminas.Constants;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.rendertasks.CollectShadowData;
import tech.diis.illuminas.rendertasks.common.GaussianFilter;

import javax.sql.rowset.serial.SerialJavaObject;

public class VSM_GF extends ShadowMode {
    public VSM_GF() {
        shadowMode = 1;
        shadowMapTexture = ShadowModes.VSM.shadowMapTexture;
        renderPipeline = new RenderPipeline("VMS-GF-Pipeline", ShadowModes.VSM.shadowMapTexture, Constants.Depth);
        renderPipeline.addRenderTask(new CollectShadowData(true))
                .addRenderTask(new RenderShadowMap(1))
                .addRenderTask(new FilterVSM());
    }

}
