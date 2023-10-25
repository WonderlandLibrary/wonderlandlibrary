package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.storage.BlockState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.zip.Deflater;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.CustomByteType;

public class ChunkPacketTransformer {
  private static byte[] transformChunkData(byte[] data, int primaryBitMask, boolean skyLight, boolean groundUp) {
    int dataSize = 0;
    ByteBuf buf = Unpooled.buffer();
    ByteBuf blockDataBuf = Unpooled.buffer();
    for (int i = 0; i < 16; i++) {
      if ((primaryBitMask & 1 << i) != 0) {
        byte tmp = 0;
        for (int j = 0; j < 4096; j++) {
          short blockData = (short)((data[dataSize + 1] & 0xFF) << 8 | data[dataSize] & 0xFF);
          dataSize += 2;
          BlockState state = BlockState.rawToState(blockData);
          state = ReplacementRegistry1_7_6_10to1_8.replace(state);
          buf.writeByte(state.getId());
          if (j % 2 == 0) {
            tmp = (byte)(state.getData() & 0xF);
          } else {
            blockDataBuf.writeByte(tmp | (state.getData() & 0xF) << 4);
          } 
        } 
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
  
  private static int calcSize(int i, boolean flag, boolean flag1) {
    int j = i * 2 * 16 * 16 * 16;
    int k = i * 16 * 16 * 16 / 2;
    int l = flag ? (i * 16 * 16 * 16 / 2) : 0;
    int i1 = flag1 ? 256 : 0;
    return j + k + l + i1;
  }
  
  public static void transformChunkBulk(PacketWrapper packetWrapper) throws Exception {
    boolean skyLightSent = ((Boolean)packetWrapper.read(Type.BOOLEAN)).booleanValue();
    int columnCount = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
    int[] chunkX = new int[columnCount];
    int[] chunkZ = new int[columnCount];
    int[] primaryBitMask = new int[columnCount];
    byte[][] data = new byte[columnCount][];
    for (int i = 0; i < columnCount; i++) {
      chunkX[i] = ((Integer)packetWrapper.read(Type.INT)).intValue();
      chunkZ[i] = ((Integer)packetWrapper.read(Type.INT)).intValue();
      primaryBitMask[i] = ((Integer)packetWrapper.read(Type.UNSIGNED_SHORT)).intValue();
    } 
    int totalSize = 0;
    for (int j = 0; j < columnCount; j++) {
      int size = calcSize(Integer.bitCount(primaryBitMask[j]), skyLightSent, true);
      CustomByteType customByteType1 = new CustomByteType(Integer.valueOf(size));
      data[j] = transformChunkData((byte[])packetWrapper.read((Type)customByteType1), primaryBitMask[j], skyLightSent, true);
      totalSize += (data[j]).length;
    } 
    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)columnCount));
    byte[] buildBuffer = new byte[totalSize];
    int bufferLocation = 0;
    for (int k = 0; k < columnCount; k++) {
      System.arraycopy(data[k], 0, buildBuffer, bufferLocation, (data[k]).length);
      bufferLocation += (data[k]).length;
    } 
    Deflater deflater = new Deflater(4);
    deflater.reset();
    deflater.setInput(buildBuffer);
    deflater.finish();
    byte[] buffer = new byte[buildBuffer.length + 100];
    int compressedSize = deflater.deflate(buffer);
    byte[] finalBuffer = new byte[compressedSize];
    System.arraycopy(buffer, 0, finalBuffer, 0, compressedSize);
    packetWrapper.write(Type.INT, Integer.valueOf(compressedSize));
    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(skyLightSent));
    CustomByteType customByteType = new CustomByteType(Integer.valueOf(compressedSize));
    packetWrapper.write((Type)customByteType, finalBuffer);
    for (int m = 0; m < columnCount; m++) {
      packetWrapper.write(Type.INT, Integer.valueOf(chunkX[m]));
      packetWrapper.write(Type.INT, Integer.valueOf(chunkZ[m]));
      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)primaryBitMask[m]));
      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\chunks\ChunkPacketTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */