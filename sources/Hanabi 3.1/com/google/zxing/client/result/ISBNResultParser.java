package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public final class ISBNResultParser extends ResultParser {
   public ISBNParsedResult parse(Result result) {
      BarcodeFormat format = result.getBarcodeFormat();
      if (format != BarcodeFormat.EAN_13) {
         return null;
      } else {
         String rawText = getMassagedText(result);
         int length = rawText.length();
         if (length != 13) {
            return null;
         } else {
            return !rawText.startsWith("978") && !rawText.startsWith("979") ? null : new ISBNParsedResult(rawText);
         }
      }
   }
}
