// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class ItemArrayType extends BaseItemArrayType
{
    public ItemArrayType() {
        super("Item Array");
    }
    
    @Override
    public Item[] read(final ByteBuf buffer) throws Exception {
        final int amount = ItemArrayType.SHORT.readPrimitive(buffer);
        final Item[] array = new Item[amount];
        for (int i = 0; i < amount; ++i) {
            array[i] = ItemArrayType.ITEM.read(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item[] object) throws Exception {
        ItemArrayType.SHORT.writePrimitive(buffer, (short)object.length);
        for (final Item o : object) {
            ItemArrayType.ITEM.write(buffer, o);
        }
    }
}
