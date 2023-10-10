// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import java.util.zip.Deflater;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.replacement.Replacement;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.storage.BlockState;
import io.netty.buffer.Unpooled;

public class ChunkPacketTransformer
{
    private static byte[] transformChunkData(byte[] data, final int primaryBitMask, final boolean skyLight, final boolean groundUp) {
        int dataSize = 0;
        final ByteBuf buf = Unpooled.buffer();
        final ByteBuf blockDataBuf = Unpooled.buffer();
        for (int i = 0; i < 16; ++i) {
            if ((primaryBitMask & 1 << i) != 0x0) {
                byte tmp = 0;
                for (int j = 0; j < 4096; ++j) {
                    final short blockData = (short)((data[dataSize + 1] & 0xFF) << 8 | (data[dataSize] & 0xFF));
                    dataSize += 2;
                    int id = BlockState.extractId(blockData);
                    int meta = BlockState.extractData(blockData);
                    final Replacement replace = ReplacementRegistry1_7_6_10to1_8.getReplacement(id, meta);
                    if (replace != null) {
                        id = replace.getId();
                        meta = replace.replaceData(meta);
                    }
                    buf.writeByte(id);
                    if (j % 2 == 0) {
                        tmp = (byte)(meta & 0xF);
                    }
                    else {
                        blockDataBuf.writeByte(tmp | (meta & 0xF) << 4);
                    }
                }
            }
        }
        buf.writeBytes(blockDataBuf);
        blockDataBuf.release();
        final int columnCount = Integer.bitCount(primaryBitMask);
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
    
    private static int calcSize(final int i, final boolean flag, final boolean flag1) {
        final int j = i * 2 * 16 * 16 * 16;
        final int k = i * 16 * 16 * 16 / 2;
        final int l = flag ? (i * 16 * 16 * 16 / 2) : 0;
        final int i2 = flag1 ? 256 : 0;
        return j + k + l + i2;
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
        int totalSize = 0;
        for (int j = 0; j < columnCount; ++j) {
            final int size = calcSize(Integer.bitCount(primaryBitMask[j]), skyLightSent, true);
            final CustomByteType customByteType = new CustomByteType(size);
            data[j] = transformChunkData(packetWrapper.read((Type<byte[]>)customByteType), primaryBitMask[j], skyLightSent, true);
            totalSize += data[j].length;
        }
        packetWrapper.write(Type.SHORT, (short)columnCount);
        final byte[] buildBuffer = new byte[totalSize];
        int bufferLocation = 0;
        for (int k = 0; k < columnCount; ++k) {
            System.arraycopy(data[k], 0, buildBuffer, bufferLocation, data[k].length);
            bufferLocation += data[k].length;
        }
        final Deflater deflater = new Deflater(4);
        deflater.reset();
        deflater.setInput(buildBuffer);
        deflater.finish();
        final byte[] buffer = new byte[buildBuffer.length + 100];
        final int compressedSize = deflater.deflate(buffer);
        final byte[] finalBuffer = new byte[compressedSize];
        System.arraycopy(buffer, 0, finalBuffer, 0, compressedSize);
        packetWrapper.write(Type.INT, compressedSize);
        packetWrapper.write(Type.BOOLEAN, skyLightSent);
        final CustomByteType customByteType2 = new CustomByteType(compressedSize);
        packetWrapper.write((Type<byte[]>)customByteType2, finalBuffer);
        for (int l = 0; l < columnCount; ++l) {
            packetWrapper.write(Type.INT, chunkX[l]);
            packetWrapper.write(Type.INT, chunkZ[l]);
            packetWrapper.write(Type.SHORT, (short)primaryBitMask[l]);
            packetWrapper.write(Type.SHORT, (Short)0);
        }
    }
}
