package com.mojang.brigadier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class StringReader implements ImmutableStringReader {
   private static final char SYNTAX_ESCAPE = '\\';
   private static final char SYNTAX_DOUBLE_QUOTE = '"';
   private static final char SYNTAX_SINGLE_QUOTE = '\'';
   private final String string;
   private int cursor;

   public StringReader(StringReader other) {
      this.string = other.string;
      this.cursor = other.cursor;
   }

   public StringReader(String string) {
      this.string = string;
   }

   public String getString() {
      return this.string;
   }

   public void setCursor(int cursor) {
      this.cursor = cursor;
   }

   public int getRemainingLength() {
      return this.string.length() - this.cursor;
   }

   public int getTotalLength() {
      return this.string.length();
   }

   public int getCursor() {
      return this.cursor;
   }

   public String getRead() {
      return this.string.substring(0, this.cursor);
   }

   public String getRemaining() {
      return this.string.substring(this.cursor);
   }

   public boolean canRead(int length) {
      return this.cursor + length <= this.string.length();
   }

   public boolean canRead() {
      return this.canRead(1);
   }

   public char peek() {
      return this.string.charAt(this.cursor);
   }

   public char peek(int offset) {
      return this.string.charAt(this.cursor + offset);
   }

   public char read() {
      return this.string.charAt(this.cursor++);
   }

   public void skip() {
      ++this.cursor;
   }

   public static boolean isAllowedNumber(char c) {
      return c >= '0' && c <= '9' || c == '.' || c == '-';
   }

   public static boolean isQuotedStringStart(char c) {
      return c == '"' || c == '\'';
   }

   public void skipWhitespace() {
      while(this.canRead() && Character.isWhitespace(this.peek())) {
         this.skip();
      }

   }

   public int readInt() throws CommandSyntaxException {
      int start = this.cursor;

      while(this.canRead() && isAllowedNumber(this.peek())) {
         this.skip();
      }

      String number = this.string.substring(start, this.cursor);
      if (number.isEmpty()) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
      } else {
         try {
            return Integer.parseInt(number);
         } catch (NumberFormatException var4) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
         }
      }
   }

   public long readLong() throws CommandSyntaxException {
      int start = this.cursor;

      while(this.canRead() && isAllowedNumber(this.peek())) {
         this.skip();
      }

      String number = this.string.substring(start, this.cursor);
      if (number.isEmpty()) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
      } else {
         try {
            return Long.parseLong(number);
         } catch (NumberFormatException var4) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
         }
      }
   }

   public double readDouble() throws CommandSyntaxException {
      int start = this.cursor;

      while(this.canRead() && isAllowedNumber(this.peek())) {
         this.skip();
      }

      String number = this.string.substring(start, this.cursor);
      if (number.isEmpty()) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
      } else {
         try {
            return Double.parseDouble(number);
         } catch (NumberFormatException var4) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
         }
      }
   }

   public float readFloat() throws CommandSyntaxException {
      int start = this.cursor;

      while(this.canRead() && isAllowedNumber(this.peek())) {
         this.skip();
      }

      String number = this.string.substring(start, this.cursor);
      if (number.isEmpty()) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
      } else {
         try {
            return Float.parseFloat(number);
         } catch (NumberFormatException var4) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
         }
      }
   }

   public static boolean isAllowedInUnquotedString(char c) {
      return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
   }

   public String readUnquotedString() {
      int start = this.cursor;

      while(this.canRead() && isAllowedInUnquotedString(this.peek())) {
         this.skip();
      }

      return this.string.substring(start, this.cursor);
   }

   public String readQuotedString() throws CommandSyntaxException {
      if (!this.canRead()) {
         return "";
      } else {
         char next = this.peek();
         if (!isQuotedStringStart(next)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
         } else {
            this.skip();
            return this.readStringUntil(next);
         }
      }
   }

   public String readStringUntil(char terminator) throws CommandSyntaxException {
      StringBuilder result = new StringBuilder();
      boolean escaped = false;

      while(this.canRead()) {
         char c = this.read();
         if (escaped) {
            if (c != terminator && c != '\\') {
               this.setCursor(this.getCursor() - 1);
               throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
            }

            result.append(c);
            escaped = false;
         } else if (c == '\\') {
            escaped = true;
         } else {
            if (c == terminator) {
               return result.toString();
            }

            result.append(c);
         }
      }

      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
   }

   public String readString() throws CommandSyntaxException {
      if (!this.canRead()) {
         return "";
      } else {
         char next = this.peek();
         if (isQuotedStringStart(next)) {
            this.skip();
            return this.readStringUntil(next);
         } else {
            return this.readUnquotedString();
         }
      }
   }

   public boolean readBoolean() throws CommandSyntaxException {
      int start = this.cursor;
      String value = this.readString();
      if (value.isEmpty()) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
      } else if (value.equals("true")) {
         return true;
      } else if (value.equals("false")) {
         return false;
      } else {
         this.cursor = start;
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
      }
   }

   public void expect(char c) throws CommandSyntaxException {
      if (this.canRead() && this.peek() == c) {
         this.skip();
      } else {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
      }
   }
}
