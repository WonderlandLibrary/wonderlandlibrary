// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.zip.Deflater;
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
        final ByteBuf blockData = output.alloc().buffer();
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                final ChunkSection section = chunk.getSections()[i];
                for (int y = 0; y < 16; ++y) {
                    for (int z = 0; z < 16; ++z) {
                        int previousData = 0;
                        for (int x = 0; x < 16; ++x) {
                            final int block = section.getFlatBlock(x, y, z);
                            dataToCompress.writeByte(block >> 4);
                            final int data = block & 0xF;
                            if (x % 2 == 0) {
                                previousData = data;
                            }
                            else {
                                blockData.writeByte(data << 4 | previousData);
                            }
                        }
                    }
                }
            }
        }
        dataToCompress.writeBytes(blockData);
        blockData.release();
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                chunk.getSections()[i].getLight().writeBlockLight(dataToCompress);
            }
        }
        final boolean skyLight = clientWorld != null && clientWorld.getEnvironment() == Environment.NORMAL;
        if (skyLight) {
            for (int j = 0; j < chunk.getSections().length; ++j) {
                if ((chunk.getBitmask() & 1 << j) != 0x0) {
                    chunk.getSections()[j].getLight().writeSkyLight(dataToCompress);
                }
            }
        }
        if (chunk.isFullChunk() && chunk.isBiomeData()) {
            for (final int biome : chunk.getBiomeData()) {
                dataToCompress.writeByte((byte)biome);
            }
        }
        dataToCompress.readerIndex(0);
        final byte[] data2 = new byte[dataToCompress.readableBytes()];
        dataToCompress.readBytes(data2);
        dataToCompress.release();
        final Deflater deflater = new Deflater(4);
        byte[] compressedData;
        int compressedSize;
        try {
            deflater.setInput(data2, 0, data2.length);
            deflater.finish();
            compressedData = new byte[data2.length];
            compressedSize = deflater.deflate(compressedData);
        }
        finally {
            deflater.end();
        }
        output.writeInt(compressedSize);
        output.writeBytes(compressedData, 0, compressedSize);
    }
}
