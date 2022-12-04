package tech.diis.illuminas;

public interface RenderPipelineLayout {
    void requires(ResourceDefinition<?>... resourceDefinitions);

    void produces(ResourceDefinition<?>... resourceDefinitions);

    com.jme3.asset.AssetManager getAssetManager();
}
