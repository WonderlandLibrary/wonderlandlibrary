package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class LEInputStream {
   public final InputStream in;

   LEInputStream(InputStream var1) {
      this.in = var1;
   }

   public final short readShort() throws IOException {
      int var1 = this.in.read();
      int var2 = this.in.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (short)((var2 << 8) + var1);
      }
   }

   public final int readInt() throws IOException {
      int var1 = this.in.read();
      int var2 = this.in.read();
      int var3 = this.in.read();
      int var4 = this.in.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var4 << 24) + (var3 << 16) + (var2 << 8) + var1;
      }
   }

   public final void skipBytes(int var1) throws IOException {
      ImageTools.skipFully(this.in, (long)var1);
   }
}
