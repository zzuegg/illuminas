package tech.diis.illuminas;

public class ObjectDefinition<OBJECT> extends ResourceDefinition<OBJECT> {
    public ObjectDefinition(String name) {
        super(name);
    }

    public OBJECT get(PipelineContext renderPipeline) {
        return (OBJECT) renderPipeline.getPipelineResource(this);
    }

    public void set(PipelineContext renderPipeline, OBJECT object) {
        renderPipeline.setPipelineResource(this, object);
    }
}
