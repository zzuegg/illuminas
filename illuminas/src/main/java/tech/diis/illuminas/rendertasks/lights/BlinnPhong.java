package tech.diis.illuminas.rendertasks.lights;

import com.jme3.material.Material;
import tech.diis.illuminas.*;

public class BlinnPhong implements LightMode {
    final Texture2dDefinition worldNormals;
    final Texture2dDefinition baseColorsSpecular;
    final Texture2dDefinition depth;
    final RenderTargetDefinition renderTargetDefinition;

    public BlinnPhong(Texture2dDefinition worldNormals, Texture2dDefinition baseColorsSpecular, Texture2dDefinition depth, RenderTargetDefinition renderTargetDefinition) {
        this.worldNormals = worldNormals;
        this.baseColorsSpecular = baseColorsSpecular;
        this.depth = depth;
        this.renderTargetDefinition = renderTargetDefinition;
    }

    @Override
    public void setMaterialParameters(PipelineContext renderPipeline, Material material) {
        material.setTexture("NormalDepth", worldNormals.get(renderPipeline));
        material.setTexture("AlbedoSpecular", baseColorsSpecular.get(renderPipeline));
        material.setTexture("Depth", depth.get(renderPipeline));
    }

    @Override
    public ResourceDefinition<?>[] getRequiredResources() {
        return new ResourceDefinition[]{
                worldNormals, baseColorsSpecular, depth
        };
    }

    @Override
    public RenderTargetDefinition getRenderTargetDefinition() {
        return renderTargetDefinition;
    }
}
