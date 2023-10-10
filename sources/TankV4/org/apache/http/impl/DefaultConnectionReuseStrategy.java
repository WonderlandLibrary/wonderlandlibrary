package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      Args.notNull(var2, "HTTP context");
      ProtocolVersion var3 = var1.getStatusLine().getProtocolVersion();
      Header var4 = var1.getFirstHeader("Transfer-Encoding");
      if (var4 != null) {
         if (!"chunked".equalsIgnoreCase(var4.getValue())) {
            return false;
         }
      } else if (this >= var1) {
         Header[] var5 = var1.getHeaders("Content-Length");
         if (var5.length != 1) {
            return false;
         }

         Header var6 = var5[0];

         try {
            int var7 = Integer.parseInt(var6.getValue());
            if (var7 < 0) {
               return false;
            }
         } catch (NumberFormatException var9) {
            return false;
         }
      }

      HeaderIterator var11 = var1.headerIterator("Connection");
      if (!var11.hasNext()) {
         var11 = var1.headerIterator("Proxy-Connection");
      }

      if (var11.hasNext()) {
         try {
            TokenIterator var12 = this.createTokenIterator(var11);
            boolean var13 = false;

            while(var12.hasNext()) {
               String var8 = var12.nextToken();
               if ("Close".equalsIgnoreCase(var8)) {
                  return false;
               }

               if ("Keep-Alive".equalsIgnoreCase(var8)) {
                  var13 = true;
               }
            }

            if (var13) {
               return true;
            }
         } catch (ParseException var10) {
            return false;
         }
      }

      return !var3.lessEquals(HttpVersion.HTTP_1_0);
   }

   protected TokenIterator createTokenIterator(HeaderIterator var1) {
      return new BasicTokenIterator(var1);
   }
}
