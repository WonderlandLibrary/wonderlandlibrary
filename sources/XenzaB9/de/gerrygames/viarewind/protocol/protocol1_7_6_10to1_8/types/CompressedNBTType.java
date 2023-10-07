// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;

public class CompressedNBTType extends Type<CompoundTag>
{
    public CompressedNBTType() {
        super(CompoundTag.class);
    }
    
    @Override
    public CompoundTag read(final ByteBuf buffer) throws IOException {
        final short length = buffer.readShort();
        if (length <= 0) {
            return null;
        }
        final ByteBuf compressed = buffer.readSlice(length);
        try (final GZIPInputStream gzipStream = new GZIPInputStream(new ByteBufInputStream(compressed))) {
            return NBTIO.readTag(gzipStream);
        }
    }
    
    @Override
    public void write(final ByteBuf buffer, final CompoundTag nbt) throws Exception {
        if (nbt == null) {
            buffer.writeShort(-1);
            return;
        }
        final ByteBuf compressedBuf = buffer.alloc().buffer();
        try {
            try (final GZIPOutputStream gzipStream = new GZIPOutputStream(new ByteBufOutputStream(compressedBuf))) {
                NBTIO.writeTag(gzipStream, nbt);
            }
            buffer.writeShort(compressedBuf.readableBytes());
            buffer.writeBytes(compressedBuf);
        }
        finally {
            compressedBuf.release();
        }
    }
}
