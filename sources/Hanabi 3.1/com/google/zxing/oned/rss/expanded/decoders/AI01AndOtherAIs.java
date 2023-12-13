package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class AI01AndOtherAIs extends AI01decoder {
   private static final int HEADER_SIZE = 4;

   AI01AndOtherAIs(BitArray information) {
      super(information);
   }

   public String parseInformation() throws NotFoundException {
      StringBuilder buff = new StringBuilder();
      buff.append("(01)");
      int initialGtinPosition = buff.length();
      int firstGtinDigit = this.getGeneralDecoder().extractNumericValueFromBitArray(4, 4);
      buff.append(firstGtinDigit);
      this.encodeCompressedGtinWithoutAI(buff, 8, initialGtinPosition);
      return this.getGeneralDecoder().decodeAllCodes(buff, 48);
   }
}
