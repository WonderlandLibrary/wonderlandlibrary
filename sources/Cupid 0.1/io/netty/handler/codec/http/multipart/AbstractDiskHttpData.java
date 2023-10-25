package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class AbstractDiskHttpData extends AbstractHttpData {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
  
  protected File file;
  
  private boolean isRenamed;
  
  private FileChannel fileChannel;
  
  protected AbstractDiskHttpData(String name, Charset charset, long size) {
    super(name, charset, size);
  }
  
  protected abstract String getDiskFilename();
  
  protected abstract String getPrefix();
  
  protected abstract String getBaseDirectory();
  
  protected abstract String getPostfix();
  
  protected abstract boolean deleteOnExit();
  
  private File tempFile() throws IOException {
    String newpostfix;
    File tmpFile;
    String diskFilename = getDiskFilename();
    if (diskFilename != null) {
      newpostfix = '_' + diskFilename;
    } else {
      newpostfix = getPostfix();
    } 
    if (getBaseDirectory() == null) {
      tmpFile = File.createTempFile(getPrefix(), newpostfix);
    } else {
      tmpFile = File.createTempFile(getPrefix(), newpostfix, new File(getBaseDirectory()));
    } 
    if (deleteOnExit())
      tmpFile.deleteOnExit(); 
    return tmpFile;
  }
  
  public void setContent(ByteBuf buffer) throws IOException {
    if (buffer == null)
      throw new NullPointerException("buffer"); 
    try {
      this.size = buffer.readableBytes();
      if (this.definedSize > 0L && this.definedSize < this.size)
        throw new IOException("Out of size: " + this.size + " > " + this.definedSize); 
      if (this.file == null)
        this.file = tempFile(); 
      if (buffer.readableBytes() == 0) {
        this.file.createNewFile();
        return;
      } 
      FileOutputStream outputStream = new FileOutputStream(this.file);
      FileChannel localfileChannel = outputStream.getChannel();
      ByteBuffer byteBuffer = buffer.nioBuffer();
      int written = 0;
      while (written < this.size)
        written += localfileChannel.write(byteBuffer); 
      buffer.readerIndex(buffer.readerIndex() + written);
      localfileChannel.force(false);
      localfileChannel.close();
      outputStream.close();
      this.completed = true;
    } finally {
      buffer.release();
    } 
  }
  
  public void addContent(ByteBuf buffer, boolean last) throws IOException {
    if (buffer != null)
      try {
        int localsize = buffer.readableBytes();
        if (this.definedSize > 0L && this.definedSize < this.size + localsize)
          throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize); 
        ByteBuffer byteBuffer = (buffer.nioBufferCount() == 1) ? buffer.nioBuffer() : buffer.copy().nioBuffer();
        int written = 0;
        if (this.file == null)
          this.file = tempFile(); 
        if (this.fileChannel == null) {
          FileOutputStream outputStream = new FileOutputStream(this.file);
          this.fileChannel = outputStream.getChannel();
        } 
        while (written < localsize)
          written += this.fileChannel.write(byteBuffer); 
        this.size += localsize;
        buffer.readerIndex(buffer.readerIndex() + written);
      } finally {
        buffer.release();
      }  
    if (last) {
      if (this.file == null)
        this.file = tempFile(); 
      if (this.fileChannel == null) {
        FileOutputStream outputStream = new FileOutputStream(this.file);
        this.fileChannel = outputStream.getChannel();
      } 
      this.fileChannel.force(false);
      this.fileChannel.close();
      this.fileChannel = null;
      this.completed = true;
    } else if (buffer == null) {
      throw new NullPointerException("buffer");
    } 
  }
  
  public void setContent(File file) throws IOException {
    if (this.file != null)
      delete(); 
    this.file = file;
    this.size = file.length();
    this.isRenamed = true;
    this.completed = true;
  }
  
  public void setContent(InputStream inputStream) throws IOException {
    if (inputStream == null)
      throw new NullPointerException("inputStream"); 
    if (this.file != null)
      delete(); 
    this.file = tempFile();
    FileOutputStream outputStream = new FileOutputStream(this.file);
    FileChannel localfileChannel = outputStream.getChannel();
    byte[] bytes = new byte[16384];
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    int read = inputStream.read(bytes);
    int written = 0;
    while (read > 0) {
      byteBuffer.position(read).flip();
      written += localfileChannel.write(byteBuffer);
      read = inputStream.read(bytes);
    } 
    localfileChannel.force(false);
    localfileChannel.close();
    this.size = written;
    if (this.definedSize > 0L && this.definedSize < this.size) {
      this.file.delete();
      this.file = null;
      throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
    } 
    this.isRenamed = true;
    this.completed = true;
  }
  
  public void delete() {
    if (this.fileChannel != null) {
      try {
        this.fileChannel.force(false);
        this.fileChannel.close();
      } catch (IOException e) {
        logger.warn("Failed to close a file.", e);
      } 
      this.fileChannel = null;
    } 
    if (!this.isRenamed) {
      if (this.file != null && this.file.exists())
        this.file.delete(); 
      this.file = null;
    } 
  }
  
  public byte[] get() throws IOException {
    if (this.file == null)
      return EmptyArrays.EMPTY_BYTES; 
    return readFrom(this.file);
  }
  
  public ByteBuf getByteBuf() throws IOException {
    if (this.file == null)
      return Unpooled.EMPTY_BUFFER; 
    byte[] array = readFrom(this.file);
    return Unpooled.wrappedBuffer(array);
  }
  
  public ByteBuf getChunk(int length) throws IOException {
    if (this.file == null || length == 0)
      return Unpooled.EMPTY_BUFFER; 
    if (this.fileChannel == null) {
      FileInputStream inputStream = new FileInputStream(this.file);
      this.fileChannel = inputStream.getChannel();
    } 
    int read = 0;
    ByteBuffer byteBuffer = ByteBuffer.allocate(length);
    while (read < length) {
      int readnow = this.fileChannel.read(byteBuffer);
      if (readnow == -1) {
        this.fileChannel.close();
        this.fileChannel = null;
        break;
      } 
      read += readnow;
    } 
    if (read == 0)
      return Unpooled.EMPTY_BUFFER; 
    byteBuffer.flip();
    ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
    buffer.readerIndex(0);
    buffer.writerIndex(read);
    return buffer;
  }
  
  public String getString() throws IOException {
    return getString(HttpConstants.DEFAULT_CHARSET);
  }
  
  public String getString(Charset encoding) throws IOException {
    if (this.file == null)
      return ""; 
    if (encoding == null) {
      byte[] arrayOfByte = readFrom(this.file);
      return new String(arrayOfByte, HttpConstants.DEFAULT_CHARSET.name());
    } 
    byte[] array = readFrom(this.file);
    return new String(array, encoding.name());
  }
  
  public boolean isInMemory() {
    return false;
  }
  
  public boolean renameTo(File dest) throws IOException {
    if (dest == null)
      throw new NullPointerException("dest"); 
    if (this.file == null)
      throw new IOException("No file defined so cannot be renamed"); 
    if (!this.file.renameTo(dest)) {
      FileInputStream inputStream = new FileInputStream(this.file);
      FileOutputStream outputStream = new FileOutputStream(dest);
      FileChannel in = inputStream.getChannel();
      FileChannel out = outputStream.getChannel();
      int chunkSize = 8196;
      long position = 0L;
      while (position < this.size) {
        if (chunkSize < this.size - position)
          chunkSize = (int)(this.size - position); 
        position += in.transferTo(position, chunkSize, out);
      } 
      in.close();
      out.close();
      if (position == this.size) {
        this.file.delete();
        this.file = dest;
        this.isRenamed = true;
        return true;
      } 
      dest.delete();
      return false;
    } 
    this.file = dest;
    this.isRenamed = true;
    return true;
  }
  
  private static byte[] readFrom(File src) throws IOException {
    long srcsize = src.length();
    if (srcsize > 2147483647L)
      throw new IllegalArgumentException("File too big to be loaded in memory"); 
    FileInputStream inputStream = new FileInputStream(src);
    FileChannel fileChannel = inputStream.getChannel();
    byte[] array = new byte[(int)srcsize];
    ByteBuffer byteBuffer = ByteBuffer.wrap(array);
    int read = 0;
    while (read < srcsize)
      read += fileChannel.read(byteBuffer); 
    fileChannel.close();
    return array;
  }
  
  public File getFile() throws IOException {
    return this.file;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\AbstractDiskHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */