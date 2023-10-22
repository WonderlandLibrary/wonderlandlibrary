package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class EAN8Writer extends UPCEANWriter {
   private static final int CODE_WIDTH = 67;

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (format != BarcodeFormat.EAN_8) {
         throw new IllegalArgumentException("Can only encode EAN_8, but got " + format);
      } else {
         return super.encode(contents, format, width, height, hints);
      }
   }

   public boolean[] encode(String contents) {
      if (contents.length() != 8) {
         throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + contents.length());
      } else {
         boolean[] result = new boolean[67];
         int pos = 0;
         pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);

         int i;
         int digit;
         for(i = 0; i <= 3; ++i) {
            digit = Integer.parseInt(contents.substring(i, i + 1));
            pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], false);
         }

         pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, false);

         for(i = 4; i <= 7; ++i) {
            digit = Integer.parseInt(contents.substring(i, i + 1));
            pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], true);
         }

         pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);
         return result;
      }
   }
}
