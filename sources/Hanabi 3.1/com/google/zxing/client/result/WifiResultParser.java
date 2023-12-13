package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class WifiResultParser extends ResultParser {
   public WifiParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("WIFI:")) {
         return null;
      } else {
         String ssid = matchSinglePrefixedField("S:", rawText, ';', false);
         if (ssid != null && ssid.length() != 0) {
            String pass = matchSinglePrefixedField("P:", rawText, ';', false);
            String type = matchSinglePrefixedField("T:", rawText, ';', false);
            if (type == null) {
               type = "nopass";
            }

            boolean hidden = Boolean.parseBoolean(matchSinglePrefixedField("B:", rawText, ';', false));
            return new WifiParsedResult(type, ssid, pass, hidden);
         } else {
            return null;
         }
      }
   }
}
