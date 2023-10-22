package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class EAN13Writer extends UPCEANWriter {
   private static final int CODE_WIDTH = 95;

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (format != BarcodeFormat.EAN_13) {
         throw new IllegalArgumentException("Can only encode EAN_13, but got " + format);
      } else {
         return super.encode(contents, format, width, height, hints);
      }
   }

   public boolean[] encode(String contents) {
      if (contents.length() != 13) {
         throw new IllegalArgumentException("Requested contents should be 13 digits long, but got " + contents.length());
      } else {
         try {
            if (!UPCEANReader.checkStandardUPCEANChecksum(contents)) {
               throw new IllegalArgumentException("Contents do not pass checksum");
            }
         } catch (FormatException var8) {
            throw new IllegalArgumentException("Illegal contents");
         }

         int firstDigit = Integer.parseInt(contents.substring(0, 1));
         int parities = EAN13Reader.FIRST_DIGIT_ENCODINGS[firstDigit];
         boolean[] result = new boolean[95];
         int pos = 0;
         pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);

         int i;
         int digit;
         for(i = 1; i <= 6; ++i) {
            digit = Integer.parseInt(contents.substring(i, i + 1));
            if ((parities >> 6 - i & 1) == 1) {
               digit += 10;
            }

            pos += appendPattern(result, pos, UPCEANReader.L_AND_G_PATTERNS[digit], false);
         }

         pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, false);

         for(i = 7; i <= 12; ++i) {
            digit = Integer.parseInt(contents.substring(i, i + 1));
            pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], true);
         }

         pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);
         return result;
      }
   }
}
