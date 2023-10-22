package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class URLTOResultParser extends ResultParser {
   public URIParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("urlto:") && !rawText.startsWith("URLTO:")) {
         return null;
      } else {
         int titleEnd = rawText.indexOf(58, 6);
         if (titleEnd < 0) {
            return null;
         } else {
            String title = titleEnd <= 6 ? null : rawText.substring(6, titleEnd);
            String uri = rawText.substring(titleEnd + 1);
            return new URIParsedResult(uri, title);
         }
      }
   }
}
