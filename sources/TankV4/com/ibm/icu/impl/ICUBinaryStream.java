package com.ibm.icu.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class ICUBinaryStream extends DataInputStream {
   public ICUBinaryStream(InputStream var1, int var2) {
      super(var1);
      this.mark(var2);
   }

   public ICUBinaryStream(byte[] var1) {
      this(new ByteArrayInputStream(var1), var1.length);
   }

   public void seek(int var1) throws IOException {
      this.reset();
      int var2 = this.skipBytes(var1);
      if (var2 != var1) {
         throw new IllegalStateException("Skip(" + var1 + ") only skipped " + var2 + " bytes");
      }
   }
}
