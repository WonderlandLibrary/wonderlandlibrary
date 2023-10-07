package viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;

public abstract class ModernMetaListType extends AbstractMetaListType {

    @Override
    protected void writeEnd(final Type<Metadata> type, final ByteBuf buffer) throws Exception {
        type.write(buffer, null);
    }
}
