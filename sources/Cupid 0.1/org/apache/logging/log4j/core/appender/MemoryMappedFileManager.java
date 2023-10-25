package org.apache.logging.log4j.core.appender;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.NullOutputStream;
import org.apache.logging.log4j.util.Constants;

public class MemoryMappedFileManager extends OutputStreamManager {
  static final int DEFAULT_REGION_LENGTH = 33554432;
  
  private static final int MAX_REMAP_COUNT = 10;
  
  private static final MemoryMappedFileManagerFactory FACTORY = new MemoryMappedFileManagerFactory();
  
  private static final double NANOS_PER_MILLISEC = 1000000.0D;
  
  private final boolean immediateFlush;
  
  private final int regionLength;
  
  private final String advertiseURI;
  
  private final RandomAccessFile randomAccessFile;
  
  private MappedByteBuffer mappedBuffer;
  
  private long mappingOffset;
  
  protected MemoryMappedFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, long position, int regionLength, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) throws IOException {
    super(os, fileName, layout, writeHeader, ByteBuffer.wrap(Constants.EMPTY_BYTE_ARRAY));
    this.immediateFlush = immediateFlush;
    this.randomAccessFile = Objects.<RandomAccessFile>requireNonNull(file, "RandomAccessFile");
    this.regionLength = regionLength;
    this.advertiseURI = advertiseURI;
    this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), position, regionLength);
    this.byteBuffer = this.mappedBuffer;
    this.mappingOffset = position;
  }
  
  public static MemoryMappedFileManager getFileManager(String fileName, boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
    return narrow(MemoryMappedFileManager.class, getManager(fileName, new FactoryData(append, immediateFlush, regionLength, advertiseURI, layout), FACTORY));
  }
  
  @Deprecated
  public Boolean isEndOfBatch() {
    return Boolean.FALSE;
  }
  
  @Deprecated
  public void setEndOfBatch(boolean endOfBatch) {}
  
  protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
    while (length > this.mappedBuffer.remaining()) {
      int chunk = this.mappedBuffer.remaining();
      this.mappedBuffer.put(bytes, offset, chunk);
      offset += chunk;
      length -= chunk;
      remap();
    } 
    this.mappedBuffer.put(bytes, offset, length);
  }
  
  private synchronized void remap() {
    long offset = this.mappingOffset + this.mappedBuffer.position();
    int length = this.mappedBuffer.remaining() + this.regionLength;
    try {
      unsafeUnmap(this.mappedBuffer);
      long fileLength = this.randomAccessFile.length() + this.regionLength;
      LOGGER.debug("{} {} extending {} by {} bytes to {}", getClass().getSimpleName(), getName(), getFileName(), 
          Integer.valueOf(this.regionLength), Long.valueOf(fileLength));
      long startNanos = System.nanoTime();
      this.randomAccessFile.setLength(fileLength);
      float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
      LOGGER.debug("{} {} extended {} OK in {} millis", getClass().getSimpleName(), getName(), getFileName(), 
          Float.valueOf(millis));
      this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), offset, length);
      this.byteBuffer = this.mappedBuffer;
      this.mappingOffset = offset;
    } catch (Exception ex) {
      logError("Unable to remap", ex);
    } 
  }
  
  public synchronized void flush() {
    this.mappedBuffer.force();
  }
  
  public synchronized boolean closeOutputStream() {
    long position = this.mappedBuffer.position();
    long length = this.mappingOffset + position;
    try {
      unsafeUnmap(this.mappedBuffer);
    } catch (Exception ex) {
      logError("Unable to unmap MappedBuffer", ex);
    } 
    try {
      LOGGER.debug("MMapAppender closing. Setting {} length to {} (offset {} + position {})", getFileName(), 
          Long.valueOf(length), Long.valueOf(this.mappingOffset), Long.valueOf(position));
      this.randomAccessFile.setLength(length);
      this.randomAccessFile.close();
      return true;
    } catch (IOException ex) {
      logError("Unable to close MemoryMappedFile", ex);
      return false;
    } 
  }
  
  public static MappedByteBuffer mmap(FileChannel fileChannel, String fileName, long start, int size) throws IOException {
    for (int i = 1;; i++) {
      try {
        LOGGER.debug("MMapAppender remapping {} start={}, size={}", fileName, Long.valueOf(start), Integer.valueOf(size));
        long startNanos = System.nanoTime();
        MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
        map.order(ByteOrder.nativeOrder());
        float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
        LOGGER.debug("MMapAppender remapped {} OK in {} millis", fileName, Float.valueOf(millis));
        return map;
      } catch (IOException e) {
        if (e.getMessage() == null || !e.getMessage().endsWith("user-mapped section open"))
          throw e; 
        LOGGER.debug("Remap attempt {}/{} failed. Retrying...", Integer.valueOf(i), Integer.valueOf(10), e);
        if (i < 10) {
          Thread.yield();
        } else {
          try {
            Thread.sleep(1L);
          } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            throw e;
          } 
        } 
      } 
    } 
  }
  
  private static void unsafeUnmap(MappedByteBuffer mbb) throws PrivilegedActionException {
    LOGGER.debug("MMapAppender unmapping old buffer...");
    long startNanos = System.nanoTime();
    AccessController.doPrivileged(() -> {
          Method getCleanerMethod = mbb.getClass().getMethod("cleaner", new Class[0]);
          getCleanerMethod.setAccessible(true);
          Object cleaner = getCleanerMethod.invoke(mbb, new Object[0]);
          Method cleanMethod = cleaner.getClass().getMethod("clean", new Class[0]);
          cleanMethod.invoke(cleaner, new Object[0]);
          return null;
        });
    float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
    LOGGER.debug("MMapAppender unmapped buffer OK in {} millis", Float.valueOf(millis));
  }
  
  public String getFileName() {
    return getName();
  }
  
  public int getRegionLength() {
    return this.regionLength;
  }
  
  public boolean isImmediateFlush() {
    return this.immediateFlush;
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>(super.getContentFormat());
    result.put("fileURI", this.advertiseURI);
    return result;
  }
  
  protected void flushBuffer(ByteBuffer buffer) {}
  
  public ByteBuffer getByteBuffer() {
    return this.mappedBuffer;
  }
  
  public ByteBuffer drain(ByteBuffer buf) {
    remap();
    return this.mappedBuffer;
  }
  
  private static class FactoryData {
    private final boolean append;
    
    private final boolean immediateFlush;
    
    private final int regionLength;
    
    private final String advertiseURI;
    
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
      this.append = append;
      this.immediateFlush = immediateFlush;
      this.regionLength = regionLength;
      this.advertiseURI = advertiseURI;
      this.layout = layout;
    }
  }
  
  private static class MemoryMappedFileManagerFactory implements ManagerFactory<MemoryMappedFileManager, FactoryData> {
    private MemoryMappedFileManagerFactory() {}
    
    public MemoryMappedFileManager createManager(String name, MemoryMappedFileManager.FactoryData data) {
      File file = new File(name);
      if (!data.append)
        file.delete(); 
      boolean writeHeader = (!data.append || !file.exists());
      NullOutputStream nullOutputStream = NullOutputStream.getInstance();
      RandomAccessFile raf = null;
      try {
        FileUtils.makeParentDirs(file);
        raf = new RandomAccessFile(name, "rw");
        long position = data.append ? raf.length() : 0L;
        raf.setLength(position + data.regionLength);
        return new MemoryMappedFileManager(raf, name, (OutputStream)nullOutputStream, data.immediateFlush, position, data.regionLength, data
            .advertiseURI, data.layout, writeHeader);
      } catch (Exception ex) {
        AbstractManager.LOGGER.error("MemoryMappedFileManager (" + name + ") " + ex, ex);
        Closer.closeSilently(raf);
        return null;
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */