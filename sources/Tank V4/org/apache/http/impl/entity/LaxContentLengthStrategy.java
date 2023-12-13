package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Immutable
public class LaxContentLengthStrategy implements ContentLengthStrategy {
   public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();
   private final int implicitLen;

   public LaxContentLengthStrategy(int var1) {
      this.implicitLen = var1;
   }

   public LaxContentLengthStrategy() {
      this(-1);
   }

   public long determineLength(HttpMessage var1) throws HttpException {
      Args.notNull(var1, "HTTP message");
      Header var2 = var1.getFirstHeader("Transfer-Encoding");
      if (var2 != null) {
         HeaderElement[] var12;
         try {
            var12 = var2.getElements();
         } catch (ParseException var10) {
            throw new ProtocolException("Invalid Transfer-Encoding header value: " + var2, var10);
         }

         int var13 = var12.length;
         if ("identity".equalsIgnoreCase(var2.getValue())) {
            return -1L;
         } else {
            return var13 > 0 && "chunked".equalsIgnoreCase(var12[var13 - 1].getName()) ? -2L : -1L;
         }
      } else {
         Header var3 = var1.getFirstHeader("Content-Length");
         if (var3 == null) {
            return (long)this.implicitLen;
         } else {
            long var4 = -1L;
            Header[] var6 = var1.getHeaders("Content-Length");
            int var7 = var6.length - 1;

            while(var7 >= 0) {
               Header var8 = var6[var7];

               try {
                  var4 = Long.parseLong(var8.getValue());
                  break;
               } catch (NumberFormatException var11) {
                  --var7;
               }
            }

            return var4 >= 0L ? var4 : -1L;
         }
      }
   }
}
