package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class JZlibDecoder extends ZlibDecoder {
  private final Inflater z = new Inflater();
  
  private byte[] dictionary;
  
  private volatile boolean finished;
  
  public JZlibDecoder() {
    this(ZlibWrapper.ZLIB);
  }
  
  public JZlibDecoder(ZlibWrapper wrapper) {
    if (wrapper == null)
      throw new NullPointerException("wrapper"); 
    int resultCode = this.z.init(ZlibUtil.convertWrapperType(wrapper));
    if (resultCode != 0)
      ZlibUtil.fail(this.z, "initialization failure", resultCode); 
  }
  
  public JZlibDecoder(byte[] dictionary) {
    if (dictionary == null)
      throw new NullPointerException("dictionary"); 
    this.dictionary = dictionary;
    int resultCode = this.z.inflateInit(JZlib.W_ZLIB);
    if (resultCode != 0)
      ZlibUtil.fail(this.z, "initialization failure", resultCode); 
  }
  
  public boolean isClosed() {
    return this.finished;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (this.finished) {
      in.skipBytes(in.readableBytes());
      return;
    } 
    if (!in.isReadable())
      return; 
    try {
      int inputLength = in.readableBytes();
      this.z.avail_in = inputLength;
      if (in.hasArray()) {
        this.z.next_in = in.array();
        this.z.next_in_index = in.arrayOffset() + in.readerIndex();
      } else {
        byte[] array = new byte[inputLength];
        in.getBytes(in.readerIndex(), array);
        this.z.next_in = array;
        this.z.next_in_index = 0;
      } 
      int oldNextInIndex = this.z.next_in_index;
      int maxOutputLength = inputLength << 1;
      ByteBuf decompressed = ctx.alloc().heapBuffer(maxOutputLength);
      try {
        while (true) {
          this.z.avail_out = maxOutputLength;
          decompressed.ensureWritable(maxOutputLength);
          this.z.next_out = decompressed.array();
          this.z.next_out_index = decompressed.arrayOffset() + decompressed.writerIndex();
          int oldNextOutIndex = this.z.next_out_index;
          int resultCode = this.z.inflate(2);
          int outputLength = this.z.next_out_index - oldNextOutIndex;
          if (outputLength > 0)
            decompressed.writerIndex(decompressed.writerIndex() + outputLength); 
          switch (resultCode) {
            case 2:
              if (this.dictionary == null) {
                ZlibUtil.fail(this.z, "decompression failure", resultCode);
                continue;
              } 
              resultCode = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
              if (resultCode != 0)
                ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode); 
              continue;
            case 1:
              this.finished = true;
              this.z.inflateEnd();
              break;
            case 0:
              continue;
            case -5:
              if (this.z.avail_in <= 0)
                break; 
              continue;
          } 
          ZlibUtil.fail(this.z, "decompression failure", resultCode);
        } 
      } finally {
        in.skipBytes(this.z.next_in_index - oldNextInIndex);
        if (decompressed.isReadable()) {
          out.add(decompressed);
        } else {
          decompressed.release();
        } 
      } 
    } finally {
      this.z.next_in = null;
      this.z.next_out = null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\compression\JZlibDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */