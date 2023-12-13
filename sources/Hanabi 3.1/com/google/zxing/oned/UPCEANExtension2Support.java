package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.EnumMap;
import java.util.Map;

final class UPCEANExtension2Support {
   private final int[] decodeMiddleCounters = new int[4];
   private final StringBuilder decodeRowStringBuffer = new StringBuilder();

   Result decodeRow(int rowNumber, BitArray row, int[] extensionStartRange) throws NotFoundException {
      StringBuilder result = this.decodeRowStringBuffer;
      result.setLength(0);
      int end = this.decodeMiddle(row, extensionStartRange, result);
      String resultString = result.toString();
      Map extensionData = parseExtensionString(resultString);
      Result extensionResult = new Result(resultString, (byte[])null, new ResultPoint[]{new ResultPoint((float)(extensionStartRange[0] + extensionStartRange[1]) / 2.0F, (float)rowNumber), new ResultPoint((float)end, (float)rowNumber)}, BarcodeFormat.UPC_EAN_EXTENSION);
      if (extensionData != null) {
         extensionResult.putAllMetadata(extensionData);
      }

      return extensionResult;
   }

   int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
      int[] counters = this.decodeMiddleCounters;
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      int end = row.getSize();
      int rowOffset = startRange[1];
      int checkParity = 0;

      for(int x = 0; x < 2 && rowOffset < end; ++x) {
         int bestMatch = UPCEANReader.decodeDigit(row, counters, rowOffset, UPCEANReader.L_AND_G_PATTERNS);
         resultString.append((char)(48 + bestMatch % 10));
         int[] arr$ = counters;
         int len$ = counters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int counter = arr$[i$];
            rowOffset += counter;
         }

         if (bestMatch >= 10) {
            checkParity |= 1 << 1 - x;
         }

         if (x != 1) {
            rowOffset = row.getNextSet(rowOffset);
            rowOffset = row.getNextUnset(rowOffset);
         }
      }

      if (resultString.length() != 2) {
         throw NotFoundException.getNotFoundInstance();
      } else if (Integer.parseInt(resultString.toString()) % 4 != checkParity) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return rowOffset;
      }
   }

   private static Map parseExtensionString(String raw) {
      if (raw.length() != 2) {
         return null;
      } else {
         Map result = new EnumMap(ResultMetadataType.class);
         result.put(ResultMetadataType.ISSUE_NUMBER, Integer.valueOf(raw));
         return result;
      }
   }
}
