package viaversion.viaversion.api.type.types.version;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.types.minecraft.AbstractMetaListType;

public class MetadataList1_8Type extends AbstractMetaListType {
    @Override
    protected Type<Metadata> getType() {
        return Types1_8.METADATA;
    }

    @Override
    protected void writeEnd(final Type<Metadata> type, final ByteBuf buffer) throws Exception {
        buffer.writeByte(0x7f);
    }
}
