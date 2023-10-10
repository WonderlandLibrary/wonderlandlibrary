package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;

public final class ZipShort implements Cloneable, Serializable {
   private static final long serialVersionUID = 1L;
   private static final int BYTE_1_MASK = 65280;
   private static final int BYTE_1_SHIFT = 8;
   private final int value;

   public ZipShort(int var1) {
      this.value = var1;
   }

   public ZipShort(byte[] var1) {
      this(var1, 0);
   }

   public ZipShort(byte[] var1, int var2) {
      this.value = getValue(var1, var2);
   }

   public byte[] getBytes() {
      byte[] var1 = new byte[]{(byte)(this.value & 255), (byte)((this.value & '\uff00') >> 8)};
      return var1;
   }

   public int getValue() {
      return this.value;
   }

   public static byte[] getBytes(int var0) {
      byte[] var1 = new byte[]{(byte)(var0 & 255), (byte)((var0 & '\uff00') >> 8)};
      return var1;
   }

   public static int getValue(byte[] var0, int var1) {
      int var2 = var0[var1 + 1] << 8 & '\uff00';
      var2 += var0[var1] & 255;
      return var2;
   }

   public static int getValue(byte[] var0) {
      return getValue(var0, 0);
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof ZipShort) {
         return this.value == ((ZipShort)var1).getValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String toString() {
      return "ZipShort value: " + this.value;
   }
}
