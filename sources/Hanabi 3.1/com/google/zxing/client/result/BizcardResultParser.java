package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;

public final class BizcardResultParser extends AbstractDoCoMoResultParser {
   public AddressBookParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("BIZCARD:")) {
         return null;
      } else {
         String firstName = matchSingleDoCoMoPrefixedField("N:", rawText, true);
         String lastName = matchSingleDoCoMoPrefixedField("X:", rawText, true);
         String fullName = buildName(firstName, lastName);
         String title = matchSingleDoCoMoPrefixedField("T:", rawText, true);
         String org = matchSingleDoCoMoPrefixedField("C:", rawText, true);
         String[] addresses = matchDoCoMoPrefixedField("A:", rawText, true);
         String phoneNumber1 = matchSingleDoCoMoPrefixedField("B:", rawText, true);
         String phoneNumber2 = matchSingleDoCoMoPrefixedField("M:", rawText, true);
         String phoneNumber3 = matchSingleDoCoMoPrefixedField("F:", rawText, true);
         String email = matchSingleDoCoMoPrefixedField("E:", rawText, true);
         return new AddressBookParsedResult(maybeWrap(fullName), (String)null, buildPhoneNumbers(phoneNumber1, phoneNumber2, phoneNumber3), (String[])null, maybeWrap(email), (String[])null, (String)null, (String)null, addresses, (String[])null, org, (String)null, title, (String)null);
      }
   }

   private static String[] buildPhoneNumbers(String number1, String number2, String number3) {
      List numbers = new ArrayList(3);
      if (number1 != null) {
         numbers.add(number1);
      }

      if (number2 != null) {
         numbers.add(number2);
      }

      if (number3 != null) {
         numbers.add(number3);
      }

      int size = numbers.size();
      return size == 0 ? null : (String[])numbers.toArray(new String[size]);
   }

   private static String buildName(String firstName, String lastName) {
      if (firstName == null) {
         return lastName;
      } else {
         return lastName == null ? firstName : firstName + ' ' + lastName;
      }
   }
}
