package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class RequestProxyAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      if (!var1.containsHeader("Proxy-Authorization")) {
         HttpRoutedConnection var3 = (HttpRoutedConnection)var2.getAttribute("http.connection");
         if (var3 == null) {
            this.log.debug("HTTP connection not set in the context");
         } else {
            HttpRoute var4 = var3.getRoute();
            if (!var4.isTunnelled()) {
               AuthState var5 = (AuthState)var2.getAttribute("http.auth.proxy-scope");
               if (var5 == null) {
                  this.log.debug("Proxy auth state not set in the context");
               } else {
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("Proxy auth state: " + var5.getState());
                  }

                  this.process(var5, var1, var2);
               }
            }
         }
      }
   }
}
