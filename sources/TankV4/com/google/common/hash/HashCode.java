package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;
import java.security.MessageDigest;
import javax.annotation.Nullable;

@Beta
public abstract class HashCode {
   private static final char[] hexDigits = "0123456789abcdef".toCharArray();

   HashCode() {
   }

   public abstract int bits();

   public abstract int asInt();

   public abstract long asLong();

   public abstract long padToLong();

   public abstract byte[] asBytes();

   public int writeBytesTo(byte[] var1, int var2, int var3) {
      var3 = Ints.min(var3, this.bits() / 8);
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      this.writeBytesToImpl(var1, var2, var3);
      return var3;
   }

   abstract void writeBytesToImpl(byte[] var1, int var2, int var3);

   byte[] getBytesInternal() {
      return this.asBytes();
   }

   public static HashCode fromInt(int var0) {
      return new HashCode.IntHashCode(var0);
   }

   public static HashCode fromLong(long var0) {
      return new HashCode.LongHashCode(var0);
   }

   public static HashCode fromBytes(byte[] var0) {
      Preconditions.checkArgument(var0.length >= 1, "A HashCode must contain at least 1 byte.");
      return fromBytesNoCopy((byte[])var0.clone());
   }

   static HashCode fromBytesNoCopy(byte[] var0) {
      return new HashCode.BytesHashCode(var0);
   }

   public static HashCode fromString(String var0) {
      Preconditions.checkArgument(var0.length() >= 2, "input string (%s) must have at least 2 characters", var0);
      Preconditions.checkArgument(var0.length() % 2 == 0, "input string (%s) must have an even number of characters", var0);
      byte[] var1 = new byte[var0.length() / 2];

      for(int var2 = 0; var2 < var0.length(); var2 += 2) {
         int var3 = decode(var0.charAt(var2)) << 4;
         int var4 = decode(var0.charAt(var2 + 1));
         var1[var2 / 2] = (byte)(var3 + var4);
      }

      return fromBytesNoCopy(var1);
   }

   private static int decode(char var0) {
      if (var0 >= '0' && var0 <= '9') {
         return var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         return var0 - 97 + 10;
      } else {
         throw new IllegalArgumentException("Illegal hexadecimal character: " + var0);
      }
   }

   public final boolean equals(@Nullable Object var1) {
      if (var1 instanceof HashCode) {
         HashCode var2 = (HashCode)var1;
         return MessageDigest.isEqual(this.asBytes(), var2.asBytes());
      } else {
         return false;
      }
   }

   public final int hashCode() {
      if (this.bits() >= 32) {
         return this.asInt();
      } else {
         byte[] var1 = this.asBytes();
         int var2 = var1[0] & 255;

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2 |= (var1[var3] & 255) << var3 * 8;
         }

         return var2;
      }
   }

   public final String toString() {
      byte[] var1 = this.asBytes();
      StringBuilder var2 = new StringBuilder(2 * var1.length);
      byte[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var3[var5];
         var2.append(hexDigits[var6 >> 4 & 15]).append(hexDigits[var6 & 15]);
      }

      return var2.toString();
   }

   private static final class BytesHashCode extends HashCode implements Serializable {
      final byte[] bytes;
      private static final long serialVersionUID = 0L;

      BytesHashCode(byte[] var1) {
         this.bytes = (byte[])Preconditions.checkNotNull(var1);
      }

      public int bits() {
         return this.bytes.length * 8;
      }

      public byte[] asBytes() {
         return (byte[])this.bytes.clone();
      }

      public int asInt() {
         Preconditions.checkState(this.bytes.length >= 4, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
         return this.bytes[0] & 255 | (this.bytes[1] & 255) << 8 | (this.bytes[2] & 255) << 16 | (this.bytes[3] & 255) << 24;
      }

      public long asLong() {
         Preconditions.checkState(this.bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
         return this.padToLong();
      }

      public long padToLong() {
         long var1 = (long)(this.bytes[0] & 255);

         for(int var3 = 1; var3 < Math.min(this.bytes.length, 8); ++var3) {
            var1 |= ((long)this.bytes[var3] & 255L) << var3 * 8;
         }

         return var1;
      }

      void writeBytesToImpl(byte[] var1, int var2, int var3) {
         System.arraycopy(this.bytes, 0, var1, var2, var3);
      }

      byte[] getBytesInternal() {
         return this.bytes;
      }
   }

   private static final class LongHashCode extends HashCode implements Serializable {
      final long hash;
      private static final long serialVersionUID = 0L;

      LongHashCode(long var1) {
         this.hash = var1;
      }

      public int bits() {
         return 64;
      }

      public byte[] asBytes() {
         return new byte[]{(byte)((int)this.hash), (byte)((int)(this.hash >> 8)), (byte)((int)(this.hash >> 16)), (byte)((int)(this.hash >> 24)), (byte)((int)(this.hash >> 32)), (byte)((int)(this.hash >> 40)), (byte)((int)(this.hash >> 48)), (byte)((int)(this.hash >> 56))};
      }

      public int asInt() {
         return (int)this.hash;
      }

      public long asLong() {
         return this.hash;
      }

      public long padToLong() {
         return this.hash;
      }

      void writeBytesToImpl(byte[] var1, int var2, int var3) {
         for(int var4 = 0; var4 < var3; ++var4) {
            var1[var2 + var4] = (byte)((int)(this.hash >> var4 * 8));
         }

      }
   }

   private static final class IntHashCode extends HashCode implements Serializable {
      final int hash;
      private static final long serialVersionUID = 0L;

      IntHashCode(int var1) {
         this.hash = var1;
      }

      public int bits() {
         return 32;
      }

      public byte[] asBytes() {
         return new byte[]{(byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24)};
      }

      public int asInt() {
         return this.hash;
      }

      public long asLong() {
         throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
      }

      public long padToLong() {
         return UnsignedInts.toLong(this.hash);
      }

      void writeBytesToImpl(byte[] var1, int var2, int var3) {
         for(int var4 = 0; var4 < var3; ++var4) {
            var1[var2 + var4] = (byte)(this.hash >> var4 * 8);
         }

      }
   }
}
