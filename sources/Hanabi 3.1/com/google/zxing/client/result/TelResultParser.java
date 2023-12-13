package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class TelResultParser extends ResultParser {
   public TelParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("tel:") && !rawText.startsWith("TEL:")) {
         return null;
      } else {
         String telURI = rawText.startsWith("TEL:") ? "tel:" + rawText.substring(4) : rawText;
         int queryStart = rawText.indexOf(63, 4);
         String number = queryStart < 0 ? rawText.substring(4) : rawText.substring(4, queryStart);
         return new TelParsedResult(number, telURI, (String)null);
      }
   }
}
