package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseConnControl implements HttpResponseInterceptor {
   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      HttpCoreContext var3 = HttpCoreContext.adapt(var2);
      int var4 = var1.getStatusLine().getStatusCode();
      if (var4 != 400 && var4 != 408 && var4 != 411 && var4 != 413 && var4 != 414 && var4 != 503 && var4 != 501) {
         Header var5 = var1.getFirstHeader("Connection");
         if (var5 == null || !"Close".equalsIgnoreCase(var5.getValue())) {
            HttpEntity var6 = var1.getEntity();
            if (var6 != null) {
               ProtocolVersion var7 = var1.getStatusLine().getProtocolVersion();
               if (var6.getContentLength() < 0L && (!var6.isChunked() || var7.lessEquals(HttpVersion.HTTP_1_0))) {
                  var1.setHeader("Connection", "Close");
                  return;
               }
            }

            HttpRequest var9 = var3.getRequest();
            if (var9 != null) {
               Header var8 = var9.getFirstHeader("Connection");
               if (var8 != null) {
                  var1.setHeader("Connection", var8.getValue());
               } else if (var9.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                  var1.setHeader("Connection", "Close");
               }
            }

         }
      } else {
         var1.setHeader("Connection", "Close");
      }
   }
}
