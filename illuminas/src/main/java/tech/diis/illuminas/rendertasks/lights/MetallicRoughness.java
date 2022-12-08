package tech.diis.illuminas.rendertasks.lights;

import com.jme3.material.Material;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderTargetDefinition;
import tech.diis.illuminas.ResourceDefinition;
import tech.diis.illuminas.Texture2dDefinition;

public class MetallicRoughness implements LightMode {
    final Texture2dDefinition worldNormals;
    final Texture2dDefinition baseColor;
    final Texture2dDefinition metallicRoughness;
    final Texture2dDefinition depth;
    final RenderTargetDefinition renderTargetDefinition;
    final int lightMode;

    public MetallicRoughness(Texture2dDefinition worldNormals, Texture2dDefinition baseColor, Texture2dDefinition metallicRoughness, Texture2dDefinition depth, RenderTargetDefinition renderTargetDefinition) {
        this.worldNormals = worldNormals;
        this.baseColor = baseColor;
        this.metallicRoughness = metallicRoughness;
        this.depth = depth;
        this.renderTargetDefinition = renderTargetDefinition;
        this.lightMode = 2;
    }

    @Override
    public void setMaterialParameters(PipelineContext renderPipeline, Material material) {
        material.setTexture("NormalDepth", worldNormals.get(renderPipeline));
        material.setTexture("BaseColor", baseColor.get(renderPipeline));
        material.setTexture("MetallicRoughness", metallicRoughness.get(renderPipeline));
        material.setTexture("Depth", depth.get(renderPipeline));
        material.setInt("LightMode", lightMode);
    }

    @Override
    public ResourceDefinition<?>[] getRequiredResources() {
        return new ResourceDefinition[]{
                worldNormals, baseColor, metallicRoughness, depth
        };
    }

    @Override
    public RenderTargetDefinition getRenderTargetDefinition() {
        return renderTargetDefinition;
    }
}
