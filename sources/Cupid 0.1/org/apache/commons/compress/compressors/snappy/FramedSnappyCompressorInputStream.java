package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class FramedSnappyCompressorInputStream extends CompressorInputStream {
  static final long MASK_OFFSET = 2726488792L;
  
  private static final int STREAM_IDENTIFIER_TYPE = 255;
  
  private static final int COMPRESSED_CHUNK_TYPE = 0;
  
  private static final int UNCOMPRESSED_CHUNK_TYPE = 1;
  
  private static final int PADDING_CHUNK_TYPE = 254;
  
  private static final int MIN_UNSKIPPABLE_TYPE = 2;
  
  private static final int MAX_UNSKIPPABLE_TYPE = 127;
  
  private static final int MAX_SKIPPABLE_TYPE = 253;
  
  private static final byte[] SZ_SIGNATURE = new byte[] { -1, 6, 0, 0, 115, 78, 97, 80, 112, 89 };
  
  private final PushbackInputStream in;
  
  private SnappyCompressorInputStream currentCompressedChunk;
  
  private final byte[] oneByte = new byte[1];
  
  private boolean endReached;
  
  private boolean inUncompressedChunk;
  
  private int uncompressedBytesRemaining;
  
  private long expectedChecksum = -1L;
  
  private final PureJavaCrc32C checksum = new PureJavaCrc32C();
  
  public FramedSnappyCompressorInputStream(InputStream in) throws IOException {
    this.in = new PushbackInputStream(in, 1);
    readStreamIdentifier();
  }
  
  public int read() throws IOException {
    return (read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
  }
  
  public void close() throws IOException {
    if (this.currentCompressedChunk != null) {
      this.currentCompressedChunk.close();
      this.currentCompressedChunk = null;
    } 
    this.in.close();
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    int read = readOnce(b, off, len);
    if (read == -1) {
      readNextBlock();
      if (this.endReached)
        return -1; 
      read = readOnce(b, off, len);
    } 
    return read;
  }
  
  public int available() throws IOException {
    if (this.inUncompressedChunk)
      return Math.min(this.uncompressedBytesRemaining, this.in.available()); 
    if (this.currentCompressedChunk != null)
      return this.currentCompressedChunk.available(); 
    return 0;
  }
  
  private int readOnce(byte[] b, int off, int len) throws IOException {
    int read = -1;
    if (this.inUncompressedChunk) {
      int amount = Math.min(this.uncompressedBytesRemaining, len);
      if (amount == 0)
        return -1; 
      read = this.in.read(b, off, amount);
      if (read != -1) {
        this.uncompressedBytesRemaining -= read;
        count(read);
      } 
    } else if (this.currentCompressedChunk != null) {
      long before = this.currentCompressedChunk.getBytesRead();
      read = this.currentCompressedChunk.read(b, off, len);
      if (read == -1) {
        this.currentCompressedChunk.close();
        this.currentCompressedChunk = null;
      } else {
        count(this.currentCompressedChunk.getBytesRead() - before);
      } 
    } 
    if (read > 0)
      this.checksum.update(b, off, read); 
    return read;
  }
  
  private void readNextBlock() throws IOException {
    verifyLastChecksumAndReset();
    this.inUncompressedChunk = false;
    int type = readOneByte();
    if (type == -1) {
      this.endReached = true;
    } else if (type == 255) {
      this.in.unread(type);
      pushedBackBytes(1L);
      readStreamIdentifier();
      readNextBlock();
    } else if (type == 254 || (type > 127 && type <= 253)) {
      skipBlock();
      readNextBlock();
    } else {
      if (type >= 2 && type <= 127)
        throw new IOException("unskippable chunk with type " + type + " (hex " + Integer.toHexString(type) + ")" + " detected."); 
      if (type == 1) {
        this.inUncompressedChunk = true;
        this.uncompressedBytesRemaining = readSize() - 4;
        this.expectedChecksum = unmask(readCrc());
      } else if (type == 0) {
        long size = (readSize() - 4);
        this.expectedChecksum = unmask(readCrc());
        this.currentCompressedChunk = new SnappyCompressorInputStream((InputStream)new BoundedInputStream(this.in, size));
        count(this.currentCompressedChunk.getBytesRead());
      } else {
        throw new IOException("unknown chunk type " + type + " detected.");
      } 
    } 
  }
  
  private long readCrc() throws IOException {
    byte[] b = new byte[4];
    int read = IOUtils.readFully(this.in, b);
    count(read);
    if (read != 4)
      throw new IOException("premature end of stream"); 
    long crc = 0L;
    for (int i = 0; i < 4; i++)
      crc |= (b[i] & 0xFFL) << 8 * i; 
    return crc;
  }
  
  static long unmask(long x) {
    x -= 2726488792L;
    x &= 0xFFFFFFFFL;
    return (x >> 17L | x << 15L) & 0xFFFFFFFFL;
  }
  
  private int readSize() throws IOException {
    int b = 0;
    int sz = 0;
    for (int i = 0; i < 3; i++) {
      b = readOneByte();
      if (b == -1)
        throw new IOException("premature end of stream"); 
      sz |= b << i * 8;
    } 
    return sz;
  }
  
  private void skipBlock() throws IOException {
    int size = readSize();
    long read = IOUtils.skip(this.in, size);
    count(read);
    if (read != size)
      throw new IOException("premature end of stream"); 
  }
  
  private void readStreamIdentifier() throws IOException {
    byte[] b = new byte[10];
    int read = IOUtils.readFully(this.in, b);
    count(read);
    if (10 != read || !matches(b, 10))
      throw new IOException("Not a framed Snappy stream"); 
  }
  
  private int readOneByte() throws IOException {
    int b = this.in.read();
    if (b != -1) {
      count(1);
      return b & 0xFF;
    } 
    return -1;
  }
  
  private void verifyLastChecksumAndReset() throws IOException {
    if (this.expectedChecksum >= 0L && this.expectedChecksum != this.checksum.getValue())
      throw new IOException("Checksum verification failed"); 
    this.expectedChecksum = -1L;
    this.checksum.reset();
  }
  
  public static boolean matches(byte[] signature, int length) {
    if (length < SZ_SIGNATURE.length)
      return false; 
    byte[] shortenedSig = signature;
    if (signature.length > SZ_SIGNATURE.length) {
      shortenedSig = new byte[SZ_SIGNATURE.length];
      System.arraycopy(signature, 0, shortenedSig, 0, SZ_SIGNATURE.length);
    } 
    return Arrays.equals(shortenedSig, SZ_SIGNATURE);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\snappy\FramedSnappyCompressorInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */