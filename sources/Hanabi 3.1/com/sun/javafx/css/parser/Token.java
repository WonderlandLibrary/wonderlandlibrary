package com.sun.javafx.css.parser;

final class Token {
   static final int EOF = -1;
   static final int INVALID = 0;
   static final int SKIP = 1;
   static final Token EOF_TOKEN = new Token(-1, "EOF");
   static final Token INVALID_TOKEN = new Token(0, "INVALID");
   static final Token SKIP_TOKEN = new Token(1, "SKIP");
   private final String text;
   private int offset;
   private int line;
   private final int type;

   Token(int var1, String var2, int var3, int var4) {
      this.type = var1;
      this.text = var2;
      this.line = var3;
      this.offset = var4;
   }

   Token(int var1, String var2) {
      this(var1, var2, -1, -1);
   }

   Token(int var1) {
      this(var1, (String)null);
   }

   private Token() {
      this(0, "INVALID");
   }

   String getText() {
      return this.text;
   }

   int getType() {
      return this.type;
   }

   int getLine() {
      return this.line;
   }

   void setLine(int var1) {
      this.line = var1;
   }

   int getOffset() {
      return this.offset;
   }

   void setOffset(int var1) {
      this.offset = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[').append(this.line).append(',').append(this.offset).append(']').append(',').append(this.text).append(",<").append(this.type).append('>');
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Token var2 = (Token)var1;
         if (this.type != var2.type) {
            return false;
         } else {
            if (this.text == null) {
               if (var2.text != null) {
                  return false;
               }
            } else if (!this.text.equals(var2.text)) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 67 * var1 + this.type;
      var1 = 67 * var1 + (this.text != null ? this.text.hashCode() : 0);
      return var1;
   }
}
