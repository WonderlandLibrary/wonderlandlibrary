package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;

public final class AddressBookAUResultParser extends ResultParser {
   public AddressBookParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (rawText.contains("MEMORY") && rawText.contains("\r\n")) {
         String name = matchSinglePrefixedField("NAME1:", rawText, '\r', true);
         String pronunciation = matchSinglePrefixedField("NAME2:", rawText, '\r', true);
         String[] phoneNumbers = matchMultipleValuePrefix("TEL", 3, rawText, true);
         String[] emails = matchMultipleValuePrefix("MAIL", 3, rawText, true);
         String note = matchSinglePrefixedField("MEMORY:", rawText, '\r', false);
         String address = matchSinglePrefixedField("ADD:", rawText, '\r', true);
         String[] addresses = address == null ? null : new String[]{address};
         return new AddressBookParsedResult(maybeWrap(name), pronunciation, phoneNumbers, (String[])null, emails, (String[])null, (String)null, note, addresses, (String[])null, (String)null, (String)null, (String)null, (String)null);
      } else {
         return null;
      }
   }

   private static String[] matchMultipleValuePrefix(String prefix, int max, String rawText, boolean trim) {
      List values = null;

      for(int i = 1; i <= max; ++i) {
         String value = matchSinglePrefixedField(prefix + i + ':', rawText, '\r', trim);
         if (value == null) {
            break;
         }

         if (values == null) {
            values = new ArrayList(max);
         }

         values.add(value);
      }

      return values == null ? null : (String[])values.toArray(new String[values.size()]);
   }
}
