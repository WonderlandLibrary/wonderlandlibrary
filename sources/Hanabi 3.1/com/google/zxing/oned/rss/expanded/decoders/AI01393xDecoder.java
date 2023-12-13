package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI01393xDecoder extends AI01decoder {
   private static final int HEADER_SIZE = 8;
   private static final int LAST_DIGIT_SIZE = 2;
   private static final int FIRST_THREE_DIGITS_SIZE = 10;

   AI01393xDecoder(BitArray information) {
      super(information);
   }

   public String parseInformation() throws NotFoundException {
      if (this.getInformation().getSize() < 48) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder buf = new StringBuilder();
         this.encodeCompressedGtin(buf, 8);
         int lastAIdigit = this.getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
         buf.append("(393");
         buf.append(lastAIdigit);
         buf.append(')');
         int firstThreeDigits = this.getGeneralDecoder().extractNumericValueFromBitArray(50, 10);
         if (firstThreeDigits / 100 == 0) {
            buf.append('0');
         }

         if (firstThreeDigits / 10 == 0) {
            buf.append('0');
         }

         buf.append(firstThreeDigits);
         DecodedInformation generalInformation = this.getGeneralDecoder().decodeGeneralPurposeField(60, (String)null);
         buf.append(generalInformation.getNewString());
         return buf.toString();
      }
   }
}
