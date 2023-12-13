package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class UPCAWriter implements Writer {
   private final EAN13Writer subWriter = new EAN13Writer();

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
      return this.encode(contents, format, width, height, (Map)null);
   }

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (format != BarcodeFormat.UPC_A) {
         throw new IllegalArgumentException("Can only encode UPC-A, but got " + format);
      } else {
         return this.subWriter.encode(preencode(contents), BarcodeFormat.EAN_13, width, height, hints);
      }
   }

   private static String preencode(String contents) {
      int length = contents.length();
      if (length == 11) {
         int sum = 0;

         for(int i = 0; i < 11; ++i) {
            sum += (contents.charAt(i) - 48) * (i % 2 == 0 ? 3 : 1);
         }

         contents = contents + (1000 - sum) % 10;
      } else if (length != 12) {
         throw new IllegalArgumentException("Requested contents should be 11 or 12 digits long, but got " + contents.length());
      }

      return '0' + contents;
   }
}
