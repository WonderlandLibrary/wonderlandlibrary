package org.json;

import java.io.Reader;
import java.util.HashMap;

public class XMLTokener extends JSONTokener {
   public static final HashMap<String, Character> entity = new HashMap(8);

   public XMLTokener(Reader r) {
      super(r);
   }

   public XMLTokener(String s) {
      super(s);
   }

   public String nextCDATA() throws JSONException {
      StringBuilder sb = new StringBuilder();

      int i;
      do {
         if (!this.more()) {
            throw this.syntaxError("Unclosed CDATA");
         }

         char c = this.next();
         sb.append(c);
         i = sb.length() - 3;
      } while(i < 0 || sb.charAt(i) != ']' || sb.charAt(i + 1) != ']' || sb.charAt(i + 2) != '>');

      sb.setLength(i);
      return sb.toString();
   }

   public Object nextContent() throws JSONException {
      char c;
      do {
         c = this.next();
      } while(Character.isWhitespace(c));

      if (c == 0) {
         return null;
      } else if (c == '<') {
         return XML.LT;
      } else {
         StringBuilder sb;
         for(sb = new StringBuilder(); c != 0; c = this.next()) {
            if (c == '<') {
               this.back();
               return sb.toString().trim();
            }

            if (c == '&') {
               sb.append(this.nextEntity(c));
            } else {
               sb.append(c);
            }
         }

         return sb.toString().trim();
      }
   }

   public Object nextEntity(char ampersand) throws JSONException {
      StringBuilder sb = new StringBuilder();

      while(true) {
         char c = this.next();
         if (!Character.isLetterOrDigit(c) && c != '#') {
            if (c == ';') {
               String string = sb.toString();
               return unescapeEntity(string);
            }

            throw this.syntaxError("Missing ';' in XML entity: &" + sb);
         }

         sb.append(Character.toLowerCase(c));
      }
   }

   static String unescapeEntity(String e) {
      if (e != null && !e.isEmpty()) {
         if (e.charAt(0) != '#') {
            Character knownEntity = (Character)entity.get(e);
            return knownEntity == null ? '&' + e + ';' : knownEntity.toString();
         } else {
            int cp;
            if (e.charAt(1) != 'x' && e.charAt(1) != 'X') {
               cp = Integer.parseInt(e.substring(1));
            } else {
               cp = Integer.parseInt(e.substring(2), 16);
            }

            return new String(new int[]{cp}, 0, 1);
         }
      } else {
         return "";
      }
   }

   public Object nextMeta() throws JSONException {
      char c;
      do {
         c = this.next();
      } while(Character.isWhitespace(c));

      switch(c) {
      case '\u0000':
         throw this.syntaxError("Misshaped meta tag");
      case '!':
         return XML.BANG;
      case '"':
      case '\'':
         char q = c;

         do {
            c = this.next();
            if (c == 0) {
               throw this.syntaxError("Unterminated string");
            }
         } while(c != q);

         return Boolean.TRUE;
      case '/':
         return XML.SLASH;
      case '<':
         return XML.LT;
      case '=':
         return XML.EQ;
      case '>':
         return XML.GT;
      case '?':
         return XML.QUEST;
      default:
         while(true) {
            c = this.next();
            if (Character.isWhitespace(c)) {
               return Boolean.TRUE;
            }

            switch(c) {
            case '\u0000':
               throw this.syntaxError("Unterminated string");
            case '!':
            case '"':
            case '\'':
            case '/':
            case '<':
            case '=':
            case '>':
            case '?':
               this.back();
               return Boolean.TRUE;
            }
         }
      }
   }

   public Object nextToken() throws JSONException {
      char c;
      do {
         c = this.next();
      } while(Character.isWhitespace(c));

      StringBuilder sb;
      switch(c) {
      case '\u0000':
         throw this.syntaxError("Misshaped element");
      case '!':
         return XML.BANG;
      case '"':
      case '\'':
         char q = c;
         sb = new StringBuilder();

         while(true) {
            c = this.next();
            if (c == 0) {
               throw this.syntaxError("Unterminated string");
            }

            if (c == q) {
               return sb.toString();
            }

            if (c == '&') {
               sb.append(this.nextEntity(c));
            } else {
               sb.append(c);
            }
         }
      case '/':
         return XML.SLASH;
      case '<':
         throw this.syntaxError("Misplaced '<'");
      case '=':
         return XML.EQ;
      case '>':
         return XML.GT;
      case '?':
         return XML.QUEST;
      default:
         sb = new StringBuilder();

         while(true) {
            sb.append(c);
            c = this.next();
            if (Character.isWhitespace(c)) {
               return sb.toString();
            }

            switch(c) {
            case '\u0000':
               return sb.toString();
            case '!':
            case '/':
            case '=':
            case '>':
            case '?':
            case '[':
            case ']':
               this.back();
               return sb.toString();
            case '"':
            case '\'':
            case '<':
               throw this.syntaxError("Bad character in a name");
            }
         }
      }
   }

   public void skipPast(String to) {
      int offset = 0;
      int length = to.length();
      char[] circle = new char[length];

      char c;
      int i;
      for(i = 0; i < length; ++i) {
         c = this.next();
         if (c == 0) {
            return;
         }

         circle[i] = c;
      }

      while(true) {
         int j = offset;
         boolean b = true;

         for(i = 0; i < length; ++i) {
            if (circle[j] != to.charAt(i)) {
               b = false;
               break;
            }

            ++j;
            if (j >= length) {
               j -= length;
            }
         }

         if (b) {
            return;
         }

         c = this.next();
         if (c == 0) {
            return;
         }

         circle[offset] = c;
         ++offset;
         if (offset >= length) {
            offset -= length;
         }
      }
   }

   static {
      entity.put("amp", XML.AMP);
      entity.put("apos", XML.APOS);
      entity.put("gt", XML.GT);
      entity.put("lt", XML.LT);
      entity.put("quot", XML.QUOT);
   }
}
