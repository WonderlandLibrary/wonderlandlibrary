// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import java.io.DataOutput;
import io.netty.buffer.ByteBufOutputStream;
import java.io.DataInput;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import com.viaversion.viaversion.libs.opennbt.tag.limiter.TagLimiter;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;

public class NBTType extends Type<CompoundTag>
{
    private static final int MAX_NBT_BYTES = 2097152;
    private static final int MAX_NESTING_LEVEL = 512;
    
    public NBTType() {
        super(CompoundTag.class);
    }
    
    @Override
    public CompoundTag read(final ByteBuf buffer) throws Exception {
        final int readerIndex = buffer.readerIndex();
        final byte b = buffer.readByte();
        if (b == 0) {
            return null;
        }
        buffer.readerIndex(readerIndex);
        return NBTIO.readTag((DataInput)new ByteBufInputStream(buffer), TagLimiter.create(2097152, 512));
    }
    
    @Override
    public void write(final ByteBuf buffer, final CompoundTag object) throws Exception {
        if (object == null) {
            buffer.writeByte(0);
        }
        else {
            final ByteBufOutputStream bytebufStream = new ByteBufOutputStream(buffer);
            NBTIO.writeTag((DataOutput)bytebufStream, object);
        }
    }
}
