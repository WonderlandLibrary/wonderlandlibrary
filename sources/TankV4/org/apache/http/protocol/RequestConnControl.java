package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class RequestConnControl implements HttpRequestInterceptor {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      String var3 = var1.getRequestLine().getMethod();
      if (!var3.equalsIgnoreCase("CONNECT")) {
         if (!var1.containsHeader("Connection")) {
            var1.addHeader("Connection", "Keep-Alive");
         }

      }
   }
}
