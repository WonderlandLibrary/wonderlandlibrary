package com.ibm.icu.text;

import java.text.ParseException;

public class StringPrepParseException extends ParseException {
   static final long serialVersionUID = 7160264827701651255L;
   public static final int INVALID_CHAR_FOUND = 0;
   public static final int ILLEGAL_CHAR_FOUND = 1;
   public static final int PROHIBITED_ERROR = 2;
   public static final int UNASSIGNED_ERROR = 3;
   public static final int CHECK_BIDI_ERROR = 4;
   public static final int STD3_ASCII_RULES_ERROR = 5;
   public static final int ACE_PREFIX_ERROR = 6;
   public static final int VERIFICATION_ERROR = 7;
   public static final int LABEL_TOO_LONG_ERROR = 8;
   public static final int BUFFER_OVERFLOW_ERROR = 9;
   public static final int ZERO_LENGTH_LABEL = 10;
   public static final int DOMAIN_NAME_TOO_LONG_ERROR = 11;
   private int error;
   private int line;
   private StringBuffer preContext = new StringBuffer();
   private StringBuffer postContext = new StringBuffer();
   private static final int PARSE_CONTEXT_LEN = 16;
   static final boolean $assertionsDisabled = !StringPrepParseException.class.desiredAssertionStatus();

   public StringPrepParseException(String var1, int var2) {
      super(var1, -1);
      this.error = var2;
      this.line = 0;
   }

   public StringPrepParseException(String var1, int var2, String var3, int var4) {
      super(var1, -1);
      this.error = var2;
      this.setContext(var3, var4);
      this.line = 0;
   }

   public StringPrepParseException(String var1, int var2, String var3, int var4, int var5) {
      super(var1, -1);
      this.error = var2;
      this.setContext(var3, var4);
      this.line = var5;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof StringPrepParseException)) {
         return false;
      } else {
         return ((StringPrepParseException)var1).error == this.error;
      }
   }

   /** @deprecated */
   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getMessage());
      var1.append(". line:  ");
      var1.append(this.line);
      var1.append(". preContext:  ");
      var1.append(this.preContext);
      var1.append(". postContext: ");
      var1.append(this.postContext);
      var1.append("\n");
      return var1.toString();
   }

   private void setPreContext(String var1, int var2) {
      this.setPreContext(var1.toCharArray(), var2);
   }

   private void setPreContext(char[] var1, int var2) {
      int var3 = var2 <= 16 ? 0 : var2 - 15;
      int var4 = var3 <= 16 ? var3 : 16;
      this.preContext.append(var1, var3, var4);
   }

   private void setPostContext(String var1, int var2) {
      this.setPostContext(var1.toCharArray(), var2);
   }

   private void setPostContext(char[] var1, int var2) {
      int var4 = var1.length - var2;
      this.postContext.append(var1, var2, var4);
   }

   private void setContext(String var1, int var2) {
      this.setPreContext(var1, var2);
      this.setPostContext(var1, var2);
   }

   public int getError() {
      return this.error;
   }
}
