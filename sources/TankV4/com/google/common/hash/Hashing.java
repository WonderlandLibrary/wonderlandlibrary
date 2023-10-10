package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Iterator;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.Nullable;

@Beta
public final class Hashing {
   private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();

   public static HashFunction goodFastHash(int var0) {
      int var1 = checkPositiveAndMakeMultipleOf32(var0);
      if (var1 == 32) {
         return Hashing.Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
      } else if (var1 <= 128) {
         return Hashing.Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
      } else {
         int var2 = (var1 + 127) / 128;
         HashFunction[] var3 = new HashFunction[var2];
         var3[0] = Hashing.Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
         int var4 = GOOD_FAST_HASH_SEED;

         for(int var5 = 1; var5 < var2; ++var5) {
            var4 += 1500450271;
            var3[var5] = murmur3_128(var4);
         }

         return new Hashing.ConcatenatedHashFunction(var3);
      }
   }

   public static HashFunction murmur3_32(int var0) {
      return new Murmur3_32HashFunction(var0);
   }

   public static HashFunction murmur3_32() {
      return Hashing.Murmur3_32Holder.MURMUR3_32;
   }

   public static HashFunction murmur3_128(int var0) {
      return new Murmur3_128HashFunction(var0);
   }

   public static HashFunction murmur3_128() {
      return Hashing.Murmur3_128Holder.MURMUR3_128;
   }

   public static HashFunction sipHash24() {
      return Hashing.SipHash24Holder.SIP_HASH_24;
   }

   public static HashFunction sipHash24(long var0, long var2) {
      return new SipHashFunction(2, 4, var0, var2);
   }

   public static HashFunction md5() {
      return Hashing.Md5Holder.MD5;
   }

   public static HashFunction sha1() {
      return Hashing.Sha1Holder.SHA_1;
   }

   public static HashFunction sha256() {
      return Hashing.Sha256Holder.SHA_256;
   }

   public static HashFunction sha512() {
      return Hashing.Sha512Holder.SHA_512;
   }

   public static HashFunction crc32() {
      return Hashing.Crc32Holder.CRC_32;
   }

   public static HashFunction adler32() {
      return Hashing.Adler32Holder.ADLER_32;
   }

   private static HashFunction checksumHashFunction(Hashing.ChecksumType var0, String var1) {
      return new ChecksumHashFunction(var0, Hashing.ChecksumType.access$200(var0), var1);
   }

   public static int consistentHash(HashCode var0, int var1) {
      return consistentHash(var0.padToLong(), var1);
   }

   public static int consistentHash(long var0, int var2) {
      Preconditions.checkArgument(var2 > 0, "buckets must be positive: %s", var2);
      Hashing.LinearCongruentialGenerator var3 = new Hashing.LinearCongruentialGenerator(var0);
      int var4 = 0;

      while(true) {
         int var5 = (int)((double)(var4 + 1) / var3.nextDouble());
         if (var5 < 0 || var5 >= var2) {
            return var4;
         }

         var4 = var5;
      }
   }

   public static HashCode combineOrdered(Iterable var0) {
      Iterator var1 = var0.iterator();
      Preconditions.checkArgument(var1.hasNext(), "Must be at least 1 hash code to combine.");
      int var2 = ((HashCode)var1.next()).bits();
      byte[] var3 = new byte[var2 / 8];
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         HashCode var5 = (HashCode)var4.next();
         byte[] var6 = var5.asBytes();
         Preconditions.checkArgument(var6.length == var3.length, "All hashcodes must have the same bit length.");

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var3[var7] = (byte)(var3[var7] * 37 ^ var6[var7]);
         }
      }

      return HashCode.fromBytesNoCopy(var3);
   }

   public static HashCode combineUnordered(Iterable var0) {
      Iterator var1 = var0.iterator();
      Preconditions.checkArgument(var1.hasNext(), "Must be at least 1 hash code to combine.");
      byte[] var2 = new byte[((HashCode)var1.next()).bits() / 8];
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         HashCode var4 = (HashCode)var3.next();
         byte[] var5 = var4.asBytes();
         Preconditions.checkArgument(var5.length == var2.length, "All hashcodes must have the same bit length.");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var2[var6] += var5[var6];
         }
      }

      return HashCode.fromBytesNoCopy(var2);
   }

   static int checkPositiveAndMakeMultipleOf32(int var0) {
      Preconditions.checkArgument(var0 > 0, "Number of bits must be positive");
      return var0 + 31 & -32;
   }

   private Hashing() {
   }

   static int access$000() {
      return GOOD_FAST_HASH_SEED;
   }

   static HashFunction access$100(Hashing.ChecksumType var0, String var1) {
      return checksumHashFunction(var0, var1);
   }

   private static final class LinearCongruentialGenerator {
      private long state;

      public LinearCongruentialGenerator(long var1) {
         this.state = var1;
      }

      public double nextDouble() {
         this.state = 2862933555777941757L * this.state + 1L;
         return (double)((int)(this.state >>> 33) + 1) / 2.147483648E9D;
      }
   }

   @VisibleForTesting
   static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
      private final int bits;

      ConcatenatedHashFunction(HashFunction... var1) {
         super(var1);
         int var2 = 0;
         HashFunction[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HashFunction var6 = var3[var5];
            var2 += var6.bits();
         }

         this.bits = var2;
      }

      HashCode makeHash(Hasher[] var1) {
         byte[] var2 = new byte[this.bits / 8];
         int var3 = 0;
         Hasher[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Hasher var7 = var4[var6];
            HashCode var8 = var7.hash();
            var3 += var8.writeBytesTo(var2, var3, var8.bits() / 8);
         }

         return HashCode.fromBytesNoCopy(var2);
      }

      public int bits() {
         return this.bits;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Hashing.ConcatenatedHashFunction) {
            Hashing.ConcatenatedHashFunction var2 = (Hashing.ConcatenatedHashFunction)var1;
            if (this.bits == var2.bits && this.functions.length == var2.functions.length) {
               for(int var3 = 0; var3 < this.functions.length; ++var3) {
                  if (!this.functions[var3].equals(var2.functions[var3])) {
                     return false;
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.bits;
         HashFunction[] var2 = this.functions;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HashFunction var5 = var2[var4];
            var1 ^= var5.hashCode();
         }

         return var1;
      }
   }

   static enum ChecksumType implements Supplier {
      CRC_32(32) {
         public Checksum get() {
            return new CRC32();
         }

         public Object get() {
            return this.get();
         }
      },
      ADLER_32(32) {
         public Checksum get() {
            return new Adler32();
         }

         public Object get() {
            return this.get();
         }
      };

      private final int bits;
      private static final Hashing.ChecksumType[] $VALUES = new Hashing.ChecksumType[]{CRC_32, ADLER_32};

      private ChecksumType(int var3) {
         this.bits = var3;
      }

      public abstract Checksum get();

      public Object get() {
         return this.get();
      }

      static int access$200(Hashing.ChecksumType var0) {
         return var0.bits;
      }

      ChecksumType(int var3, Object var4) {
         this(var3);
      }
   }

   private static class Adler32Holder {
      static final HashFunction ADLER_32;

      static {
         ADLER_32 = Hashing.access$100(Hashing.ChecksumType.ADLER_32, "Hashing.adler32()");
      }
   }

   private static class Crc32Holder {
      static final HashFunction CRC_32;

      static {
         CRC_32 = Hashing.access$100(Hashing.ChecksumType.CRC_32, "Hashing.crc32()");
      }
   }

   private static class Sha512Holder {
      static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
   }

   private static class Sha256Holder {
      static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
   }

   private static class Sha1Holder {
      static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
   }

   private static class Md5Holder {
      static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
   }

   private static class SipHash24Holder {
      static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
   }

   private static class Murmur3_128Holder {
      static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
      static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.access$000());
   }

   private static class Murmur3_32Holder {
      static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
      static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.access$000());
   }
}
