package com.ibm.icu.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Trie2_32 extends Trie2 {
  public static Trie2_32 createFromSerialized(InputStream is) throws IOException {
    return (Trie2_32)Trie2.createFromSerialized(is);
  }
  
  public final int get(int codePoint) {
    if (codePoint >= 0) {
      if (codePoint < 55296 || (codePoint > 56319 && codePoint <= 65535)) {
        int ix = this.index[codePoint >> 5];
        ix = (ix << 2) + (codePoint & 0x1F);
        int value = this.data32[ix];
        return value;
      } 
      if (codePoint <= 65535) {
        int ix = this.index[2048 + (codePoint - 55296 >> 5)];
        ix = (ix << 2) + (codePoint & 0x1F);
        int value = this.data32[ix];
        return value;
      } 
      if (codePoint < this.highStart) {
        int ix = 2080 + (codePoint >> 11);
        ix = this.index[ix];
        ix += codePoint >> 5 & 0x3F;
        ix = this.index[ix];
        ix = (ix << 2) + (codePoint & 0x1F);
        int value = this.data32[ix];
        return value;
      } 
      if (codePoint <= 1114111) {
        int value = this.data32[this.highValueIndex];
        return value;
      } 
    } 
    return this.errorValue;
  }
  
  public int getFromU16SingleLead(char codeUnit) {
    int ix = this.index[codeUnit >> 5];
    ix = (ix << 2) + (codeUnit & 0x1F);
    int value = this.data32[ix];
    return value;
  }
  
  public int serialize(OutputStream os) throws IOException {
    DataOutputStream dos = new DataOutputStream(os);
    int bytesWritten = 0;
    bytesWritten += serializeHeader(dos);
    for (int i = 0; i < this.dataLength; i++)
      dos.writeInt(this.data32[i]); 
    bytesWritten += this.dataLength * 4;
    return bytesWritten;
  }
  
  public int getSerializedLength() {
    return 16 + this.header.indexLength * 2 + this.dataLength * 4;
  }
  
  int rangeEnd(int startingCP, int limit, int value) {
    int cp = startingCP;
    int block = 0;
    int index2Block = 0;
    label42: while (cp < limit) {
      if (cp < 55296 || (cp > 56319 && cp <= 65535)) {
        index2Block = 0;
        block = this.index[cp >> 5] << 2;
      } else if (cp < 65535) {
        index2Block = 2048;
        block = this.index[index2Block + (cp - 55296 >> 5)] << 2;
      } else if (cp < this.highStart) {
        int i = 2080 + (cp >> 11);
        index2Block = this.index[i];
        block = this.index[index2Block + (cp >> 5 & 0x3F)] << 2;
      } else {
        if (value == this.data32[this.highValueIndex])
          cp = limit; 
        break;
      } 
      if (index2Block == this.index2NullOffset) {
        if (value != this.initialValue)
          break; 
        cp += 2048;
        continue;
      } 
      if (block == this.dataNullOffset) {
        if (value != this.initialValue)
          break; 
        cp += 32;
        continue;
      } 
      int startIx = block + (cp & 0x1F);
      int limitIx = block + 32;
      for (int ix = startIx; ix < limitIx; ix++) {
        if (this.data32[ix] != value) {
          cp += ix - startIx;
          break label42;
        } 
      } 
      cp += limitIx - startIx;
    } 
    if (cp > limit)
      cp = limit; 
    return cp - 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Trie2_32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */