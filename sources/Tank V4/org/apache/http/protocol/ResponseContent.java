package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseContent implements HttpResponseInterceptor {
   private final boolean overwrite;

   public ResponseContent() {
      this(false);
   }

   public ResponseContent(boolean var1) {
      this.overwrite = var1;
   }

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      if (this.overwrite) {
         var1.removeHeaders("Transfer-Encoding");
         var1.removeHeaders("Content-Length");
      } else {
         if (var1.containsHeader("Transfer-Encoding")) {
            throw new ProtocolException("Transfer-encoding header already present");
         }

         if (var1.containsHeader("Content-Length")) {
            throw new ProtocolException("Content-Length header already present");
         }
      }

      ProtocolVersion var3 = var1.getStatusLine().getProtocolVersion();
      HttpEntity var4 = var1.getEntity();
      if (var4 != null) {
         long var5 = var4.getContentLength();
         if (var4.isChunked() && !var3.lessEquals(HttpVersion.HTTP_1_0)) {
            var1.addHeader("Transfer-Encoding", "chunked");
         } else if (var5 >= 0L) {
            var1.addHeader("Content-Length", Long.toString(var4.getContentLength()));
         }

         if (var4.getContentType() != null && !var1.containsHeader("Content-Type")) {
            var1.addHeader(var4.getContentType());
         }

         if (var4.getContentEncoding() != null && !var1.containsHeader("Content-Encoding")) {
            var1.addHeader(var4.getContentEncoding());
         }
      } else {
         int var7 = var1.getStatusLine().getStatusCode();
         if (var7 != 204 && var7 != 304 && var7 != 205) {
            var1.addHeader("Content-Length", "0");
         }
      }

   }
}
