package com.ibm.icu.text;

import com.ibm.icu.util.ByteArrayWrapper;

public final class RawCollationKey extends ByteArrayWrapper {
  public RawCollationKey() {}
  
  public RawCollationKey(int capacity) {
    this.bytes = new byte[capacity];
  }
  
  public RawCollationKey(byte[] bytes) {
    this.bytes = bytes;
  }
  
  public RawCollationKey(byte[] bytesToAdopt, int size) {
    super(bytesToAdopt, size);
  }
  
  public int compareTo(RawCollationKey rhs) {
    int result = compareTo(rhs);
    return (result < 0) ? -1 : ((result == 0) ? 0 : 1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RawCollationKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */