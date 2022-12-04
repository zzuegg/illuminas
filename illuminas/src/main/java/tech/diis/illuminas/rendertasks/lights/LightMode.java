package tech.diis.illuminas.rendertasks.lights;

import com.jme3.material.Material;
import tech.diis.illuminas.PipelineContext;
import tech.diis.illuminas.RenderPipeline;
import tech.diis.illuminas.RenderTargetDefinition;
import tech.diis.illuminas.ResourceDefinition;


public interface LightMode {
    void setMaterialParameters(PipelineContext renderPipeline, Material material);

    ResourceDefinition<?>[] getRequiredResources();

    RenderTargetDefinition getRenderTargetDefinition();
}
