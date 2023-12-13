package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor {
   private final Collection defaultHeaders;

   public RequestDefaultHeaders(Collection var1) {
      this.defaultHeaders = var1;
   }

   public RequestDefaultHeaders() {
      this((Collection)null);
   }

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      String var3 = var1.getRequestLine().getMethod();
      if (!var3.equalsIgnoreCase("CONNECT")) {
         Collection var4 = (Collection)var1.getParams().getParameter("http.default-headers");
         if (var4 == null) {
            var4 = this.defaultHeaders;
         }

         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               Header var6 = (Header)var5.next();
               var1.addHeader(var6);
            }
         }

      }
   }
}
