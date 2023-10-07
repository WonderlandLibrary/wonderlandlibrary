// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import com.viaversion.viaversion.api.minecraft.Environment;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class Chunk1_7_10Type extends PartialType<Chunk, ClientWorld>
{
    public Chunk1_7_10Type(final ClientWorld param) {
        super(param, Chunk.class);
    }
    
    @Override
    public Chunk read(final ByteBuf byteBuf, final ClientWorld clientWorld) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld clientWorld, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        output.writeShort(chunk.getBitmask());
        output.writeShort(0);
        final ByteBuf dataToCompress = output.alloc().buffer();
        try {
            for (int i = 0; i < chunk.getSections().length; ++i) {
                if ((chunk.getBitmask() & 1 << i) != 0x0) {
                    final ChunkSection section = chunk.getSections()[i];
                    for (int j = 0; j < 4096; ++j) {
                        final int block = section.getFlatBlock(j);
                        dataToCompress.writeByte(block >> 4);
                    }
                }
            }
            for (int i = 0; i < chunk.getSections().length; ++i) {
                if ((chunk.getBitmask() & 1 << i) != 0x0) {
                    final ChunkSection section = chunk.getSections()[i];
                    for (int j = 0; j < 4096; j += 2) {
                        final int data0 = section.getFlatBlock(j) & 0xF;
                        final int data2 = section.getFlatBlock(j + 1) & 0xF;
                        dataToCompress.writeByte(data2 << 4 | data0);
                    }
                }
            }
            for (int i = 0; i < chunk.getSections().length; ++i) {
                if ((chunk.getBitmask() & 1 << i) != 0x0) {
                    chunk.getSections()[i].getLight().writeBlockLight(dataToCompress);
                }
            }
            final boolean skyLight = clientWorld != null && clientWorld.getEnvironment() == Environment.NORMAL;
            if (skyLight) {
                for (int k = 0; k < chunk.getSections().length; ++k) {
                    if ((chunk.getBitmask() & 1 << k) != 0x0) {
                        chunk.getSections()[k].getLight().writeSkyLight(dataToCompress);
                    }
                }
            }
            if (chunk.isFullChunk() && chunk.isBiomeData()) {
                for (final int biome : chunk.getBiomeData()) {
                    dataToCompress.writeByte(biome);
                }
            }
            final ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
            try (final DeflaterOutputStream compressorStream = new DeflaterOutputStream(compressedStream)) {
                compressorStream.write(Type.REMAINING_BYTES.read(dataToCompress));
            }
            output.writeInt(compressedStream.size());
            output.writeBytes(compressedStream.toByteArray());
        }
        finally {
            dataToCompress.release();
        }
    }
}
