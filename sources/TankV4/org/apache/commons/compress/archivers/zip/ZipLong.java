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

   public ZipLong(long var1) {
      this.value = var1;
   }

   public ZipLong(byte[] var1) {
      this(var1, 0);
   }

   public ZipLong(byte[] var1, int var2) {
      this.value = getValue(var1, var2);
   }

   public byte[] getBytes() {
      return getBytes(this.value);
   }

   public long getValue() {
      return this.value;
   }

   public static byte[] getBytes(long var0) {
      byte[] var2 = new byte[]{(byte)((int)(var0 & 255L)), (byte)((int)((var0 & 65280L) >> 8)), (byte)((int)((var0 & 16711680L) >> 16)), (byte)((int)((var0 & 4278190080L) >> 24))};
      return var2;
   }

   public static long getValue(byte[] var0, int var1) {
      long var2 = (long)(var0[var1 + 3] << 24) & 4278190080L;
      var2 += (long)(var0[var1 + 2] << 16 & 16711680);
      var2 += (long)(var0[var1 + 1] << 8 & '\uff00');
      var2 += (long)(var0[var1] & 255);
      return var2;
   }

   public static long getValue(byte[] var0) {
      return getValue(var0, 0);
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof ZipLong) {
         return this.value == ((ZipLong)var1).getValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)this.value;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String toString() {
      return "ZipLong value: " + this.value;
   }
}
