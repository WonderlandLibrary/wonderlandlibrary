// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class IntArrayType extends Type<int[]>
{
    public IntArrayType() {
        super(int[].class);
    }
    
    @Override
    public int[] read(final ByteBuf byteBuf) throws Exception {
        final byte size = byteBuf.readByte();
        final int[] array = new int[size];
        for (byte i = 0; i < size; ++i) {
            array[i] = byteBuf.readInt();
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf byteBuf, final int[] array) throws Exception {
        byteBuf.writeByte(array.length);
        for (final int i : array) {
            byteBuf.writeInt(i);
        }
    }
}
