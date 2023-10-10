package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicLineParser implements LineParser {
   /** @deprecated */
   @Deprecated
   public static final BasicLineParser DEFAULT = new BasicLineParser();
   public static final BasicLineParser INSTANCE = new BasicLineParser();
   protected final ProtocolVersion protocol;

   public BasicLineParser(ProtocolVersion var1) {
      this.protocol = (ProtocolVersion)(var1 != null ? var1 : HttpVersion.HTTP_1_1);
   }

   public BasicLineParser() {
      this((ProtocolVersion)null);
   }

   public static ProtocolVersion parseProtocolVersion(String var0, LineParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((LineParser)(var1 != null ? var1 : INSTANCE)).parseProtocolVersion(var2, var3);
   }

   public ProtocolVersion parseProtocolVersion(CharArrayBuffer var1, ParserCursor var2) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      String var3 = this.protocol.getProtocol();
      int var4 = var3.length();
      int var5 = var2.getPos();
      int var6 = var2.getUpperBound();
      this.skipWhitespace(var1, var2);
      int var7 = var2.getPos();
      if (var7 + var4 + 4 > var6) {
         throw new ParseException("Not a valid protocol version: " + var1.substring(var5, var6));
      } else {
         boolean var8 = true;

         int var9;
         for(var9 = 0; var8 && var9 < var4; ++var9) {
            var8 = var1.charAt(var7 + var9) == var3.charAt(var9);
         }

         if (var8) {
            var8 = var1.charAt(var7 + var4) == '/';
         }

         if (!var8) {
            throw new ParseException("Not a valid protocol version: " + var1.substring(var5, var6));
         } else {
            var7 += var4 + 1;
            var9 = var1.indexOf(46, var7, var6);
            if (var9 == -1) {
               throw new ParseException("Invalid protocol version number: " + var1.substring(var5, var6));
            } else {
               int var10;
               try {
                  var10 = Integer.parseInt(var1.substringTrimmed(var7, var9));
               } catch (NumberFormatException var15) {
                  throw new ParseException("Invalid protocol major version number: " + var1.substring(var5, var6));
               }

               var7 = var9 + 1;
               int var11 = var1.indexOf(32, var7, var6);
               if (var11 == -1) {
                  var11 = var6;
               }

               int var12;
               try {
                  var12 = Integer.parseInt(var1.substringTrimmed(var7, var11));
               } catch (NumberFormatException var14) {
                  throw new ParseException("Invalid protocol minor version number: " + var1.substring(var5, var6));
               }

               var2.updatePos(var11);
               return this.createProtocolVersion(var10, var12);
            }
         }
      }
   }

   protected ProtocolVersion createProtocolVersion(int var1, int var2) {
      return this.protocol.forVersion(var1, var2);
   }

   public boolean hasProtocolVersion(CharArrayBuffer var1, ParserCursor var2) {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      int var3 = var2.getPos();
      String var4 = this.protocol.getProtocol();
      int var5 = var4.length();
      if (var1.length() < var5 + 4) {
         return false;
      } else {
         if (var3 < 0) {
            var3 = var1.length() - 4 - var5;
         } else if (var3 == 0) {
            while(var3 < var1.length() && HTTP.isWhitespace(var1.charAt(var3))) {
               ++var3;
            }
         }

         if (var3 + var5 + 4 > var1.length()) {
            return false;
         } else {
            boolean var6 = true;

            for(int var7 = 0; var6 && var7 < var5; ++var7) {
               var6 = var1.charAt(var3 + var7) == var4.charAt(var7);
            }

            if (var6) {
               var6 = var1.charAt(var3 + var5) == '/';
            }

            return var6;
         }
      }
   }

   public static RequestLine parseRequestLine(String var0, LineParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((LineParser)(var1 != null ? var1 : INSTANCE)).parseRequestLine(var2, var3);
   }

   public RequestLine parseRequestLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      int var3 = var2.getPos();
      int var4 = var2.getUpperBound();

      try {
         this.skipWhitespace(var1, var2);
         int var5 = var2.getPos();
         int var6 = var1.indexOf(32, var5, var4);
         if (var6 < 0) {
            throw new ParseException("Invalid request line: " + var1.substring(var3, var4));
         } else {
            String var7 = var1.substringTrimmed(var5, var6);
            var2.updatePos(var6);
            this.skipWhitespace(var1, var2);
            var5 = var2.getPos();
            var6 = var1.indexOf(32, var5, var4);
            if (var6 < 0) {
               throw new ParseException("Invalid request line: " + var1.substring(var3, var4));
            } else {
               String var8 = var1.substringTrimmed(var5, var6);
               var2.updatePos(var6);
               ProtocolVersion var9 = this.parseProtocolVersion(var1, var2);
               this.skipWhitespace(var1, var2);
               if (!var2.atEnd()) {
                  throw new ParseException("Invalid request line: " + var1.substring(var3, var4));
               } else {
                  return this.createRequestLine(var7, var8, var9);
               }
            }
         }
      } catch (IndexOutOfBoundsException var10) {
         throw new ParseException("Invalid request line: " + var1.substring(var3, var4));
      }
   }

   protected RequestLine createRequestLine(String var1, String var2, ProtocolVersion var3) {
      return new BasicRequestLine(var1, var2, var3);
   }

   public static StatusLine parseStatusLine(String var0, LineParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      ParserCursor var3 = new ParserCursor(0, var0.length());
      return ((LineParser)(var1 != null ? var1 : INSTANCE)).parseStatusLine(var2, var3);
   }

   public StatusLine parseStatusLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      int var3 = var2.getPos();
      int var4 = var2.getUpperBound();

      try {
         ProtocolVersion var5 = this.parseProtocolVersion(var1, var2);
         this.skipWhitespace(var1, var2);
         int var6 = var2.getPos();
         int var7 = var1.indexOf(32, var6, var4);
         if (var7 < 0) {
            var7 = var4;
         }

         String var9 = var1.substringTrimmed(var6, var7);

         for(int var10 = 0; var10 < var9.length(); ++var10) {
            if (!Character.isDigit(var9.charAt(var10))) {
               throw new ParseException("Status line contains invalid status code: " + var1.substring(var3, var4));
            }
         }

         int var8;
         try {
            var8 = Integer.parseInt(var9);
         } catch (NumberFormatException var11) {
            throw new ParseException("Status line contains invalid status code: " + var1.substring(var3, var4));
         }

         String var13;
         if (var7 < var4) {
            var13 = var1.substringTrimmed(var7, var4);
         } else {
            var13 = "";
         }

         return this.createStatusLine(var5, var8, var13);
      } catch (IndexOutOfBoundsException var12) {
         throw new ParseException("Invalid status line: " + var1.substring(var3, var4));
      }
   }

   protected StatusLine createStatusLine(ProtocolVersion var1, int var2, String var3) {
      return new BasicStatusLine(var1, var2, var3);
   }

   public static Header parseHeader(String var0, LineParser var1) throws ParseException {
      Args.notNull(var0, "Value");
      CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
      var2.append(var0);
      return ((LineParser)(var1 != null ? var1 : INSTANCE)).parseHeader(var2);
   }

   public Header parseHeader(CharArrayBuffer var1) throws ParseException {
      return new BufferedHeader(var1);
   }

   protected void skipWhitespace(CharArrayBuffer var1, ParserCursor var2) {
      int var3 = var2.getPos();

      for(int var4 = var2.getUpperBound(); var3 < var4 && HTTP.isWhitespace(var1.charAt(var3)); ++var3) {
      }

      var2.updatePos(var3);
   }
}
