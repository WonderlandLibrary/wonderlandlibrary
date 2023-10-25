package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import java.util.zip.Deflater;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.PartialType;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

public class Chunk1_7_10Type extends PartialType<Chunk, ClientWorld> {
  public Chunk1_7_10Type(ClientWorld param) {
    super(param, Chunk.class);
  }
  
  public Chunk read(ByteBuf byteBuf, ClientWorld clientWorld) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  public void write(ByteBuf output, ClientWorld clientWorld, Chunk chunk) throws Exception {
    byte[] compressedData;
    int compressedSize;
    output.writeInt(chunk.getX());
    output.writeInt(chunk.getZ());
    output.writeBoolean(chunk.isFullChunk());
    output.writeShort(chunk.getBitmask());
    output.writeShort(0);
    ByteBuf dataToCompress = output.alloc().buffer();
    ByteBuf blockData = output.alloc().buffer();
    int i;
    for (i = 0; i < (chunk.getSections()).length; i++) {
      if ((chunk.getBitmask() & 1 << i) != 0) {
        ChunkSection section = chunk.getSections()[i];
        for (int y = 0; y < 16; y++) {
          for (int z = 0; z < 16; z++) {
            int previousData = 0;
            for (int x = 0; x < 16; x++) {
              int block = section.getFlatBlock(x, y, z);
              dataToCompress.writeByte(block >> 4);
              int j = block & 0xF;
              if (x % 2 == 0) {
                previousData = j;
              } else {
                blockData.writeByte(j << 4 | previousData);
              } 
            } 
          } 
        } 
      } 
    } 
    dataToCompress.writeBytes(blockData);
    blockData.release();
    for (i = 0; i < (chunk.getSections()).length; i++) {
      if ((chunk.getBitmask() & 1 << i) != 0)
        chunk.getSections()[i].writeBlockLight(dataToCompress); 
    } 
    boolean skyLight = (clientWorld != null && clientWorld.getEnvironment() == Environment.NORMAL);
    if (skyLight)
      for (int j = 0; j < (chunk.getSections()).length; j++) {
        if ((chunk.getBitmask() & 1 << j) != 0)
          chunk.getSections()[j].writeSkyLight(dataToCompress); 
      }  
    if (chunk.isFullChunk() && chunk.isBiomeData())
      for (int biome : chunk.getBiomeData())
        dataToCompress.writeByte((byte)biome);  
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
    } finally {
      deflater.end();
    } 
    output.writeInt(compressedSize);
    output.writeBytes(compressedData, 0, compressedSize);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\Chunk1_7_10Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */