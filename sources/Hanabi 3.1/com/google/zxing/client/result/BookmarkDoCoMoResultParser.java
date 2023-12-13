package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
   public URIParsedResult parse(Result result) {
      String rawText = result.getText();
      if (!rawText.startsWith("MEBKM:")) {
         return null;
      } else {
         String title = matchSingleDoCoMoPrefixedField("TITLE:", rawText, true);
         String[] rawUri = matchDoCoMoPrefixedField("URL:", rawText, true);
         if (rawUri == null) {
            return null;
         } else {
            String uri = rawUri[0];
            return URIResultParser.isBasicallyValidURI(uri) ? new URIParsedResult(uri, title) : null;
         }
      }
   }
}
