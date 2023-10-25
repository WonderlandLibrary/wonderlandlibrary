package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class AbstractMemoryHttpData extends AbstractHttpData {
  private ByteBuf byteBuf;
  
  private int chunkPosition;
  
  protected boolean isRenamed;
  
  protected AbstractMemoryHttpData(String name, Charset charset, long size) {
    super(name, charset, size);
  }
  
  public void setContent(ByteBuf buffer) throws IOException {
    if (buffer == null)
      throw new NullPointerException("buffer"); 
    long localsize = buffer.readableBytes();
    if (this.definedSize > 0L && this.definedSize < localsize)
      throw new IOException("Out of size: " + localsize + " > " + this.definedSize); 
    if (this.byteBuf != null)
      this.byteBuf.release(); 
    this.byteBuf = buffer;
    this.size = localsize;
    this.completed = true;
  }
  
  public void setContent(InputStream inputStream) throws IOException {
    if (inputStream == null)
      throw new NullPointerException("inputStream"); 
    ByteBuf buffer = Unpooled.buffer();
    byte[] bytes = new byte[16384];
    int read = inputStream.read(bytes);
    int written = 0;
    while (read > 0) {
      buffer.writeBytes(bytes, 0, read);
      written += read;
      read = inputStream.read(bytes);
    } 
    this.size = written;
    if (this.definedSize > 0L && this.definedSize < this.size)
      throw new IOException("Out of size: " + this.size + " > " + this.definedSize); 
    if (this.byteBuf != null)
      this.byteBuf.release(); 
    this.byteBuf = buffer;
    this.completed = true;
  }
  
  public void addContent(ByteBuf buffer, boolean last) throws IOException {
    if (buffer != null) {
      long localsize = buffer.readableBytes();
      if (this.definedSize > 0L && this.definedSize < this.size + localsize)
        throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize); 
      this.size += localsize;
      if (this.byteBuf == null) {
        this.byteBuf = buffer;
      } else if (this.byteBuf instanceof CompositeByteBuf) {
        CompositeByteBuf cbb = (CompositeByteBuf)this.byteBuf;
        cbb.addComponent(buffer);
        cbb.writerIndex(cbb.writerIndex() + buffer.readableBytes());
      } else {
        CompositeByteBuf cbb = Unpooled.compositeBuffer(2147483647);
        cbb.addComponents(new ByteBuf[] { this.byteBuf, buffer });
        cbb.writerIndex(this.byteBuf.readableBytes() + buffer.readableBytes());
        this.byteBuf = (ByteBuf)cbb;
      } 
    } 
    if (last) {
      this.completed = true;
    } else if (buffer == null) {
      throw new NullPointerException("buffer");
    } 
  }
  
  public void setContent(File file) throws IOException {
    if (file == null)
      throw new NullPointerException("file"); 
    long newsize = file.length();
    if (newsize > 2147483647L)
      throw new IllegalArgumentException("File too big to be loaded in memory"); 
    FileInputStream inputStream = new FileInputStream(file);
    FileChannel fileChannel = inputStream.getChannel();
    byte[] array = new byte[(int)newsize];
    ByteBuffer byteBuffer = ByteBuffer.wrap(array);
    int read = 0;
    while (read < newsize)
      read += fileChannel.read(byteBuffer); 
    fileChannel.close();
    inputStream.close();
    byteBuffer.flip();
    if (this.byteBuf != null)
      this.byteBuf.release(); 
    this.byteBuf = Unpooled.wrappedBuffer(2147483647, new ByteBuffer[] { byteBuffer });
    this.size = newsize;
    this.completed = true;
  }
  
  public void delete() {
    if (this.byteBuf != null) {
      this.byteBuf.release();
      this.byteBuf = null;
    } 
  }
  
  public byte[] get() {
    if (this.byteBuf == null)
      return Unpooled.EMPTY_BUFFER.array(); 
    byte[] array = new byte[this.byteBuf.readableBytes()];
    this.byteBuf.getBytes(this.byteBuf.readerIndex(), array);
    return array;
  }
  
  public String getString() {
    return getString(HttpConstants.DEFAULT_CHARSET);
  }
  
  public String getString(Charset encoding) {
    if (this.byteBuf == null)
      return ""; 
    if (encoding == null)
      encoding = HttpConstants.DEFAULT_CHARSET; 
    return this.byteBuf.toString(encoding);
  }
  
  public ByteBuf getByteBuf() {
    return this.byteBuf;
  }
  
  public ByteBuf getChunk(int length) throws IOException {
    if (this.byteBuf == null || length == 0 || this.byteBuf.readableBytes() == 0) {
      this.chunkPosition = 0;
      return Unpooled.EMPTY_BUFFER;
    } 
    int sizeLeft = this.byteBuf.readableBytes() - this.chunkPosition;
    if (sizeLeft == 0) {
      this.chunkPosition = 0;
      return Unpooled.EMPTY_BUFFER;
    } 
    int sliceLength = length;
    if (sizeLeft < length)
      sliceLength = sizeLeft; 
    ByteBuf chunk = this.byteBuf.slice(this.chunkPosition, sliceLength).retain();
    this.chunkPosition += sliceLength;
    return chunk;
  }
  
  public boolean isInMemory() {
    return true;
  }
  
  public boolean renameTo(File dest) throws IOException {
    if (dest == null)
      throw new NullPointerException("dest"); 
    if (this.byteBuf == null) {
      dest.createNewFile();
      this.isRenamed = true;
      return true;
    } 
    int length = this.byteBuf.readableBytes();
    FileOutputStream outputStream = new FileOutputStream(dest);
    FileChannel fileChannel = outputStream.getChannel();
    int written = 0;
    if (this.byteBuf.nioBufferCount() == 1) {
      ByteBuffer byteBuffer = this.byteBuf.nioBuffer();
      while (written < length)
        written += fileChannel.write(byteBuffer); 
    } else {
      ByteBuffer[] byteBuffers = this.byteBuf.nioBuffers();
      while (written < length)
        written = (int)(written + fileChannel.write(byteBuffers)); 
    } 
    fileChannel.force(false);
    fileChannel.close();
    outputStream.close();
    this.isRenamed = true;
    return (written == length);
  }
  
  public File getFile() throws IOException {
    throw new IOException("Not represented by a file");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\AbstractMemoryHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */