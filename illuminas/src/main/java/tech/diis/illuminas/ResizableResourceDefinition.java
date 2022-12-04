package tech.diis.illuminas;

public abstract class ResizableResourceDefinition<RESOURCE> extends ResourceDefinition<RESOURCE> {
    protected ResizableResourceDefinition(String name) {
        super(name);
    }

    protected abstract void resize(int width, int height, PipelineContext pipelineContext);
}
