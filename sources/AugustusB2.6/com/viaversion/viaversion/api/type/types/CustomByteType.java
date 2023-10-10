// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.PartialType;

public class CustomByteType extends PartialType<byte[], Integer>
{
    public CustomByteType(final Integer param) {
        super(param, byte[].class);
    }
    
    @Override
    public byte[] read(final ByteBuf byteBuf, final Integer integer) throws Exception {
        if (byteBuf.readableBytes() < integer) {
            throw new RuntimeException("Readable bytes does not match expected!");
        }
        final byte[] byteArray = new byte[(int)integer];
        byteBuf.readBytes(byteArray);
        return byteArray;
    }
    
    @Override
    public void write(final ByteBuf byteBuf, final Integer integer, final byte[] bytes) throws Exception {
        byteBuf.writeBytes(bytes);
    }
}
