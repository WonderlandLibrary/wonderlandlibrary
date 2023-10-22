package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.io.IOException;
import java.security.AccessController;

class DFontDecoder extends FontFileWriter {
   private static native long createCTFont(String var0);

   private static native void releaseCTFont(long var0);

   private static native int getCTFontFormat(long var0);

   private static native int[] getCTFontTags(long var0);

   private static native byte[] getCTFontTable(long var0, int var2);

   public DFontDecoder() {
   }

   public void decode(String var1) throws IOException {
      if (var1 == null) {
         throw new IOException("Invalid font name");
      } else {
         long var2 = 0L;

         try {
            var2 = createCTFont(var1);
            if (var2 == 0L) {
               throw new IOException("Failure creating CTFont");
            }

            int var4 = getCTFontFormat(var2);
            if (var4 != 1953658213 && var4 != 65536 && var4 != 1330926671) {
               throw new IOException("Unsupported Dfont");
            }

            int[] var5 = getCTFontTags(var2);
            short var6 = (short)var5.length;
            int var7 = 12 + 16 * var6;
            byte[][] var8 = new byte[var6][];

            int var9;
            int var10;
            int var11;
            for(var9 = 0; var9 < var5.length; ++var9) {
               var10 = var5[var9];
               var8[var9] = getCTFontTable(var2, var10);
               var11 = var8[var9].length;
               var7 += var11 + 3 & -4;
            }

            releaseCTFont(var2);
            var2 = 0L;
            this.setLength(var7);
            this.writeHeader(var4, var6);
            var9 = 12 + 16 * var6;

            for(var10 = 0; var10 < var6; ++var10) {
               var11 = var5[var10];
               byte[] var12 = var8[var10];
               this.writeDirectoryEntry(var10, var11, 0, var9, var12.length);
               this.seek(var9);
               this.writeBytes(var12);
               var9 += var12.length + 3 & -4;
            }
         } finally {
            if (var2 != 0L) {
               releaseCTFont(var2);
            }

         }

      }
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         return null;
      });
   }
}
