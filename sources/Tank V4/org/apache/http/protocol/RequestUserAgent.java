package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Immutable
public class RequestUserAgent implements HttpRequestInterceptor {
   private final String userAgent;

   public RequestUserAgent(String var1) {
      this.userAgent = var1;
   }

   public RequestUserAgent() {
      this((String)null);
   }

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      if (!var1.containsHeader("User-Agent")) {
         String var3 = null;
         HttpParams var4 = var1.getParams();
         if (var4 != null) {
            var3 = (String)var4.getParameter("http.useragent");
         }

         if (var3 == null) {
            var3 = this.userAgent;
         }

         if (var3 != null) {
            var1.addHeader("User-Agent", var3);
         }
      }

   }
}
