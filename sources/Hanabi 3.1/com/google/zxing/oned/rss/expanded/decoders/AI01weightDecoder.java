package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

abstract class AI01weightDecoder extends AI01decoder {
   AI01weightDecoder(BitArray information) {
      super(information);
   }

   protected final void encodeCompressedWeight(StringBuilder buf, int currentPos, int weightSize) {
      int originalWeightNumeric = this.getGeneralDecoder().extractNumericValueFromBitArray(currentPos, weightSize);
      this.addWeightCode(buf, originalWeightNumeric);
      int weightNumeric = this.checkWeight(originalWeightNumeric);
      int currentDivisor = 100000;

      for(int i = 0; i < 5; ++i) {
         if (weightNumeric / currentDivisor == 0) {
            buf.append('0');
         }

         currentDivisor /= 10;
      }

      buf.append(weightNumeric);
   }

   protected abstract void addWeightCode(StringBuilder var1, int var2);

   protected abstract int checkWeight(int var1);
}
