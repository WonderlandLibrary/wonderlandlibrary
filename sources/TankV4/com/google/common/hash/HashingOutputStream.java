package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Beta
public final class HashingOutputStream extends FilterOutputStream {
   private final Hasher hasher;

   public HashingOutputStream(HashFunction var1, OutputStream var2) {
      super((OutputStream)Preconditions.checkNotNull(var2));
      this.hasher = (Hasher)Preconditions.checkNotNull(var1.newHasher());
   }

   public void write(int var1) throws IOException {
      this.hasher.putByte((byte)var1);
      this.out.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.hasher.putBytes(var1, var2, var3);
      this.out.write(var1, var2, var3);
   }

   public HashCode hash() {
      return this.hasher.hash();
   }

   public void close() throws IOException {
      this.out.close();
   }
}
