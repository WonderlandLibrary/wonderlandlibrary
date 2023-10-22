package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
   public AddressBookParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("MECARD:")) {
         return null;
      } else {
         String[] rawName = matchDoCoMoPrefixedField("N:", rawText, true);
         if (rawName == null) {
            return null;
         } else {
            String name = parseName(rawName[0]);
            String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawText, true);
            String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawText, true);
            String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawText, true);
            String note = matchSingleDoCoMoPrefixedField("NOTE:", rawText, false);
            String[] addresses = matchDoCoMoPrefixedField("ADR:", rawText, true);
            String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawText, true);
            if (birthday != null && !isStringOfDigits(birthday, 8)) {
               birthday = null;
            }

            String url = matchSingleDoCoMoPrefixedField("URL:", rawText, true);
            String org = matchSingleDoCoMoPrefixedField("ORG:", rawText, true);
            return new AddressBookParsedResult(maybeWrap(name), pronunciation, phoneNumbers, (String[])null, emails, (String[])null, (String)null, note, addresses, (String[])null, org, birthday, (String)null, url);
         }
      }
   }

   private static String parseName(String name) {
      int comma = name.indexOf(44);
      return comma >= 0 ? name.substring(comma + 1) + ' ' + name.substring(0, comma) : name;
   }
}
