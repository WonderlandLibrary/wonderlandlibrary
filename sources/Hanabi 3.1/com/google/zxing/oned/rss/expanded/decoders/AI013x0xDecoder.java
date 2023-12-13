package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

abstract class AI013x0xDecoder extends AI01weightDecoder {
   private static final int HEADER_SIZE = 5;
   private static final int WEIGHT_SIZE = 15;

   AI013x0xDecoder(BitArray information) {
      super(information);
   }

   public String parseInformation() throws NotFoundException {
      if (this.getInformation().getSize() != 60) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuilder buf = new StringBuilder();
         this.encodeCompressedGtin(buf, 5);
         this.encodeCompressedWeight(buf, 45, 15);
         return buf.toString();
      }
   }
}
