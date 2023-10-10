package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class CoderBase {
   private final Class[] acceptableOptions;
   private static final byte[] NONE = new byte[0];

   protected CoderBase(Class... var1) {
      this.acceptableOptions = var1;
   }

   boolean canAcceptOptions(Object var1) {
      Class[] var2 = this.acceptableOptions;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (var5.isInstance(var1)) {
            return true;
         }
      }

      return false;
   }

   byte[] getOptionsAsProperties(Object var1) {
      return NONE;
   }

   Object getOptionsFromCoder(Coder var1, InputStream var2) {
      return null;
   }

   abstract InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException;

   OutputStream encode(OutputStream var1, Object var2) throws IOException {
      throw new UnsupportedOperationException("method doesn't support writing");
   }

   protected static int numberOptionOrDefault(Object var0, int var1) {
      return var0 instanceof Number ? ((Number)var0).intValue() : var1;
   }
}
