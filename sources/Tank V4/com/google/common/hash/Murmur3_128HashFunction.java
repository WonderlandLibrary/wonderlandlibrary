package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nullable;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable {
   private final int seed;
   private static final long serialVersionUID = 0L;

   Murmur3_128HashFunction(int var1) {
      this.seed = var1;
   }

   public int bits() {
      return 128;
   }

   public Hasher newHasher() {
      return new Murmur3_128HashFunction.Murmur3_128Hasher(this.seed);
   }

   public String toString() {
      return "Hashing.murmur3_128(" + this.seed + ")";
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof Murmur3_128HashFunction) {
         Murmur3_128HashFunction var2 = (Murmur3_128HashFunction)var1;
         return this.seed == var2.seed;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getClass().hashCode() ^ this.seed;
   }

   private static final class Murmur3_128Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
      private static final int CHUNK_SIZE = 16;
      private static final long C1 = -8663945395140668459L;
      private static final long C2 = 5545529020109919103L;
      private long h1;
      private long h2;
      private int length;

      Murmur3_128Hasher(int var1) {
         super(16);
         this.h1 = (long)var1;
         this.h2 = (long)var1;
         this.length = 0;
      }

      protected void process(ByteBuffer var1) {
         long var2 = var1.getLong();
         long var4 = var1.getLong();
         this.bmix64(var2, var4);
         this.length += 16;
      }

      private void bmix64(long var1, long var3) {
         this.h1 ^= mixK1(var1);
         this.h1 = Long.rotateLeft(this.h1, 27);
         this.h1 += this.h2;
         this.h1 = this.h1 * 5L + 1390208809L;
         this.h2 ^= mixK2(var3);
         this.h2 = Long.rotateLeft(this.h2, 31);
         this.h2 += this.h1;
         this.h2 = this.h2 * 5L + 944331445L;
      }

      protected void processRemaining(ByteBuffer var1) {
         long var2 = 0L;
         long var4 = 0L;
         this.length += var1.remaining();
         switch(var1.remaining()) {
         case 7:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(6)) << 48;
         case 6:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(5)) << 40;
         case 5:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(4)) << 32;
         case 4:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(3)) << 24;
         case 3:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(2)) << 16;
         case 2:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(1)) << 8;
         case 1:
            var2 ^= (long)UnsignedBytes.toInt(var1.get(0));
            break;
         case 15:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(14)) << 48;
         case 14:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(13)) << 40;
         case 13:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(12)) << 32;
         case 12:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(11)) << 24;
         case 11:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(10)) << 16;
         case 10:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(9)) << 8;
         case 9:
            var4 ^= (long)UnsignedBytes.toInt(var1.get(8));
         case 8:
            var2 ^= var1.getLong();
            break;
         default:
            throw new AssertionError("Should never get here.");
         }

         this.h1 ^= mixK1(var2);
         this.h2 ^= mixK2(var4);
      }

      public HashCode makeHash() {
         this.h1 ^= (long)this.length;
         this.h2 ^= (long)this.length;
         this.h1 += this.h2;
         this.h2 += this.h1;
         this.h1 = fmix64(this.h1);
         this.h2 = fmix64(this.h2);
         this.h1 += this.h2;
         this.h2 += this.h1;
         return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
      }

      private static long fmix64(long var0) {
         var0 ^= var0 >>> 33;
         var0 *= -49064778989728563L;
         var0 ^= var0 >>> 33;
         var0 *= -4265267296055464877L;
         var0 ^= var0 >>> 33;
         return var0;
      }

      private static long mixK1(long var0) {
         var0 *= -8663945395140668459L;
         var0 = Long.rotateLeft(var0, 31);
         var0 *= 5545529020109919103L;
         return var0;
      }

      private static long mixK2(long var0) {
         var0 *= 5545529020109919103L;
         var0 = Long.rotateLeft(var0, 33);
         var0 *= -8663945395140668459L;
         return var0;
      }
   }
}
