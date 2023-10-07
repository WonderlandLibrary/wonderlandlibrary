// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

public class ChunkPacketTransformer
{
    private static byte[] transformChunkData(final byte[] data, final int primaryBitMask, final boolean skyLight, final boolean groundUp) throws Exception {
        final ByteBuf inputData = Unpooled.wrappedBuffer(data);
        final ByteBuf finalBuf = ByteBufAllocator.DEFAULT.buffer();
        try {
            final ChunkSection[] sections = new ChunkSection[16];
            for (int i = 0; i < 16; ++i) {
                if ((primaryBitMask & 1 << i) != 0x0) {
                    sections[i] = Types1_8.CHUNK_SECTION.read(inputData);
                }
            }
            for (int i = 0; i < 16; ++i) {
                if ((primaryBitMask & 1 << i) != 0x0) {
                    final ChunkSection section = sections[i];
                    for (int k = 0; k < section.getPaletteSize(); ++k) {
                        int blockData = section.getPaletteEntry(k);
                        blockData = ReplacementRegistry1_7_6_10to1_8.replace(blockData);
                        section.setPaletteEntry(k, blockData);
                    }
                }
            }
            for (int i = 0; i < 16; ++i) {
                if ((primaryBitMask & 1 << i) != 0x0) {
                    final ChunkSection section = sections[i];
                    for (int j = 0; j < 4096; ++j) {
                        final int raw = section.getFlatBlock(j);
                        finalBuf.writeByte(raw >> 4);
                    }
                }
            }
            for (int i = 0; i < 16; ++i) {
                if ((primaryBitMask & 1 << i) != 0x0) {
                    final ChunkSection section = sections[i];
                    for (int j = 0; j < 4096; j += 2) {
                        final int meta0 = section.getFlatBlock(j) & 0xF;
                        final int meta2 = section.getFlatBlock(j + 1) & 0xF;
                        finalBuf.writeByte(meta2 << 4 | meta0);
                    }
                }
            }
            final int columnCount = Integer.bitCount(primaryBitMask);
            finalBuf.writeBytes(inputData, 2048 * columnCount);
            if (skyLight) {
                finalBuf.writeBytes(inputData, 2048 * columnCount);
            }
            if (groundUp && inputData.isReadable(256)) {
                finalBuf.writeBytes(inputData, 256);
            }
            return Type.REMAINING_BYTES.read(finalBuf);
        }
        finally {
            finalBuf.release();
        }
    }
    
    private static int calcSize(final int i, final boolean hasSkyLight, final boolean hasBiome) {
        final int blocks = i * 2 * 16 * 16 * 16;
        final int blockLight = i * 16 * 16 * 16 / 2;
        final int skyLight = hasSkyLight ? (i * 16 * 16 * 16 / 2) : 0;
        final int biome = hasBiome ? 256 : 0;
        return blocks + blockLight + skyLight + biome;
    }
    
    public static void transformChunkBulk(final PacketWrapper packetWrapper) throws Exception {
        final boolean skyLightSent = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
        final int columnCount = packetWrapper.read((Type<Integer>)Type.VAR_INT);
        final int[] chunkX = new int[columnCount];
        final int[] chunkZ = new int[columnCount];
        final int[] primaryBitMask = new int[columnCount];
        final byte[][] data = new byte[columnCount][];
        for (int i = 0; i < columnCount; ++i) {
            chunkX[i] = packetWrapper.read((Type<Integer>)Type.INT);
            chunkZ[i] = packetWrapper.read((Type<Integer>)Type.INT);
            primaryBitMask[i] = packetWrapper.read((Type<Integer>)Type.UNSIGNED_SHORT);
        }
        for (int i = 0; i < columnCount; ++i) {
            final int size = calcSize(Integer.bitCount(primaryBitMask[i]), skyLightSent, true);
            final CustomByteType customByteType = new CustomByteType(size);
            data[i] = transformChunkData(packetWrapper.read((Type<byte[]>)customByteType), primaryBitMask[i], skyLightSent, true);
        }
        final ByteArrayOutputStream compressedData = new ByteArrayOutputStream();
        try (final DeflaterOutputStream deflaterStream = new DeflaterOutputStream(compressedData)) {
            for (int j = 0; j < columnCount; ++j) {
                deflaterStream.write(data[j]);
            }
        }
        packetWrapper.write(Type.SHORT, (short)columnCount);
        packetWrapper.write(Type.INT, compressedData.size());
        packetWrapper.write(Type.BOOLEAN, skyLightSent);
        final CustomByteType customByteType2 = new CustomByteType(compressedData.size());
        packetWrapper.write((Type<byte[]>)customByteType2, compressedData.toByteArray());
        for (int j = 0; j < columnCount; ++j) {
            packetWrapper.write(Type.INT, chunkX[j]);
            packetWrapper.write(Type.INT, chunkZ[j]);
            packetWrapper.write(Type.SHORT, (short)primaryBitMask[j]);
            packetWrapper.write(Type.SHORT, (Short)0);
        }
    }
}
