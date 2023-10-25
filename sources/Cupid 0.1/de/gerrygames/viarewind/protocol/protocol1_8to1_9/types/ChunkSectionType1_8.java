package de.gerrygames.viarewind.protocol.protocol1_8to1_9.types;

import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.Type;

public class ChunkSectionType1_8 extends Type<ChunkSection> {
  public ChunkSectionType1_8() {
    super("Chunk Section Type", ChunkSection.class);
  }
  
  public ChunkSection read(ByteBuf buffer) throws Exception {
    ChunkSection chunkSection = new ChunkSection();
    byte[] blockData = new byte[8192];
    buffer.readBytes(blockData);
    ShortBuffer blockBuf = ByteBuffer.wrap(blockData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
    for (int i = 0; i < 4096; i++) {
      int mask = blockBuf.get();
      int type = mask >> 4;
      int data = mask & 0xF;
      chunkSection.setBlock(i, type, data);
    } 
    return chunkSection;
  }
  
  public void write(ByteBuf buffer, ChunkSection chunkSection) throws Exception {
    for (int y = 0; y < 16; y++) {
      for (int z = 0; z < 16; z++) {
        for (int x = 0; x < 16; x++) {
          int block = chunkSection.getFlatBlock(x, y, z);
          buffer.writeByte(block);
          buffer.writeByte(block >> 8);
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\types\ChunkSectionType1_8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */