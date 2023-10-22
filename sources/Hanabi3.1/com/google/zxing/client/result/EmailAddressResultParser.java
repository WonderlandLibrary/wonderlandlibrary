package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Map;

public final class EmailAddressResultParser extends ResultParser {
   public EmailAddressParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      if (!rawText.startsWith("mailto:") && !rawText.startsWith("MAILTO:")) {
         return !EmailDoCoMoResultParser.isBasicallyValidEmailAddress(rawText) ? null : new EmailAddressParsedResult(rawText, (String)null, (String)null, "mailto:" + rawText);
      } else {
         String emailAddress = rawText.substring(7);
         int queryStart = emailAddress.indexOf(63);
         if (queryStart >= 0) {
            emailAddress = emailAddress.substring(0, queryStart);
         }

         Map nameValues = parseNameValuePairs(rawText);
         String subject = null;
         String body = null;
         if (nameValues != null) {
            if (emailAddress.length() == 0) {
               emailAddress = (String)nameValues.get("to");
            }

            subject = (String)nameValues.get("subject");
            body = (String)nameValues.get("body");
         }

         return new EmailAddressParsedResult(emailAddress, subject, body, rawText);
      }
   }
}
