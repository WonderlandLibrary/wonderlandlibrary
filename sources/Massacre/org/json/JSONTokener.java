package org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
   private long character;
   private boolean eof;
   private long index;
   private long line;
   private char previous;
   private final Reader reader;
   private boolean usePrevious;
   private long characterPreviousLine;

   public JSONTokener(Reader reader) {
      this.reader = (Reader)(reader.markSupported() ? reader : new BufferedReader(reader));
      this.eof = false;
      this.usePrevious = false;
      this.previous = 0;
      this.index = 0L;
      this.character = 1L;
      this.characterPreviousLine = 0L;
      this.line = 1L;
   }

   public JSONTokener(InputStream inputStream) {
      this((Reader)(new InputStreamReader(inputStream)));
   }

   public JSONTokener(String s) {
      this((Reader)(new StringReader(s)));
   }

   public void back() throws JSONException {
      if (!this.usePrevious && this.index > 0L) {
         this.decrementIndexes();
         this.usePrevious = true;
         this.eof = false;
      } else {
         throw new JSONException("Stepping back two steps is not supported");
      }
   }

   private void decrementIndexes() {
      --this.index;
      if (this.previous != '\r' && this.previous != '\n') {
         if (this.character > 0L) {
            --this.character;
         }
      } else {
         --this.line;
         this.character = this.characterPreviousLine;
      }

   }

   public static int dehexchar(char c) {
      if (c >= '0' && c <= '9') {
         return c - 48;
      } else if (c >= 'A' && c <= 'F') {
         return c - 55;
      } else {
         return c >= 'a' && c <= 'f' ? c - 87 : -1;
      }
   }

   public boolean end() {
      return this.eof && !this.usePrevious;
   }

   public boolean more() throws JSONException {
      if (this.usePrevious) {
         return true;
      } else {
         try {
            this.reader.mark(1);
         } catch (IOException var3) {
            throw new JSONException("Unable to preserve stream position", var3);
         }

         try {
            if (this.reader.read() <= 0) {
               this.eof = true;
               return false;
            } else {
               this.reader.reset();
               return true;
            }
         } catch (IOException var2) {
            throw new JSONException("Unable to read the next character from the stream", var2);
         }
      }
   }

   public char next() throws JSONException {
      int c;
      if (this.usePrevious) {
         this.usePrevious = false;
         c = this.previous;
      } else {
         try {
            c = this.reader.read();
         } catch (IOException var3) {
            throw new JSONException(var3);
         }
      }

      if (c <= 0) {
         this.eof = true;
         return '\u0000';
      } else {
         this.incrementIndexes(c);
         this.previous = (char)c;
         return this.previous;
      }
   }

   private void incrementIndexes(int c) {
      if (c > 0) {
         ++this.index;
         if (c == 13) {
            ++this.line;
            this.characterPreviousLine = this.character;
            this.character = 0L;
         } else if (c == 10) {
            if (this.previous != '\r') {
               ++this.line;
               this.characterPreviousLine = this.character;
            }

            this.character = 0L;
         } else {
            ++this.character;
         }
      }

   }

   public char next(char c) throws JSONException {
      char n = this.next();
      if (n != c) {
         if (n > 0) {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
         } else {
            throw this.syntaxError("Expected '" + c + "' and instead saw ''");
         }
      } else {
         return n;
      }
   }

   public String next(int n) throws JSONException {
      if (n == 0) {
         return "";
      } else {
         char[] chars = new char[n];

         for(int pos = 0; pos < n; ++pos) {
            chars[pos] = this.next();
            if (this.end()) {
               throw this.syntaxError("Substring bounds error");
            }
         }

         return new String(chars);
      }
   }

   public char nextClean() throws JSONException {
      char c;
      do {
         c = this.next();
      } while(c != 0 && c <= ' ');

      return c;
   }

   public String nextString(char quote) throws JSONException {
      StringBuilder sb = new StringBuilder();

      while(true) {
         char c = this.next();
         switch(c) {
         case '\u0000':
         case '\n':
         case '\r':
            throw this.syntaxError("Unterminated string");
         case '\\':
            c = this.next();
            switch(c) {
            case '"':
            case '\'':
            case '/':
            case '\\':
               sb.append(c);
               continue;
            case 'b':
               sb.append('\b');
               continue;
            case 'f':
               sb.append('\f');
               continue;
            case 'n':
               sb.append('\n');
               continue;
            case 'r':
               sb.append('\r');
               continue;
            case 't':
               sb.append('\t');
               continue;
            case 'u':
               try {
                  sb.append((char)Integer.parseInt(this.next((int)4), 16));
                  continue;
               } catch (NumberFormatException var5) {
                  throw this.syntaxError("Illegal escape.", var5);
               }
            default:
               throw this.syntaxError("Illegal escape.");
            }
         default:
            if (c == quote) {
               return sb.toString();
            }

            sb.append(c);
         }
      }
   }

   public String nextTo(char delimiter) throws JSONException {
      StringBuilder sb = new StringBuilder();

      while(true) {
         char c = this.next();
         if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
            if (c != 0) {
               this.back();
            }

            return sb.toString().trim();
         }

         sb.append(c);
      }
   }

   public String nextTo(String delimiters) throws JSONException {
      StringBuilder sb = new StringBuilder();

      while(true) {
         char c = this.next();
         if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
            if (c != 0) {
               this.back();
            }

            return sb.toString().trim();
         }

         sb.append(c);
      }
   }

   public Object nextValue() throws JSONException {
      char c = this.nextClean();
      switch(c) {
      case '"':
      case '\'':
         return this.nextString(c);
      case '[':
         this.back();
         return new JSONArray(this);
      case '{':
         this.back();
         return new JSONObject(this);
      default:
         StringBuilder sb;
         for(sb = new StringBuilder(); c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
            sb.append(c);
         }

         if (!this.eof) {
            this.back();
         }

         String string = sb.toString().trim();
         if ("".equals(string)) {
            throw this.syntaxError("Missing value");
         } else {
            return JSONObject.stringToValue(string);
         }
      }
   }

   public char skipTo(char to) throws JSONException {
      char c;
      try {
         long startIndex = this.index;
         long startCharacter = this.character;
         long startLine = this.line;
         this.reader.mark(1000000);

         while(true) {
            c = this.next();
            if (c == 0) {
               this.reader.reset();
               this.index = startIndex;
               this.character = startCharacter;
               this.line = startLine;
               return '\u0000';
            }

            if (c == to) {
               this.reader.mark(1);
               break;
            }
         }
      } catch (IOException var9) {
         throw new JSONException(var9);
      }

      this.back();
      return c;
   }

   public JSONException syntaxError(String message) {
      return new JSONException(message + this.toString());
   }

   public JSONException syntaxError(String message, Throwable causedBy) {
      return new JSONException(message + this.toString(), causedBy);
   }

   public String toString() {
      return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
   }
}
