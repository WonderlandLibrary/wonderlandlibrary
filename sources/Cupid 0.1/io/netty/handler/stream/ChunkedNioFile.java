package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ChunkedNioFile implements ChunkedInput<ByteBuf> {
  private final FileChannel in;
  
  private final long startOffset;
  
  private final long endOffset;
  
  private final int chunkSize;
  
  private long offset;
  
  public ChunkedNioFile(File in) throws IOException {
    this((new FileInputStream(in)).getChannel());
  }
  
  public ChunkedNioFile(File in, int chunkSize) throws IOException {
    this((new FileInputStream(in)).getChannel(), chunkSize);
  }
  
  public ChunkedNioFile(FileChannel in) throws IOException {
    this(in, 8192);
  }
  
  public ChunkedNioFile(FileChannel in, int chunkSize) throws IOException {
    this(in, 0L, in.size(), chunkSize);
  }
  
  public ChunkedNioFile(FileChannel in, long offset, long length, int chunkSize) throws IOException {
    if (in == null)
      throw new NullPointerException("in"); 
    if (offset < 0L)
      throw new IllegalArgumentException("offset: " + offset + " (expected: 0 or greater)"); 
    if (length < 0L)
      throw new IllegalArgumentException("length: " + length + " (expected: 0 or greater)"); 
    if (chunkSize <= 0)
      throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)"); 
    if (offset != 0L)
      in.position(offset); 
    this.in = in;
    this.chunkSize = chunkSize;
    this.offset = this.startOffset = offset;
    this.endOffset = offset + length;
  }
  
  public long startOffset() {
    return this.startOffset;
  }
  
  public long endOffset() {
    return this.endOffset;
  }
  
  public long currentOffset() {
    return this.offset;
  }
  
  public boolean isEndOfInput() throws Exception {
    return (this.offset >= this.endOffset || !this.in.isOpen());
  }
  
  public void close() throws Exception {
    this.in.close();
  }
  
  public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
    long offset = this.offset;
    if (offset >= this.endOffset)
      return null; 
    int chunkSize = (int)Math.min(this.chunkSize, this.endOffset - offset);
    ByteBuf buffer = ctx.alloc().buffer(chunkSize);
    boolean release = true;
    try {
      int readBytes = 0;
      do {
        int localReadBytes = buffer.writeBytes(this.in, chunkSize - readBytes);
        if (localReadBytes < 0)
          break; 
        readBytes += localReadBytes;
      } while (readBytes != chunkSize);
      this.offset += readBytes;
      release = false;
      return buffer;
    } finally {
      if (release)
        buffer.release(); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\stream\ChunkedNioFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */