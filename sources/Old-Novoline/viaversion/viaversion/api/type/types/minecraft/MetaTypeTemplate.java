package viaversion.viaversion.api.type.types.minecraft;

import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;

public abstract class MetaTypeTemplate extends Type<Metadata> {
    public MetaTypeTemplate() {
        super("Metadata type", Metadata.class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return MetaTypeTemplate.class;
    }
}
