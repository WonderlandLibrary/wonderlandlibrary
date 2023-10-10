// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import java.io.DataOutput;
import io.netty.buffer.ByteBufOutputStream;
import java.io.DataInput;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import io.netty.buffer.ByteBufInputStream;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;

public class NBTType extends Type<CompoundTag>
{
    public NBTType() {
        super(CompoundTag.class);
    }
    
    @Override
    public CompoundTag read(final ByteBuf buffer) throws Exception {
        Preconditions.checkArgument(buffer.readableBytes() <= 2097152, "Cannot read NBT (got %s bytes)", new Object[] { buffer.readableBytes() });
        final int readerIndex = buffer.readerIndex();
        final byte b = buffer.readByte();
        if (b == 0) {
            return null;
        }
        buffer.readerIndex(readerIndex);
        return NBTIO.readTag((DataInput)new ByteBufInputStream(buffer));
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
