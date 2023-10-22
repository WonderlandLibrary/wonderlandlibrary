package com.google.zxing.client.result;

public final class TextParsedResult extends ParsedResult {
   private final String text;
   private final String language;

   public TextParsedResult(String text, String language) {
      super(ParsedResultType.TEXT);
      this.text = text;
      this.language = language;
   }

   public String getText() {
      return this.text;
   }

   public String getLanguage() {
      return this.language;
   }

   public String getDisplayResult() {
      return this.text;
   }
}
