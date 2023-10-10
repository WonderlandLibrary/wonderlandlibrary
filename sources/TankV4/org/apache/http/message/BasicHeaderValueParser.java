package org.apache.http.message;

import java.util.ArrayList;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueParser implements HeaderValueParser {
   /** @deprecated */
   @Deprecated
   public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
   public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
   private static final char PARAM_DELIMITER = ';';
   private static final char ELEM_DELIMITER = ',';
   private static final char[] ALL_DELIMITERS = new char[]{';', ','};

   public static HeaderElement[] parseElements(String var0, HeaderValueParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((HeaderValueParser)(var1 != null ? var1 : INSTANCE)).parseElements(var2, var3);
   }

   public HeaderElement[] parseElements(CharArrayBuffer var1, ParserCursor var2) {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      ArrayList var3 = new ArrayList();

      while(true) {
         HeaderElement var4;
         do {
            if (var2.atEnd()) {
               return (HeaderElement[])var3.toArray(new HeaderElement[var3.size()]);
            }

            var4 = this.parseHeaderElement(var1, var2);
         } while(var4.getName().length() == 0 && var4.getValue() == null);

         var3.add(var4);
      }
   }

   public static HeaderElement parseHeaderElement(String var0, HeaderValueParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((HeaderValueParser)(var1 != null ? var1 : INSTANCE)).parseHeaderElement(var2, var3);
   }

   public HeaderElement parseHeaderElement(CharArrayBuffer var1, ParserCursor var2) {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      NameValuePair var3 = this.parseNameValuePair(var1, var2);
      NameValuePair[] var4 = null;
      if (!var2.atEnd()) {
         char var5 = var1.charAt(var2.getPos() - 1);
         if (var5 != ',') {
            var4 = this.parseParameters(var1, var2);
         }
      }

      return this.createHeaderElement(var3.getName(), var3.getValue(), var4);
   }

   protected HeaderElement createHeaderElement(String var1, String var2, NameValuePair[] var3) {
      return new BasicHeaderElement(var1, var2, var3);
   }

   public static NameValuePair[] parseParameters(String var0, HeaderValueParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((HeaderValueParser)(var1 != null ? var1 : INSTANCE)).parseParameters(var2, var3);
   }

   public NameValuePair[] parseParameters(CharArrayBuffer var1, ParserCursor var2) {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      int var3 = var2.getPos();

      for(int var4 = var2.getUpperBound(); var3 < var4; ++var3) {
         char var5 = var1.charAt(var3);
         if (!HTTP.isWhitespace(var5)) {
            break;
         }
      }

      var2.updatePos(var3);
      if (var2.atEnd()) {
         return new NameValuePair[0];
      } else {
         ArrayList var8 = new ArrayList();

         while(!var2.atEnd()) {
            NameValuePair var6 = this.parseNameValuePair(var1, var2);
            var8.add(var6);
            char var7 = var1.charAt(var2.getPos() - 1);
            if (var7 == ',') {
               break;
            }
         }

         return (NameValuePair[])var8.toArray(new NameValuePair[var8.size()]);
      }
   }

   public static NameValuePair parseNameValuePair(String var0, HeaderValueParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((HeaderValueParser)(var1 != null ? var1 : INSTANCE)).parseNameValuePair(var2, var3);
   }

   public NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2) {
      return this.parseNameValuePair(var1, var2, ALL_DELIMITERS);
   }

   public NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2, char[] var3) {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      boolean var4 = false;
      int var5 = var2.getPos();
      int var6 = var2.getPos();

      int var7;
      for(var7 = var2.getUpperBound(); var5 < var7; ++var5) {
         char var9 = var1.charAt(var5);
         if (var9 == '=') {
            break;
         }

         if (var3 != null) {
            var4 = true;
            break;
         }
      }

      String var8;
      if (var5 == var7) {
         var4 = true;
         var8 = var1.substringTrimmed(var6, var7);
      } else {
         var8 = var1.substringTrimmed(var6, var5);
         ++var5;
      }

      if (var4) {
         var2.updatePos(var5);
         return this.createNameValuePair(var8, (String)null);
      } else {
         int var10 = var5;
         boolean var11 = false;

         for(boolean var12 = false; var5 < var7; ++var5) {
            char var13 = var1.charAt(var5);
            if (var13 == '"' && !var12) {
               var11 = !var11;
            }

            if (!var11 && !var12 && var3 != null) {
               var4 = true;
               break;
            }

            if (var12) {
               var12 = false;
            } else {
               var12 = var11 && var13 == '\\';
            }
         }

         int var15;
         for(var15 = var5; var10 < var15 && HTTP.isWhitespace(var1.charAt(var10)); ++var10) {
         }

         while(var15 > var10 && HTTP.isWhitespace(var1.charAt(var15 - 1))) {
            --var15;
         }

         if (var15 - var10 >= 2 && var1.charAt(var10) == '"' && var1.charAt(var15 - 1) == '"') {
            ++var10;
            --var15;
         }

         String var14 = var1.substring(var10, var15);
         if (var4) {
            ++var5;
         }

         var2.updatePos(var5);
         return this.createNameValuePair(var8, var14);
      }
   }

   protected NameValuePair createNameValuePair(String var1, String var2) {
      return new BasicNameValuePair(var1, var2);
   }
}
