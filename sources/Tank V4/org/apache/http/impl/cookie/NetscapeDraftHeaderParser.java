package org.apache.http.impl.cookie;

import java.util.ArrayList;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class NetscapeDraftHeaderParser {
   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();

   public HeaderElement parseHeader(CharArrayBuffer var1, ParserCursor var2) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      NameValuePair var3 = this.parseNameValuePair(var1, var2);
      ArrayList var4 = new ArrayList();

      while(!var2.atEnd()) {
         NameValuePair var5 = this.parseNameValuePair(var1, var2);
         var4.add(var5);
      }

      return new BasicHeaderElement(var3.getName(), var3.getValue(), (NameValuePair[])var4.toArray(new NameValuePair[var4.size()]));
   }

   private NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2) {
      boolean var3 = false;
      int var4 = var2.getPos();
      int var5 = var2.getPos();
      int var6 = var2.getUpperBound();

      String var7;
      for(var7 = null; var4 < var6; ++var4) {
         char var8 = var1.charAt(var4);
         if (var8 == '=') {
            break;
         }

         if (var8 == ';') {
            var3 = true;
            break;
         }
      }

      if (var4 == var6) {
         var3 = true;
         var7 = var1.substringTrimmed(var5, var6);
      } else {
         var7 = var1.substringTrimmed(var5, var4);
         ++var4;
      }

      if (var3) {
         var2.updatePos(var4);
         return new BasicNameValuePair(var7, (String)null);
      } else {
         String var11 = null;

         int var9;
         for(var9 = var4; var4 < var6; ++var4) {
            char var10 = var1.charAt(var4);
            if (var10 == ';') {
               var3 = true;
               break;
            }
         }

         int var12;
         for(var12 = var4; var9 < var12 && HTTP.isWhitespace(var1.charAt(var9)); ++var9) {
         }

         while(var12 > var9 && HTTP.isWhitespace(var1.charAt(var12 - 1))) {
            --var12;
         }

         var11 = var1.substring(var9, var12);
         if (var3) {
            ++var4;
         }

         var2.updatePos(var4);
         return new BasicNameValuePair(var7, var11);
      }
   }
}
