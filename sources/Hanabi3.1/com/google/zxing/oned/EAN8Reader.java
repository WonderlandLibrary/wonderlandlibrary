package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class EAN8Reader extends UPCEANReader {
   private final int[] decodeMiddleCounters = new int[4];

   protected int decodeMiddle(BitArray row, int[] startRange, StringBuilder result) throws NotFoundException {
      int[] counters = this.decodeMiddleCounters;
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      int end = row.getSize();
      int rowOffset = startRange[1];

      int x;
      int len$;
      int i$;
      for(int x = 0; x < 4 && rowOffset < end; ++x) {
         x = decodeDigit(row, counters, rowOffset, L_PATTERNS);
         result.append((char)(48 + x));
         int[] arr$ = counters;
         int len$ = counters.length;

         for(len$ = 0; len$ < len$; ++len$) {
            i$ = arr$[len$];
            rowOffset += i$;
         }
      }

      int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
      rowOffset = middleRange[1];

      for(x = 0; x < 4 && rowOffset < end; ++x) {
         int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
         result.append((char)(48 + bestMatch));
         int[] arr$ = counters;
         len$ = counters.length;

         for(i$ = 0; i$ < len$; ++i$) {
            int counter = arr$[i$];
            rowOffset += counter;
         }
      }

      return rowOffset;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.EAN_8;
   }
}
