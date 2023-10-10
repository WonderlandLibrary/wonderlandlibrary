// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatVarIntItemArrayType extends BaseItemArrayType
{
    public FlatVarIntItemArrayType() {
        super("Flat Item Array");
    }
    
    @Override
    public Item[] read(final ByteBuf buffer) throws Exception {
        final int amount = FlatVarIntItemArrayType.SHORT.readPrimitive(buffer);
        final Item[] array = new Item[amount];
        for (int i = 0; i < amount; ++i) {
            array[i] = FlatVarIntItemArrayType.FLAT_VAR_INT_ITEM.read(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item[] object) throws Exception {
        FlatVarIntItemArrayType.SHORT.writePrimitive(buffer, (short)object.length);
        for (final Item o : object) {
            FlatVarIntItemArrayType.FLAT_VAR_INT_ITEM.write(buffer, o);
        }
    }
}
