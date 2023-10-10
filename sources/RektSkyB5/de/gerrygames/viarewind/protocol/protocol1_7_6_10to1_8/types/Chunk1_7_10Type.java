/*
 * Decompiled with CFR 0.152.
 */
package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.PartialType;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import io.netty.buffer.ByteBuf;
import java.util.zip.Deflater;

public class Chunk1_7_10Type
extends PartialType<Chunk, ClientWorld> {
    public Chunk1_7_10Type(ClientWorld param) {
        super(param, Chunk.class);
    }

    @Override
    public Chunk read(ByteBuf byteBuf, ClientWorld clientWorld) throws Exception {
        throw new UnsupportedOperationException();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(ByteBuf output, ClientWorld clientWorld, Chunk chunk) throws Exception {
        int compressedSize;
        byte[] compressedData;
        boolean skyLight;
        int i2;
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        output.writeShort(chunk.getBitmask());
        output.writeShort(0);
        ByteBuf dataToCompress = output.alloc().buffer();
        ByteBuf blockData = output.alloc().buffer();
        for (i2 = 0; i2 < chunk.getSections().length; ++i2) {
            if ((chunk.getBitmask() & 1 << i2) == 0) continue;
            ChunkSection section = chunk.getSections()[i2];
            for (int y2 = 0; y2 < 16; ++y2) {
                for (int z2 = 0; z2 < 16; ++z2) {
                    int previousData = 0;
                    for (int x2 = 0; x2 < 16; ++x2) {
                        int block = section.getFlatBlock(x2, y2, z2);
                        dataToCompress.writeByte(block >> 4);
                        int data = block & 0xF;
                        if (x2 % 2 == 0) {
                            previousData = data;
                            continue;
                        }
                        blockData.writeByte(data << 4 | previousData);
                    }
                }
            }
        }
        dataToCompress.writeBytes(blockData);
        blockData.release();
        for (i2 = 0; i2 < chunk.getSections().length; ++i2) {
            if ((chunk.getBitmask() & 1 << i2) == 0) continue;
            chunk.getSections()[i2].getLight().writeBlockLight(dataToCompress);
        }
        boolean bl = skyLight = clientWorld != null && clientWorld.getEnvironment() == Environment.NORMAL;
        if (skyLight) {
            for (int i3 = 0; i3 < chunk.getSections().length; ++i3) {
                if ((chunk.getBitmask() & 1 << i3) == 0) continue;
                chunk.getSections()[i3].getLight().writeSkyLight(dataToCompress);
            }
        }
        if (chunk.isFullChunk() && chunk.isBiomeData()) {
            for (int biome : chunk.getBiomeData()) {
                dataToCompress.writeByte((byte)biome);
            }
        }
        dataToCompress.readerIndex(0);
        byte[] data = new byte[dataToCompress.readableBytes()];
        dataToCompress.readBytes(data);
        dataToCompress.release();
        Deflater deflater = new Deflater(4);
        try {
            deflater.setInput(data, 0, data.length);
            deflater.finish();
            compressedData = new byte[data.length];
            compressedSize = deflater.deflate(compressedData);
        }
        finally {
            deflater.end();
        }
        output.writeInt(compressedSize);
        output.writeBytes(compressedData, 0, compressedSize);
    }
}

