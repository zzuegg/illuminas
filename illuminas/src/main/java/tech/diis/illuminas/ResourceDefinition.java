package tech.diis.illuminas;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class ResourceDefinition<RESOURCE> {
    @Getter(AccessLevel.PACKAGE)
    private final String name;

    protected ResourceDefinition(String name) {
        this.name = name;
    }
}
