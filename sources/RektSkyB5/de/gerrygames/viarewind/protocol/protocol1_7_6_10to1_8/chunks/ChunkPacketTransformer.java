/*
 * Decompiled with CFR 0.152.
 */
package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.replacement.Replacement;
import de.gerrygames.viarewind.storage.BlockState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.zip.Deflater;

public class ChunkPacketTransformer {
    private static byte[] transformChunkData(byte[] data, int primaryBitMask, boolean skyLight, boolean groundUp) {
        int dataSize = 0;
        ByteBuf buf = Unpooled.buffer();
        ByteBuf blockDataBuf = Unpooled.buffer();
        for (int i2 = 0; i2 < 16; ++i2) {
            if ((primaryBitMask & 1 << i2) == 0) continue;
            byte tmp = 0;
            for (int j2 = 0; j2 < 4096; ++j2) {
                int meta;
                short blockData = (short)((data[dataSize + 1] & 0xFF) << 8 | data[dataSize] & 0xFF);
                dataSize += 2;
                int id = BlockState.extractId(blockData);
                Replacement replace = ReplacementRegistry1_7_6_10to1_8.getReplacement(id, meta = BlockState.extractData(blockData));
                if (replace != null) {
                    id = replace.getId();
                    meta = replace.replaceData(meta);
                }
                buf.writeByte(id);
                if (j2 % 2 == 0) {
                    tmp = (byte)(meta & 0xF);
                    continue;
                }
                blockDataBuf.writeByte(tmp | (meta & 0xF) << 4);
            }
        }
        buf.writeBytes(blockDataBuf);
        blockDataBuf.release();
        int columnCount = Integer.bitCount(primaryBitMask);
        buf.writeBytes(data, dataSize, 2048 * columnCount);
        dataSize += 2048 * columnCount;
        if (skyLight) {
            buf.writeBytes(data, dataSize, 2048 * columnCount);
            dataSize += 2048 * columnCount;
        }
        if (groundUp && dataSize + 256 <= data.length) {
            buf.writeBytes(data, dataSize, 256);
            dataSize += 256;
        }
        data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        return data;
    }

    private static int calcSize(int i2, boolean flag, boolean flag1) {
        int j2 = i2 * 2 * 16 * 16 * 16;
        int k2 = i2 * 16 * 16 * 16 / 2;
        int l2 = flag ? i2 * 16 * 16 * 16 / 2 : 0;
        int i1 = flag1 ? 256 : 0;
        return j2 + k2 + l2 + i1;
    }

    public static void transformChunkBulk(PacketWrapper packetWrapper) throws Exception {
        boolean skyLightSent = packetWrapper.read(Type.BOOLEAN);
        int columnCount = packetWrapper.read(Type.VAR_INT);
        int[] chunkX = new int[columnCount];
        int[] chunkZ = new int[columnCount];
        int[] primaryBitMask = new int[columnCount];
        byte[][] data = new byte[columnCount][];
        for (int i2 = 0; i2 < columnCount; ++i2) {
            chunkX[i2] = packetWrapper.read(Type.INT);
            chunkZ[i2] = packetWrapper.read(Type.INT);
            primaryBitMask[i2] = packetWrapper.read(Type.UNSIGNED_SHORT);
        }
        int totalSize = 0;
        for (int i3 = 0; i3 < columnCount; ++i3) {
            int size = ChunkPacketTransformer.calcSize(Integer.bitCount(primaryBitMask[i3]), skyLightSent, true);
            CustomByteType customByteType = new CustomByteType(size);
            data[i3] = ChunkPacketTransformer.transformChunkData(packetWrapper.read(customByteType), primaryBitMask[i3], skyLightSent, true);
            totalSize += data[i3].length;
        }
        packetWrapper.write(Type.SHORT, (short)columnCount);
        byte[] buildBuffer = new byte[totalSize];
        int bufferLocation = 0;
        for (int i4 = 0; i4 < columnCount; ++i4) {
            System.arraycopy(data[i4], 0, buildBuffer, bufferLocation, data[i4].length);
            bufferLocation += data[i4].length;
        }
        Deflater deflater = new Deflater(4);
        deflater.reset();
        deflater.setInput(buildBuffer);
        deflater.finish();
        byte[] buffer = new byte[buildBuffer.length + 100];
        int compressedSize = deflater.deflate(buffer);
        byte[] finalBuffer = new byte[compressedSize];
        System.arraycopy(buffer, 0, finalBuffer, 0, compressedSize);
        packetWrapper.write(Type.INT, compressedSize);
        packetWrapper.write(Type.BOOLEAN, skyLightSent);
        CustomByteType customByteType = new CustomByteType(compressedSize);
        packetWrapper.write(customByteType, finalBuffer);
        for (int i5 = 0; i5 < columnCount; ++i5) {
            packetWrapper.write(Type.INT, chunkX[i5]);
            packetWrapper.write(Type.INT, chunkZ[i5]);
            packetWrapper.write(Type.SHORT, (short)primaryBitMask[i5]);
            packetWrapper.write(Type.SHORT, (short)0);
        }
    }
}

