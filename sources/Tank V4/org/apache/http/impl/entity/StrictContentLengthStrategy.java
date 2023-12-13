package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Immutable
public class StrictContentLengthStrategy implements ContentLengthStrategy {
   public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
   private final int implicitLen;

   public StrictContentLengthStrategy(int var1) {
      this.implicitLen = var1;
   }

   public StrictContentLengthStrategy() {
      this(-1);
   }

   public long determineLength(HttpMessage var1) throws HttpException {
      Args.notNull(var1, "HTTP message");
      Header var2 = var1.getFirstHeader("Transfer-Encoding");
      if (var2 != null) {
         String var8 = var2.getValue();
         if ("chunked".equalsIgnoreCase(var8)) {
            if (var1.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
               throw new ProtocolException("Chunked transfer encoding not allowed for " + var1.getProtocolVersion());
            } else {
               return -2L;
            }
         } else if ("identity".equalsIgnoreCase(var8)) {
            return -1L;
         } else {
            throw new ProtocolException("Unsupported transfer encoding: " + var8);
         }
      } else {
         Header var3 = var1.getFirstHeader("Content-Length");
         if (var3 != null) {
            String var4 = var3.getValue();

            try {
               long var5 = Long.parseLong(var4);
               if (var5 < 0L) {
                  throw new ProtocolException("Negative content length: " + var4);
               } else {
                  return var5;
               }
            } catch (NumberFormatException var7) {
               throw new ProtocolException("Invalid content length: " + var4);
            }
         } else {
            return (long)this.implicitLen;
         }
      }
   }
}
