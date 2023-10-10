package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class HashingInputStream extends FilterInputStream {
   private final Hasher hasher;

   public HashingInputStream(HashFunction var1, InputStream var2) {
      super((InputStream)Preconditions.checkNotNull(var2));
      this.hasher = (Hasher)Preconditions.checkNotNull(var1.newHasher());
   }

   public int read() throws IOException {
      int var1 = this.in.read();
      if (var1 != -1) {
         this.hasher.putByte((byte)var1);
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      if (var4 != -1) {
         this.hasher.putBytes(var1, var2, var4);
      }

      return var4;
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int var1) {
   }

   public void reset() throws IOException {
      throw new IOException("reset not supported");
   }

   public HashCode hash() {
      return this.hasher.hash();
   }
}
