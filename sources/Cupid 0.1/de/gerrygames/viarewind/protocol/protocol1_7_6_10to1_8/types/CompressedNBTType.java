package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.opennbt.NBTIO;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class CompressedNBTType extends Type<CompoundTag> {
  public CompressedNBTType() {
    super(CompoundTag.class);
  }
  
  public CompoundTag read(ByteBuf buffer) {
    short length = buffer.readShort();
    if (length <= 0)
      return null; 
    byte[] compressed = new byte[length];
    buffer.readBytes(compressed);
    byte[] uncompressed = decompress(compressed);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uncompressed);
    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
    try {
      return (CompoundTag)NBTIO.readTag(dataInputStream);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      try {
        dataInputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      } 
    } 
    return null;
  }
  
  public void write(ByteBuf buffer, CompoundTag nbt) throws Exception {
    if (nbt == null) {
      buffer.writeShort(-1);
    } else {
      ByteBuf buf = buffer.alloc().buffer();
      ByteBufOutputStream bytebufStream = new ByteBufOutputStream(buf);
      DataOutputStream dataOutputStream = new DataOutputStream((OutputStream)bytebufStream);
      NBTIO.writeTag(dataOutputStream, (Tag)nbt);
      dataOutputStream.close();
      byte[] uncompressed = new byte[buf.readableBytes()];
      buf.readBytes(uncompressed);
      buf.release();
      byte[] compressed = compress(uncompressed);
      buffer.writeShort(compressed.length);
      buffer.writeBytes(compressed);
    } 
  }
  
  public static byte[] compress(byte[] content) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
      gzipOutputStream.write(content);
      gzipOutputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
    return byteArrayOutputStream.toByteArray();
  }
  
  public static byte[] decompress(byte[] contentBytes) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(contentBytes));
      while (stream.available() > 0)
        out.write(stream.read()); 
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
    return out.toByteArray();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\types\CompressedNBTType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */