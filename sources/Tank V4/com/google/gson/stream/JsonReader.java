package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader implements Closeable {
   private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
   private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
   private static final int PEEKED_NONE = 0;
   private static final int PEEKED_BEGIN_OBJECT = 1;
   private static final int PEEKED_END_OBJECT = 2;
   private static final int PEEKED_BEGIN_ARRAY = 3;
   private static final int PEEKED_END_ARRAY = 4;
   private static final int PEEKED_TRUE = 5;
   private static final int PEEKED_FALSE = 6;
   private static final int PEEKED_NULL = 7;
   private static final int PEEKED_SINGLE_QUOTED = 8;
   private static final int PEEKED_DOUBLE_QUOTED = 9;
   private static final int PEEKED_UNQUOTED = 10;
   private static final int PEEKED_BUFFERED = 11;
   private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
   private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
   private static final int PEEKED_UNQUOTED_NAME = 14;
   private static final int PEEKED_LONG = 15;
   private static final int PEEKED_NUMBER = 16;
   private static final int PEEKED_EOF = 17;
   private static final int NUMBER_CHAR_NONE = 0;
   private static final int NUMBER_CHAR_SIGN = 1;
   private static final int NUMBER_CHAR_DIGIT = 2;
   private static final int NUMBER_CHAR_DECIMAL = 3;
   private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
   private static final int NUMBER_CHAR_EXP_E = 5;
   private static final int NUMBER_CHAR_EXP_SIGN = 6;
   private static final int NUMBER_CHAR_EXP_DIGIT = 7;
   private final Reader in;
   private boolean lenient = false;
   private final char[] buffer = new char[1024];
   private int pos = 0;
   private int limit = 0;
   private int lineNumber = 0;
   private int lineStart = 0;
   private int peeked = 0;
   private long peekedLong;
   private int peekedNumberLength;
   private String peekedString;
   private int[] stack = new int[32];
   private int stackSize = 0;

   public JsonReader(Reader var1) {
      this.stack[this.stackSize++] = 6;
      if (var1 == null) {
         throw new NullPointerException("in == null");
      } else {
         this.in = var1;
      }
   }

   public final void setLenient(boolean var1) {
      this.lenient = var1;
   }

   public final boolean isLenient() {
      return this.lenient;
   }

   public void beginArray() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 3) {
         this.push(1);
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected BEGIN_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public void endArray() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 4) {
         --this.stackSize;
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected END_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public void beginObject() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 1) {
         this.push(3);
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected BEGIN_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public void endObject() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 2) {
         --this.stackSize;
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected END_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public boolean hasNext() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      return var1 != 2 && var1 != 4;
   }

   public JsonToken peek() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      switch(var1) {
      case 1:
         return JsonToken.BEGIN_OBJECT;
      case 2:
         return JsonToken.END_OBJECT;
      case 3:
         return JsonToken.BEGIN_ARRAY;
      case 4:
         return JsonToken.END_ARRAY;
      case 5:
      case 6:
         return JsonToken.BOOLEAN;
      case 7:
         return JsonToken.NULL;
      case 8:
      case 9:
      case 10:
      case 11:
         return JsonToken.STRING;
      case 12:
      case 13:
      case 14:
         return JsonToken.NAME;
      case 15:
      case 16:
         return JsonToken.NUMBER;
      case 17:
         return JsonToken.END_DOCUMENT;
      default:
         throw new AssertionError();
      }
   }

   private int doPeek() throws IOException {
      int var1 = this.stack[this.stackSize - 1];
      int var2;
      if (var1 == 1) {
         this.stack[this.stackSize - 1] = 2;
      } else if (var1 == 2) {
         var2 = this.nextNonWhitespace(true);
         switch(var2) {
         case 44:
            break;
         case 59:
            this.checkLenient();
            break;
         case 93:
            return this.peeked = 4;
         default:
            throw this.syntaxError("Unterminated array");
         }
      } else {
         if (var1 == 3 || var1 == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (var1 == 5) {
               var2 = this.nextNonWhitespace(true);
               switch(var2) {
               case 44:
                  break;
               case 59:
                  this.checkLenient();
                  break;
               case 125:
                  return this.peeked = 2;
               default:
                  throw this.syntaxError("Unterminated object");
               }
            }

            var2 = this.nextNonWhitespace(true);
            switch(var2) {
            case 34:
               return this.peeked = 13;
            case 39:
               this.checkLenient();
               return this.peeked = 12;
            case 125:
               if (var1 != 5) {
                  return this.peeked = 2;
               }

               throw this.syntaxError("Expected name");
            default:
               this.checkLenient();
               --this.pos;
               if (this.isLiteral((char)var2)) {
                  return this.peeked = 14;
               }

               throw this.syntaxError("Expected name");
            }
         }

         if (var1 == 4) {
            this.stack[this.stackSize - 1] = 5;
            var2 = this.nextNonWhitespace(true);
            switch(var2) {
            case 58:
               break;
            case 61:
               this.checkLenient();
               if ((this.pos < this.limit || this != true) && this.buffer[this.pos] == '>') {
                  ++this.pos;
               }
               break;
            default:
               throw this.syntaxError("Expected ':'");
            }
         } else if (var1 == 6) {
            if (this.lenient) {
               this.consumeNonExecutePrefix();
            }

            this.stack[this.stackSize - 1] = 7;
         } else if (var1 == 7) {
            var2 = this.nextNonWhitespace(false);
            if (var2 == -1) {
               return this.peeked = 17;
            }

            this.checkLenient();
            --this.pos;
         } else if (var1 == 8) {
            throw new IllegalStateException("JsonReader is closed");
         }
      }

      var2 = this.nextNonWhitespace(true);
      switch(var2) {
      case 34:
         if (this.stackSize == 1) {
            this.checkLenient();
         }

         return this.peeked = 9;
      case 39:
         this.checkLenient();
         return this.peeked = 8;
      case 91:
         return this.peeked = 3;
      case 93:
         if (var1 == 1) {
            return this.peeked = 4;
         }
      case 44:
      case 59:
         if (var1 != 1 && var1 != 2) {
            throw this.syntaxError("Unexpected value");
         }

         this.checkLenient();
         --this.pos;
         return this.peeked = 7;
      case 123:
         return this.peeked = 1;
      default:
         --this.pos;
         if (this.stackSize == 1) {
            this.checkLenient();
         }

         int var3 = this.peekKeyword();
         if (var3 != 0) {
            return var3;
         } else {
            var3 = this.peekNumber();
            if (var3 != 0) {
               return var3;
            } else if (!this.isLiteral(this.buffer[this.pos])) {
               throw this.syntaxError("Expected value");
            } else {
               this.checkLenient();
               return this.peeked = 10;
            }
         }
      }
   }

   private int peekKeyword() throws IOException {
      char var1 = this.buffer[this.pos];
      String var2;
      String var3;
      byte var4;
      if (var1 != 't' && var1 != 'T') {
         if (var1 != 'f' && var1 != 'F') {
            if (var1 != 'n' && var1 != 'N') {
               return 0;
            }

            var2 = "null";
            var3 = "NULL";
            var4 = 7;
         } else {
            var2 = "false";
            var3 = "FALSE";
            var4 = 6;
         }
      } else {
         var2 = "true";
         var3 = "TRUE";
         var4 = 5;
      }

      int var5 = var2.length();

      for(int var6 = 1; var6 < var5; ++var6) {
         if (this.pos + var6 >= this.limit && this != var6 + 1) {
            return 0;
         }

         var1 = this.buffer[this.pos + var6];
         if (var1 != var2.charAt(var6) && var1 != var3.charAt(var6)) {
            return 0;
         }
      }

      if ((this.pos + var5 < this.limit || this != var5 + 1) && this.isLiteral(this.buffer[this.pos + var5])) {
         return 0;
      } else {
         this.pos += var5;
         return this.peeked = var4;
      }
   }

   private int peekNumber() throws IOException {
      char[] var1 = this.buffer;
      int var2 = this.pos;
      int var3 = this.limit;
      long var4 = 0L;
      boolean var6 = false;
      boolean var7 = true;
      byte var8 = 0;
      int var9 = 0;

      label126:
      while(true) {
         if (var2 + var9 == var3) {
            if (var9 == var1.length) {
               return 0;
            }

            if (this != var9 + 1) {
               break;
            }

            var2 = this.pos;
            var3 = this.limit;
         }

         char var10 = var1[var2 + var9];
         switch(var10) {
         case '+':
            if (var8 != 5) {
               return 0;
            }

            var8 = 6;
            break;
         case '-':
            if (var8 == 0) {
               var6 = true;
               var8 = 1;
            } else {
               if (var8 != 5) {
                  return 0;
               }

               var8 = 6;
            }
            break;
         case '.':
            if (var8 != 2) {
               return 0;
            }

            var8 = 3;
            break;
         case 'E':
         case 'e':
            if (var8 != 2 && var8 != 4) {
               return 0;
            }

            var8 = 5;
            break;
         default:
            if (var10 < '0' || var10 > '9') {
               if (this.isLiteral(var10)) {
                  return 0;
               }
               break label126;
            }

            if (var8 != 1 && var8 != 0) {
               if (var8 == 2) {
                  if (var4 == 0L) {
                     return 0;
                  }

                  long var11 = var4 * 10L - (long)(var10 - 48);
                  var7 &= var4 > -922337203685477580L || var4 == -922337203685477580L && var11 < var4;
                  var4 = var11;
               } else if (var8 == 3) {
                  var8 = 4;
               } else if (var8 == 5 || var8 == 6) {
                  var8 = 7;
               }
            } else {
               var4 = (long)(-(var10 - 48));
               var8 = 2;
            }
         }

         ++var9;
      }

      if (var8 != 2 || !var7 || var4 == Long.MIN_VALUE && !var6) {
         if (var8 != 2 && var8 != 4 && var8 != 7) {
            return 0;
         } else {
            this.peekedNumberLength = var9;
            return this.peeked = 16;
         }
      } else {
         this.peekedLong = var6 ? var4 : -var4;
         this.pos += var9;
         return this.peeked = 15;
      }
   }

   private boolean isLiteral(char var1) throws IOException {
      switch(var1) {
      case '#':
      case '/':
      case ';':
      case '=':
      case '\\':
         this.checkLenient();
      case '\t':
      case '\n':
      case '\f':
      case '\r':
      case ' ':
      case ',':
      case ':':
      case '[':
      case ']':
      case '{':
      case '}':
         return false;
      default:
         return true;
      }
   }

   public String nextName() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      String var2;
      if (var1 == 14) {
         var2 = this.nextUnquotedValue();
      } else if (var1 == 12) {
         var2 = this.nextQuotedValue('\'');
      } else {
         if (var1 != 13) {
            throw new IllegalStateException("Expected a name but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         }

         var2 = this.nextQuotedValue('"');
      }

      this.peeked = 0;
      return var2;
   }

   public String nextString() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      String var2;
      if (var1 == 10) {
         var2 = this.nextUnquotedValue();
      } else if (var1 == 8) {
         var2 = this.nextQuotedValue('\'');
      } else if (var1 == 9) {
         var2 = this.nextQuotedValue('"');
      } else if (var1 == 11) {
         var2 = this.peekedString;
         this.peekedString = null;
      } else if (var1 == 15) {
         var2 = Long.toString(this.peekedLong);
      } else {
         if (var1 != 16) {
            throw new IllegalStateException("Expected a string but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         }

         var2 = new String(this.buffer, this.pos, this.peekedNumberLength);
         this.pos += this.peekedNumberLength;
      }

      this.peeked = 0;
      return var2;
   }

   public boolean nextBoolean() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 5) {
         this.peeked = 0;
         return true;
      } else if (var1 == 6) {
         this.peeked = 0;
         return false;
      } else {
         throw new IllegalStateException("Expected a boolean but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public void nextNull() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 7) {
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected null but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }

   public double nextDouble() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 15) {
         this.peeked = 0;
         return (double)this.peekedLong;
      } else {
         if (var1 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
         } else if (var1 != 8 && var1 != 9) {
            if (var1 == 10) {
               this.peekedString = this.nextUnquotedValue();
            } else if (var1 != 11) {
               throw new IllegalStateException("Expected a double but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
         } else {
            this.peekedString = this.nextQuotedValue((char)(var1 == 8 ? '\'' : '"'));
         }

         this.peeked = 11;
         double var2 = Double.parseDouble(this.peekedString);
         if (this.lenient || !Double.isNaN(var2) && !Double.isInfinite(var2)) {
            this.peekedString = null;
            this.peeked = 0;
            return var2;
         } else {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + var2 + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         }
      }
   }

   public long nextLong() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      if (var1 == 15) {
         this.peeked = 0;
         return this.peekedLong;
      } else {
         if (var1 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
         } else {
            if (var1 != 8 && var1 != 9) {
               throw new IllegalStateException("Expected a long but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }

            this.peekedString = this.nextQuotedValue((char)(var1 == 8 ? '\'' : '"'));

            try {
               long var7 = Long.parseLong(this.peekedString);
               this.peeked = 0;
               return var7;
            } catch (NumberFormatException var6) {
            }
         }

         this.peeked = 11;
         double var2 = Double.parseDouble(this.peekedString);
         long var4 = (long)var2;
         if ((double)var4 != var2) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         } else {
            this.peekedString = null;
            this.peeked = 0;
            return var4;
         }
      }
   }

   private String nextQuotedValue(char var1) throws IOException {
      char[] var2 = this.buffer;
      StringBuilder var3 = new StringBuilder();

      do {
         int var4 = this.pos;
         int var5 = this.limit;
         int var6 = var4;

         while(var4 < var5) {
            char var7 = var2[var4++];
            if (var7 == var1) {
               this.pos = var4;
               var3.append(var2, var6, var4 - var6 - 1);
               return var3.toString();
            }

            if (var7 == '\\') {
               this.pos = var4;
               var3.append(var2, var6, var4 - var6 - 1);
               var3.append(this.readEscapeCharacter());
               var4 = this.pos;
               var5 = this.limit;
               var6 = var4;
            } else if (var7 == '\n') {
               ++this.lineNumber;
               this.lineStart = var4;
            }
         }

         var3.append(var2, var6, var4 - var6);
         this.pos = var4;
      } while(this == true);

      throw this.syntaxError("Unterminated string");
   }

   private String nextUnquotedValue() throws IOException {
      StringBuilder var1 = null;
      int var2 = 0;

      label35:
      while(true) {
         if (this.pos + var2 < this.limit) {
            switch(this.buffer[this.pos + var2]) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}':
               break label35;
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\':
               this.checkLenient();
               break label35;
            default:
               ++var2;
            }
         } else if (var2 < this.buffer.length) {
            if (this != var2 + 1) {
               continue;
            }
            break;
         } else {
            if (var1 == null) {
               var1 = new StringBuilder();
            }

            var1.append(this.buffer, this.pos, var2);
            this.pos += var2;
            var2 = 0;
            if (this != true) {
               break;
            }
         }
      }

      String var3;
      if (var1 == null) {
         var3 = new String(this.buffer, this.pos, var2);
      } else {
         var1.append(this.buffer, this.pos, var2);
         var3 = var1.toString();
      }

      this.pos += var2;
      return var3;
   }

   private void skipQuotedValue(char var1) throws IOException {
      char[] var2 = this.buffer;

      do {
         int var3 = this.pos;
         int var4 = this.limit;

         while(var3 < var4) {
            char var5 = var2[var3++];
            if (var5 == var1) {
               this.pos = var3;
               return;
            }

            if (var5 == '\\') {
               this.pos = var3;
               this.readEscapeCharacter();
               var3 = this.pos;
               var4 = this.limit;
            } else if (var5 == '\n') {
               ++this.lineNumber;
               this.lineStart = var3;
            }
         }

         this.pos = var3;
      } while(this == true);

      throw this.syntaxError("Unterminated string");
   }

   private void skipUnquotedValue() throws IOException {
      do {
         int var1 = 0;

         while(this.pos + var1 < this.limit) {
            switch(this.buffer[this.pos + var1]) {
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\':
               this.checkLenient();
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}':
               this.pos += var1;
               return;
            default:
               ++var1;
            }
         }

         this.pos += var1;
      } while(this == true);

   }

   public int nextInt() throws IOException {
      int var1 = this.peeked;
      if (var1 == 0) {
         var1 = this.doPeek();
      }

      int var2;
      if (var1 == 15) {
         var2 = (int)this.peekedLong;
         if (this.peekedLong != (long)var2) {
            throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         } else {
            this.peeked = 0;
            return var2;
         }
      } else {
         if (var1 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
         } else {
            if (var1 != 8 && var1 != 9) {
               throw new IllegalStateException("Expected an int but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }

            this.peekedString = this.nextQuotedValue((char)(var1 == 8 ? '\'' : '"'));

            try {
               var2 = Integer.parseInt(this.peekedString);
               this.peeked = 0;
               return var2;
            } catch (NumberFormatException var5) {
            }
         }

         this.peeked = 11;
         double var3 = Double.parseDouble(this.peekedString);
         var2 = (int)var3;
         if ((double)var2 != var3) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         } else {
            this.peekedString = null;
            this.peeked = 0;
            return var2;
         }
      }
   }

   public void close() throws IOException {
      this.peeked = 0;
      this.stack[0] = 8;
      this.stackSize = 1;
      this.in.close();
   }

   public void skipValue() throws IOException {
      int var1 = 0;

      do {
         int var2 = this.peeked;
         if (var2 == 0) {
            var2 = this.doPeek();
         }

         if (var2 == 3) {
            this.push(1);
            ++var1;
         } else if (var2 == 1) {
            this.push(3);
            ++var1;
         } else if (var2 == 4) {
            --this.stackSize;
            --var1;
         } else if (var2 == 2) {
            --this.stackSize;
            --var1;
         } else if (var2 != 14 && var2 != 10) {
            if (var2 != 8 && var2 != 12) {
               if (var2 != 9 && var2 != 13) {
                  if (var2 == 16) {
                     this.pos += this.peekedNumberLength;
                  }
               } else {
                  this.skipQuotedValue('"');
               }
            } else {
               this.skipQuotedValue('\'');
            }
         } else {
            this.skipUnquotedValue();
         }

         this.peeked = 0;
      } while(var1 != 0);

   }

   private void push(int var1) {
      if (this.stackSize == this.stack.length) {
         int[] var2 = new int[this.stackSize * 2];
         System.arraycopy(this.stack, 0, var2, 0, this.stackSize);
         this.stack = var2;
      }

      this.stack[this.stackSize++] = var1;
   }

   private int getLineNumber() {
      return this.lineNumber + 1;
   }

   private int getColumnNumber() {
      return this.pos - this.lineStart + 1;
   }

   private int nextNonWhitespace(boolean var1) throws IOException {
      char[] var2 = this.buffer;
      int var3 = this.pos;
      int var4 = this.limit;

      while(true) {
         if (var3 == var4) {
            this.pos = var3;
            if (this != true) {
               if (var1) {
                  throw new EOFException("End of input at line " + this.getLineNumber() + " column " + this.getColumnNumber());
               }

               return -1;
            }

            var3 = this.pos;
            var4 = this.limit;
         }

         char var5 = var2[var3++];
         if (var5 == '\n') {
            ++this.lineNumber;
            this.lineStart = var3;
         } else if (var5 != ' ' && var5 != '\r' && var5 != '\t') {
            if (var5 == '/') {
               this.pos = var3;
               if (var3 == var4) {
                  --this.pos;
                  boolean var6 = this.fillBuffer(2);
                  ++this.pos;
                  if (!var6) {
                     return var5;
                  }
               }

               this.checkLenient();
               char var7 = var2[this.pos];
               switch(var7) {
               case '*':
                  ++this.pos;
                  if (this > "*/") {
                     throw this.syntaxError("Unterminated comment");
                  }

                  var3 = this.pos + 2;
                  var4 = this.limit;
                  break;
               case '/':
                  ++this.pos;
                  this.skipToEndOfLine();
                  var3 = this.pos;
                  var4 = this.limit;
                  break;
               default:
                  return var5;
               }
            } else {
               if (var5 != '#') {
                  this.pos = var3;
                  return var5;
               }

               this.pos = var3;
               this.checkLenient();
               this.skipToEndOfLine();
               var3 = this.pos;
               var4 = this.limit;
            }
         }
      }
   }

   private void checkLenient() throws IOException {
      if (!this.lenient) {
         throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
      }
   }

   private void skipToEndOfLine() throws IOException {
      while(this.pos < this.limit || this != true) {
         char var1 = this.buffer[this.pos++];
         if (var1 == '\n') {
            ++this.lineNumber;
            this.lineStart = this.pos;
         } else if (var1 != '\r') {
            continue;
         }
         break;
      }

   }

   public String toString() {
      return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
   }

   private char readEscapeCharacter() throws IOException {
      if (this.pos == this.limit && this != true) {
         throw this.syntaxError("Unterminated escape sequence");
      } else {
         char var1 = this.buffer[this.pos++];
         switch(var1) {
         case '\n':
            ++this.lineNumber;
            this.lineStart = this.pos;
         case '"':
         case '\'':
         case '\\':
         default:
            return var1;
         case 'b':
            return '\b';
         case 'f':
            return '\f';
         case 'n':
            return '\n';
         case 'r':
            return '\r';
         case 't':
            return '\t';
         case 'u':
            if (this.pos + 4 > this.limit && this != true) {
               throw this.syntaxError("Unterminated escape sequence");
            } else {
               char var2 = 0;
               int var3 = this.pos;

               for(int var4 = var3 + 4; var3 < var4; ++var3) {
                  char var5 = this.buffer[var3];
                  var2 = (char)(var2 << 4);
                  if (var5 >= '0' && var5 <= '9') {
                     var2 = (char)(var2 + (var5 - 48));
                  } else if (var5 >= 'a' && var5 <= 'f') {
                     var2 = (char)(var2 + var5 - 97 + 10);
                  } else {
                     if (var5 < 'A' || var5 > 'F') {
                        throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                     }

                     var2 = (char)(var2 + var5 - 65 + 10);
                  }
               }

               this.pos += 4;
               return var2;
            }
         }
      }
   }

   private IOException syntaxError(String var1) throws IOException {
      throw new MalformedJsonException(var1 + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
   }

   private void consumeNonExecutePrefix() throws IOException {
      this.nextNonWhitespace(true);
      --this.pos;
      if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || this == NON_EXECUTE_PREFIX.length) {
         for(int var1 = 0; var1 < NON_EXECUTE_PREFIX.length; ++var1) {
            if (this.buffer[this.pos + var1] != NON_EXECUTE_PREFIX[var1]) {
               return;
            }
         }

         this.pos += NON_EXECUTE_PREFIX.length;
      }
   }

   static int access$000(JsonReader var0) {
      return var0.peeked;
   }

   static int access$100(JsonReader var0) throws IOException {
      return var0.doPeek();
   }

   static int access$002(JsonReader var0, int var1) {
      return var0.peeked = var1;
   }

   static int access$200(JsonReader var0) {
      return var0.getLineNumber();
   }

   static int access$300(JsonReader var0) {
      return var0.getColumnNumber();
   }

   static {
      JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
         public void promoteNameToValue(JsonReader var1) throws IOException {
            if (var1 instanceof JsonTreeReader) {
               ((JsonTreeReader)var1).promoteNameToValue();
            } else {
               int var2 = JsonReader.access$000(var1);
               if (var2 == 0) {
                  var2 = JsonReader.access$100(var1);
               }

               if (var2 == 13) {
                  JsonReader.access$002(var1, 9);
               } else if (var2 == 12) {
                  JsonReader.access$002(var1, 8);
               } else {
                  if (var2 != 14) {
                     throw new IllegalStateException("Expected a name but was " + var1.peek() + " " + " at line " + JsonReader.access$200(var1) + " column " + JsonReader.access$300(var1));
                  }

                  JsonReader.access$002(var1, 10);
               }

            }
         }
      };
   }
}
