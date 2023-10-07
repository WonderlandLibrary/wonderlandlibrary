package viaversion.viaversion.api.type.types.minecraft;

import viaversion.viaversion.api.minecraft.chunks.Chunk;
import viaversion.viaversion.api.type.Type;

public abstract class BaseChunkType extends Type<Chunk> {
    public BaseChunkType() {
        super("Chunk", Chunk.class);
    }

    public BaseChunkType(String typeName) {
        super(typeName, Chunk.class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
}
