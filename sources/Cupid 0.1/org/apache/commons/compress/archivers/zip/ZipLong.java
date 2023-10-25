package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;

public final class ZipLong implements Cloneable, Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final int BYTE_1 = 1;
  
  private static final int BYTE_1_MASK = 65280;
  
  private static final int BYTE_1_SHIFT = 8;
  
  private static final int BYTE_2 = 2;
  
  private static final int BYTE_2_MASK = 16711680;
  
  private static final int BYTE_2_SHIFT = 16;
  
  private static final int BYTE_3 = 3;
  
  private static final long BYTE_3_MASK = 4278190080L;
  
  private static final int BYTE_3_SHIFT = 24;
  
  private final long value;
  
  public static final ZipLong CFH_SIG = new ZipLong(33639248L);
  
  public static final ZipLong LFH_SIG = new ZipLong(67324752L);
  
  public static final ZipLong DD_SIG = new ZipLong(134695760L);
  
  static final ZipLong ZIP64_MAGIC = new ZipLong(4294967295L);
  
  public static final ZipLong SINGLE_SEGMENT_SPLIT_MARKER = new ZipLong(808471376L);
  
  public static final ZipLong AED_SIG = new ZipLong(134630224L);
  
  public ZipLong(long value) {
    this.value = value;
  }
  
  public ZipLong(byte[] bytes) {
    this(bytes, 0);
  }
  
  public ZipLong(byte[] bytes, int offset) {
    this.value = getValue(bytes, offset);
  }
  
  public byte[] getBytes() {
    return getBytes(this.value);
  }
  
  public long getValue() {
    return this.value;
  }
  
  public static byte[] getBytes(long value) {
    byte[] result = new byte[4];
    result[0] = (byte)(int)(value & 0xFFL);
    result[1] = (byte)(int)((value & 0xFF00L) >> 8L);
    result[2] = (byte)(int)((value & 0xFF0000L) >> 16L);
    result[3] = (byte)(int)((value & 0xFF000000L) >> 24L);
    return result;
  }
  
  public static long getValue(byte[] bytes, int offset) {
    long value = (bytes[offset + 3] << 24) & 0xFF000000L;
    value += (bytes[offset + 2] << 16 & 0xFF0000);
    value += (bytes[offset + 1] << 8 & 0xFF00);
    value += (bytes[offset] & 0xFF);
    return value;
  }
  
  public static long getValue(byte[] bytes) {
    return getValue(bytes, 0);
  }
  
  public boolean equals(Object o) {
    if (o == null || !(o instanceof ZipLong))
      return false; 
    return (this.value == ((ZipLong)o).getValue());
  }
  
  public int hashCode() {
    return (int)this.value;
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cnfe) {
      throw new RuntimeException(cnfe);
    } 
  }
  
  public String toString() {
    return "ZipLong value: " + this.value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\ZipLong.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */