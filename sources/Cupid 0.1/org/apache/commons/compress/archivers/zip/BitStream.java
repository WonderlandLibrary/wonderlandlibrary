package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

class BitStream {
  private final InputStream in;
  
  private long bitCache;
  
  private int bitCacheSize;
  
  private static final int[] MASKS = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255 };
  
  BitStream(InputStream in) {
    this.in = in;
  }
  
  private boolean fillCache() throws IOException {
    boolean filled = false;
    while (this.bitCacheSize <= 56) {
      long nextByte = this.in.read();
      if (nextByte == -1L)
        break; 
      filled = true;
      this.bitCache |= nextByte << this.bitCacheSize;
      this.bitCacheSize += 8;
    } 
    return filled;
  }
  
  int nextBit() throws IOException {
    if (this.bitCacheSize == 0 && !fillCache())
      return -1; 
    int bit = (int)(this.bitCache & 0x1L);
    this.bitCache >>>= 1L;
    this.bitCacheSize--;
    return bit;
  }
  
  int nextBits(int n) throws IOException {
    if (this.bitCacheSize < n && !fillCache())
      return -1; 
    int bits = (int)(this.bitCache & MASKS[n]);
    this.bitCache >>>= n;
    this.bitCacheSize -= n;
    return bits;
  }
  
  int nextByte() throws IOException {
    return nextBits(8);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\BitStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */