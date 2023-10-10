package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;
import java.nio.ByteBuffer;

public class ByteArrayWrapper implements Comparable {
   public byte[] bytes;
   public int size;

   public ByteArrayWrapper() {
   }

   public ByteArrayWrapper(byte[] var1, int var2) {
      if ((var1 != null || var2 == 0) && var2 >= 0 && var2 <= var1.length) {
         this.bytes = var1;
         this.size = var2;
      } else {
         throw new IndexOutOfBoundsException("illegal size: " + var2);
      }
   }

   public ByteArrayWrapper(ByteBuffer var1) {
      this.size = var1.limit();
      this.bytes = new byte[this.size];
      var1.get(this.bytes, 0, this.size);
   }

   public ByteArrayWrapper ensureCapacity(int var1) {
      if (this.bytes == null || this.bytes.length < var1) {
         byte[] var2 = new byte[var1];
         copyBytes(this.bytes, 0, var2, 0, this.size);
         this.bytes = var2;
      }

      return this;
   }

   public final ByteArrayWrapper set(byte[] var1, int var2, int var3) {
      this.size = 0;
      this.append(var1, var2, var3);
      return this;
   }

   public final ByteArrayWrapper append(byte[] var1, int var2, int var3) {
      int var4 = var3 - var2;
      this.ensureCapacity(this.size + var4);
      copyBytes(var1, var2, this.bytes, this.size, var4);
      this.size += var4;
      return this;
   }

   public final byte[] releaseBytes() {
      byte[] var1 = this.bytes;
      this.bytes = null;
      this.size = 0;
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.size; ++var2) {
         if (var2 != 0) {
            var1.append(" ");
         }

         var1.append(Utility.hex((long)(this.bytes[var2] & 255), 2));
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         try {
            ByteArrayWrapper var2 = (ByteArrayWrapper)var1;
            if (this.size != var2.size) {
               return false;
            } else {
               for(int var3 = 0; var3 < this.size; ++var3) {
                  if (this.bytes[var3] != var2.bytes[var3]) {
                     return false;
                  }
               }

               return true;
            }
         } catch (ClassCastException var4) {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = this.bytes.length;

      for(int var2 = 0; var2 < this.size; ++var2) {
         var1 = 37 * var1 + this.bytes[var2];
      }

      return var1;
   }

   public int compareTo(ByteArrayWrapper var1) {
      if (this == var1) {
         return 0;
      } else {
         int var2 = this.size < var1.size ? this.size : var1.size;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (this.bytes[var3] != var1.bytes[var3]) {
               return (this.bytes[var3] & 255) - (var1.bytes[var3] & 255);
            }
         }

         return this.size - var1.size;
      }
   }

   private static final void copyBytes(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      if (var4 < 64) {
         int var5 = var1;
         int var6 = var3;

         while(true) {
            --var4;
            if (var4 < 0) {
               break;
            }

            var2[var6] = var0[var5];
            ++var5;
            ++var6;
         }
      } else {
         System.arraycopy(var0, var1, var2, var3, var4);
      }

   }

   public int compareTo(Object var1) {
      return this.compareTo((ByteArrayWrapper)var1);
   }
}
