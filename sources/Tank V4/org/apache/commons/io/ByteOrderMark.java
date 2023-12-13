package org.apache.commons.io;

import java.io.Serializable;

public class ByteOrderMark implements Serializable {
   private static final long serialVersionUID = 1L;
   public static final ByteOrderMark UTF_8 = new ByteOrderMark("UTF-8", new int[]{239, 187, 191});
   public static final ByteOrderMark UTF_16BE = new ByteOrderMark("UTF-16BE", new int[]{254, 255});
   public static final ByteOrderMark UTF_16LE = new ByteOrderMark("UTF-16LE", new int[]{255, 254});
   public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE", new int[]{0, 0, 254, 255});
   public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE", new int[]{255, 254, 0, 0});
   private final String charsetName;
   private final int[] bytes;

   public ByteOrderMark(String var1, int... var2) {
      if (var1 != null && var1.length() != 0) {
         if (var2 != null && var2.length != 0) {
            this.charsetName = var1;
            this.bytes = new int[var2.length];
            System.arraycopy(var2, 0, this.bytes, 0, var2.length);
         } else {
            throw new IllegalArgumentException("No bytes specified");
         }
      } else {
         throw new IllegalArgumentException("No charsetName specified");
      }
   }

   public String getCharsetName() {
      return this.charsetName;
   }

   public int length() {
      return this.bytes.length;
   }

   public int get(int var1) {
      return this.bytes[var1];
   }

   public byte[] getBytes() {
      byte[] var1 = new byte[this.bytes.length];

      for(int var2 = 0; var2 < this.bytes.length; ++var2) {
         var1[var2] = (byte)this.bytes[var2];
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ByteOrderMark)) {
         return false;
      } else {
         ByteOrderMark var2 = (ByteOrderMark)var1;
         if (this.bytes.length != var2.length()) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.bytes.length; ++var3) {
               if (this.bytes[var3] != var2.get(var3)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int var1 = this.getClass().hashCode();
      int[] var2 = this.bytes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var1 += var5;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append('[');
      var1.append(this.charsetName);
      var1.append(": ");

      for(int var2 = 0; var2 < this.bytes.length; ++var2) {
         if (var2 > 0) {
            var1.append(",");
         }

         var1.append("0x");
         var1.append(Integer.toHexString(255 & this.bytes[var2]).toUpperCase());
      }

      var1.append(']');
      return var1.toString();
   }
}
