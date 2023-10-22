package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI01392xDecoder extends AI01decoder {
   private static final int HEADER_SIZE = 8;
   private static final int LAST_DIGIT_SIZE = 2;

   AI01392xDecoder(BitArray information) {
      super(information);
   }

   public String parseInformation() throws NotFoundException {
      if (this.getInformation().getSize() < 48) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder buf = new StringBuilder();
         this.encodeCompressedGtin(buf, 8);
         int lastAIdigit = this.getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
         buf.append("(392");
         buf.append(lastAIdigit);
         buf.append(')');
         DecodedInformation decodedInformation = this.getGeneralDecoder().decodeGeneralPurposeField(50, (String)null);
         buf.append(decodedInformation.getNewString());
         return buf.toString();
      }
   }
}
