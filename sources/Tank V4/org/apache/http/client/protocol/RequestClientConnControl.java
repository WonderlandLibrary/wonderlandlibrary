package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestClientConnControl implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      String var3 = var1.getRequestLine().getMethod();
      if (var3.equalsIgnoreCase("CONNECT")) {
         var1.setHeader("Proxy-Connection", "Keep-Alive");
      } else {
         HttpClientContext var4 = HttpClientContext.adapt(var2);
         RouteInfo var5 = var4.getHttpRoute();
         if (var5 == null) {
            this.log.debug("Connection route not set in the context");
         } else {
            if ((var5.getHopCount() == 1 || var5.isTunnelled()) && !var1.containsHeader("Connection")) {
               var1.addHeader("Connection", "Keep-Alive");
            }

            if (var5.getHopCount() == 2 && !var5.isTunnelled() && !var1.containsHeader("Proxy-Connection")) {
               var1.addHeader("Proxy-Connection", "Keep-Alive");
            }

         }
      }
   }
}
