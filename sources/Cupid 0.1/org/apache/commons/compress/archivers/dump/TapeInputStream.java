package org.apache.commons.compress.archivers.dump;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.commons.compress.utils.IOUtils;

class TapeInputStream extends FilterInputStream {
  private byte[] blockBuffer = new byte[1024];
  
  private int currBlkIdx = -1;
  
  private int blockSize = 1024;
  
  private static final int recordSize = 1024;
  
  private int readOffset = 1024;
  
  private boolean isCompressed = false;
  
  private long bytesRead = 0L;
  
  public TapeInputStream(InputStream in) {
    super(in);
  }
  
  public void resetBlockSize(int recsPerBlock, boolean isCompressed) throws IOException {
    this.isCompressed = isCompressed;
    this.blockSize = 1024 * recsPerBlock;
    byte[] oldBuffer = this.blockBuffer;
    this.blockBuffer = new byte[this.blockSize];
    System.arraycopy(oldBuffer, 0, this.blockBuffer, 0, 1024);
    readFully(this.blockBuffer, 1024, this.blockSize - 1024);
    this.currBlkIdx = 0;
    this.readOffset = 1024;
  }
  
  public int available() throws IOException {
    if (this.readOffset < this.blockSize)
      return this.blockSize - this.readOffset; 
    return this.in.available();
  }
  
  public int read() throws IOException {
    throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    if (len % 1024 != 0)
      throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes."); 
    int bytes = 0;
    while (bytes < len) {
      if (this.readOffset == this.blockSize && !readBlock(true))
        return -1; 
      int n = 0;
      if (this.readOffset + len - bytes <= this.blockSize) {
        n = len - bytes;
      } else {
        n = this.blockSize - this.readOffset;
      } 
      System.arraycopy(this.blockBuffer, this.readOffset, b, off, n);
      this.readOffset += n;
      bytes += n;
      off += n;
    } 
    return bytes;
  }
  
  public long skip(long len) throws IOException {
    if (len % 1024L != 0L)
      throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes."); 
    long bytes = 0L;
    while (bytes < len) {
      if (this.readOffset == this.blockSize && !readBlock((len - bytes < this.blockSize)))
        return -1L; 
      long n = 0L;
      if (this.readOffset + len - bytes <= this.blockSize) {
        n = len - bytes;
      } else {
        n = (this.blockSize - this.readOffset);
      } 
      this.readOffset = (int)(this.readOffset + n);
      bytes += n;
    } 
    return bytes;
  }
  
  public void close() throws IOException {
    if (this.in != null && this.in != System.in)
      this.in.close(); 
  }
  
  public byte[] peek() throws IOException {
    if (this.readOffset == this.blockSize && !readBlock(true))
      return null; 
    byte[] b = new byte[1024];
    System.arraycopy(this.blockBuffer, this.readOffset, b, 0, b.length);
    return b;
  }
  
  public byte[] readRecord() throws IOException {
    byte[] result = new byte[1024];
    if (-1 == read(result, 0, result.length))
      throw new ShortFileException(); 
    return result;
  }
  
  private boolean readBlock(boolean decompress) throws IOException {
    boolean success = true;
    if (this.in == null)
      throw new IOException("input buffer is closed"); 
    if (!this.isCompressed || this.currBlkIdx == -1) {
      success = readFully(this.blockBuffer, 0, this.blockSize);
      this.bytesRead += this.blockSize;
    } else {
      if (!readFully(this.blockBuffer, 0, 4))
        return false; 
      this.bytesRead += 4L;
      int h = DumpArchiveUtil.convert32(this.blockBuffer, 0);
      boolean compressed = ((h & 0x1) == 1);
      if (!compressed) {
        success = readFully(this.blockBuffer, 0, this.blockSize);
        this.bytesRead += this.blockSize;
      } else {
        int flags = h >> 1 & 0x7;
        int length = h >> 4 & 0xFFFFFFF;
        byte[] compBuffer = new byte[length];
        success = readFully(compBuffer, 0, length);
        this.bytesRead += length;
        if (!decompress) {
          Arrays.fill(this.blockBuffer, (byte)0);
        } else {
          switch (DumpArchiveConstants.COMPRESSION_TYPE.find(flags & 0x3)) {
            case ZLIB:
              try {
                Inflater inflator = new Inflater();
                inflator.setInput(compBuffer, 0, compBuffer.length);
                length = inflator.inflate(this.blockBuffer);
                if (length != this.blockSize)
                  throw new ShortFileException(); 
                inflator.end();
              } catch (DataFormatException e) {
                throw new DumpArchiveException("bad data", e);
              } 
              this.currBlkIdx++;
              this.readOffset = 0;
              return success;
            case BZLIB:
              throw new UnsupportedCompressionAlgorithmException("BZLIB2");
            case LZO:
              throw new UnsupportedCompressionAlgorithmException("LZO");
          } 
          throw new UnsupportedCompressionAlgorithmException();
        } 
      } 
    } 
    this.currBlkIdx++;
    this.readOffset = 0;
    return success;
  }
  
  private boolean readFully(byte[] b, int off, int len) throws IOException {
    int count = IOUtils.readFully(this.in, b, off, len);
    if (count < len)
      throw new ShortFileException(); 
    return true;
  }
  
  public long getBytesRead() {
    return this.bytesRead;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\TapeInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */