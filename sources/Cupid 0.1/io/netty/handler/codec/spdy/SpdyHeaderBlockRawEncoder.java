package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Set;

public class SpdyHeaderBlockRawEncoder extends SpdyHeaderBlockEncoder {
  private final int version;
  
  public SpdyHeaderBlockRawEncoder(SpdyVersion version) {
    if (version == null)
      throw new NullPointerException("version"); 
    this.version = version.getVersion();
  }
  
  private static void setLengthField(ByteBuf buffer, int writerIndex, int length) {
    buffer.setInt(writerIndex, length);
  }
  
  private static void writeLengthField(ByteBuf buffer, int length) {
    buffer.writeInt(length);
  }
  
  public ByteBuf encode(SpdyHeadersFrame frame) throws Exception {
    Set<String> names = frame.headers().names();
    int numHeaders = names.size();
    if (numHeaders == 0)
      return Unpooled.EMPTY_BUFFER; 
    if (numHeaders > 65535)
      throw new IllegalArgumentException("header block contains too many headers"); 
    ByteBuf headerBlock = Unpooled.buffer();
    writeLengthField(headerBlock, numHeaders);
    for (String name : names) {
      byte[] nameBytes = name.getBytes("UTF-8");
      writeLengthField(headerBlock, nameBytes.length);
      headerBlock.writeBytes(nameBytes);
      int savedIndex = headerBlock.writerIndex();
      int valueLength = 0;
      writeLengthField(headerBlock, valueLength);
      for (String value : frame.headers().getAll(name)) {
        byte[] valueBytes = value.getBytes("UTF-8");
        if (valueBytes.length > 0) {
          headerBlock.writeBytes(valueBytes);
          headerBlock.writeByte(0);
          valueLength += valueBytes.length + 1;
        } 
      } 
      if (valueLength != 0)
        valueLength--; 
      if (valueLength > 65535)
        throw new IllegalArgumentException("header exceeds allowable length: " + name); 
      if (valueLength > 0) {
        setLengthField(headerBlock, savedIndex, valueLength);
        headerBlock.writerIndex(headerBlock.writerIndex() - 1);
      } 
    } 
    return headerBlock;
  }
  
  void end() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockRawEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */