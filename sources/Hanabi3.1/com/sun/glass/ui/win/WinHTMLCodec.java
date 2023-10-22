package com.sun.glass.ui.win;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class WinHTMLCodec {
   public static final String defaultCharset = "UTF-8";

   public static byte[] encode(byte[] var0) {
      return HTMLCodec.convertToHTMLFormat(var0);
   }

   public static byte[] decode(byte[] var0) {
      try {
         ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
         HTMLCodec var2 = new HTMLCodec(var1, EHTMLReadMode.HTML_READ_SELECTION);
         ByteArrayOutputStream var3 = new ByteArrayOutputStream(var0.length);

         int var4;
         while((var4 = var2.read()) != -1) {
            var3.write(var4);
         }

         return var3.toByteArray();
      } catch (IOException var5) {
         throw new RuntimeException("Unexpected IOException caught", var5);
      }
   }
}
