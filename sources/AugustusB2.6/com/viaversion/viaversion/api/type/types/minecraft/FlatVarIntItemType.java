// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatVarIntItemType extends BaseItemType
{
    public FlatVarIntItemType() {
        super("FlatVarIntItem");
    }
    
    @Override
    public Item read(final ByteBuf buffer) throws Exception {
        final boolean present = buffer.readBoolean();
        if (!present) {
            return null;
        }
        final Item item = new DataItem();
        item.setIdentifier(FlatVarIntItemType.VAR_INT.readPrimitive(buffer));
        item.setAmount(buffer.readByte());
        item.setTag(FlatVarIntItemType.NBT.read(buffer));
        return item;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item object) throws Exception {
        if (object == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            FlatVarIntItemType.VAR_INT.writePrimitive(buffer, object.identifier());
            buffer.writeByte(object.amount());
            FlatVarIntItemType.NBT.write(buffer, object.tag());
        }
    }
}
