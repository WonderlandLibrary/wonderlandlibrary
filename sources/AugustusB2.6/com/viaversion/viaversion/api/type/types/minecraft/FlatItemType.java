// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatItemType extends BaseItemType
{
    public FlatItemType() {
        super("FlatItem");
    }
    
    @Override
    public Item read(final ByteBuf buffer) throws Exception {
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        }
        final Item item = new DataItem();
        item.setIdentifier(id);
        item.setAmount(buffer.readByte());
        item.setTag(Type.NBT.read(buffer));
        return item;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item object) throws Exception {
        if (object == null) {
            buffer.writeShort(-1);
        }
        else {
            buffer.writeShort(object.identifier());
            buffer.writeByte(object.amount());
            Type.NBT.write(buffer, object.tag());
        }
    }
}
