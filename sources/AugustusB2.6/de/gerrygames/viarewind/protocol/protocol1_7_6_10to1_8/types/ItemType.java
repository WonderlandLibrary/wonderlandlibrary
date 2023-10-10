// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;

public class ItemType extends Type<Item>
{
    private final boolean compressed;
    
    public ItemType(final boolean compressed) {
        super(Item.class);
        this.compressed = compressed;
    }
    
    @Override
    public Item read(final ByteBuf buffer) throws Exception {
        final int readerIndex = buffer.readerIndex();
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        }
        final Item item = new DataItem();
        item.setIdentifier(id);
        item.setAmount(buffer.readByte());
        item.setData(buffer.readShort());
        item.setTag((this.compressed ? Types1_7_6_10.COMPRESSED_NBT : Types1_7_6_10.NBT).read(buffer));
        return item;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item item) throws Exception {
        if (item == null) {
            buffer.writeShort(-1);
        }
        else {
            buffer.writeShort(item.identifier());
            buffer.writeByte(item.amount());
            buffer.writeShort(item.data());
            (this.compressed ? Types1_7_6_10.COMPRESSED_NBT : Types1_7_6_10.NBT).write(buffer, item.tag());
        }
    }
}
