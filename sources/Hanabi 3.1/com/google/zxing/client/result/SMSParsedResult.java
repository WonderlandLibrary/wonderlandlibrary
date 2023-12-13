package com.google.zxing.client.result;

public final class SMSParsedResult extends ParsedResult {
   private final String[] numbers;
   private final String[] vias;
   private final String subject;
   private final String body;

   public SMSParsedResult(String number, String via, String subject, String body) {
      super(ParsedResultType.SMS);
      this.numbers = new String[]{number};
      this.vias = new String[]{via};
      this.subject = subject;
      this.body = body;
   }

   public SMSParsedResult(String[] numbers, String[] vias, String subject, String body) {
      super(ParsedResultType.SMS);
      this.numbers = numbers;
      this.vias = vias;
      this.subject = subject;
      this.body = body;
   }

   public String getSMSURI() {
      StringBuilder result = new StringBuilder();
      result.append("sms:");
      boolean first = true;

      for(int i = 0; i < this.numbers.length; ++i) {
         if (first) {
            first = false;
         } else {
            result.append(',');
         }

         result.append(this.numbers[i]);
         if (this.vias != null && this.vias[i] != null) {
            result.append(";via=");
            result.append(this.vias[i]);
         }
      }

      boolean hasBody = this.body != null;
      boolean hasSubject = this.subject != null;
      if (hasBody || hasSubject) {
         result.append('?');
         if (hasBody) {
            result.append("body=");
            result.append(this.body);
         }

         if (hasSubject) {
            if (hasBody) {
               result.append('&');
            }

            result.append("subject=");
            result.append(this.subject);
         }
      }

      return result.toString();
   }

   public String[] getNumbers() {
      return this.numbers;
   }

   public String[] getVias() {
      return this.vias;
   }

   public String getSubject() {
      return this.subject;
   }

   public String getBody() {
      return this.body;
   }

   public String getDisplayResult() {
      StringBuilder result = new StringBuilder(100);
      maybeAppend(this.numbers, result);
      maybeAppend(this.subject, result);
      maybeAppend(this.body, result);
      return result.toString();
   }
}
