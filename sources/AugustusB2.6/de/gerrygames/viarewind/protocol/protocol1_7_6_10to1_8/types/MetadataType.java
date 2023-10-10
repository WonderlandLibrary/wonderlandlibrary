// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.MetaTypeTemplate;

public class MetadataType extends MetaTypeTemplate
{
    @Override
    public Metadata read(final ByteBuf buffer) throws Exception {
        final byte item = buffer.readByte();
        if (item == 127) {
            return null;
        }
        final int typeID = (item & 0xE0) >> 5;
        final MetaType1_7_6_10 type = MetaType1_7_6_10.byId(typeID);
        final int id = item & 0x1F;
        return new Metadata(id, type, type.type().read(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final Metadata meta) throws Exception {
        final int item = (meta.metaType().typeId() << 5 | (meta.id() & 0x1F)) & 0xFF;
        buffer.writeByte(item);
        meta.metaType().type().write(buffer, meta.getValue());
    }
}
