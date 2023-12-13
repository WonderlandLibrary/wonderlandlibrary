package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2Options;

class LZMA2Decoder extends CoderBase {
   LZMA2Decoder() {
      super(LZMA2Options.class, Number.class);
   }

   InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
      try {
         int var4 = this.getDictionarySize(var2);
         return new LZMA2InputStream(var1, var4);
      } catch (IllegalArgumentException var5) {
         throw new IOException(var5.getMessage());
      }
   }

   OutputStream encode(OutputStream var1, Object var2) throws IOException {
      LZMA2Options var3 = this.getOptions(var2);
      FinishableWrapperOutputStream var4 = new FinishableWrapperOutputStream(var1);
      return var3.getOutputStream(var4);
   }

   byte[] getOptionsAsProperties(Object var1) {
      int var2 = this.getDictSize(var1);
      int var3 = Integer.numberOfLeadingZeros(var2);
      int var4 = (var2 >>> 30 - var3) - 2;
      return new byte[]{(byte)((19 - var3) * 2 + var4)};
   }

   Object getOptionsFromCoder(Coder var1, InputStream var2) {
      return this.getDictionarySize(var1);
   }

   private int getDictSize(Object var1) {
      return var1 instanceof LZMA2Options ? ((LZMA2Options)var1).getDictSize() : this.numberOptionOrDefault(var1);
   }

   private int getDictionarySize(Coder var1) throws IllegalArgumentException {
      int var2 = 255 & var1.properties[0];
      if ((var2 & -64) != 0) {
         throw new IllegalArgumentException("Unsupported LZMA2 property bits");
      } else if (var2 > 40) {
         throw new IllegalArgumentException("Dictionary larger than 4GiB maximum size");
      } else {
         return var2 == 40 ? -1 : (2 | var2 & 1) << var2 / 2 + 11;
      }
   }

   private LZMA2Options getOptions(Object var1) throws IOException {
      if (var1 instanceof LZMA2Options) {
         return (LZMA2Options)var1;
      } else {
         LZMA2Options var2 = new LZMA2Options();
         var2.setDictSize(this.numberOptionOrDefault(var1));
         return var2;
      }
   }

   private int numberOptionOrDefault(Object var1) {
      return numberOptionOrDefault(var1, 8388608);
   }
}
