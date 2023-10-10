// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatItemArrayType extends BaseItemArrayType
{
    public FlatItemArrayType() {
        super("Flat Item Array");
    }
    
    @Override
    public Item[] read(final ByteBuf buffer) throws Exception {
        final int amount = Type.SHORT.readPrimitive(buffer);
        final Item[] array = new Item[amount];
        for (int i = 0; i < amount; ++i) {
            array[i] = Type.FLAT_ITEM.read(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item[] object) throws Exception {
        Type.SHORT.writePrimitive(buffer, (short)object.length);
        for (final Item o : object) {
            Type.FLAT_ITEM.write(buffer, o);
        }
    }
}
