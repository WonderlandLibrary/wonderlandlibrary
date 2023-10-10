package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.zip.Checksum;

final class ChecksumHashFunction extends AbstractStreamingHashFunction implements Serializable {
   private final Supplier checksumSupplier;
   private final int bits;
   private final String toString;
   private static final long serialVersionUID = 0L;

   ChecksumHashFunction(Supplier var1, int var2, String var3) {
      this.checksumSupplier = (Supplier)Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(var2 == 32 || var2 == 64, "bits (%s) must be either 32 or 64", var2);
      this.bits = var2;
      this.toString = (String)Preconditions.checkNotNull(var3);
   }

   public int bits() {
      return this.bits;
   }

   public Hasher newHasher() {
      return new ChecksumHashFunction.ChecksumHasher(this, (Checksum)this.checksumSupplier.get());
   }

   public String toString() {
      return this.toString;
   }

   static int access$100(ChecksumHashFunction var0) {
      return var0.bits;
   }

   private final class ChecksumHasher extends AbstractByteHasher {
      private final Checksum checksum;
      final ChecksumHashFunction this$0;

      private ChecksumHasher(ChecksumHashFunction var1, Checksum var2) {
         this.this$0 = var1;
         this.checksum = (Checksum)Preconditions.checkNotNull(var2);
      }

      protected void update(byte var1) {
         this.checksum.update(var1);
      }

      protected void update(byte[] var1, int var2, int var3) {
         this.checksum.update(var1, var2, var3);
      }

      public HashCode hash() {
         long var1 = this.checksum.getValue();
         return ChecksumHashFunction.access$100(this.this$0) == 32 ? HashCode.fromInt((int)var1) : HashCode.fromLong(var1);
      }

      ChecksumHasher(ChecksumHashFunction var1, Checksum var2, Object var3) {
         this(var1, var2);
      }
   }
}
