package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthState;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class RequestTargetAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      String var3 = var1.getRequestLine().getMethod();
      if (!var3.equalsIgnoreCase("CONNECT")) {
         if (!var1.containsHeader("Authorization")) {
            AuthState var4 = (AuthState)var2.getAttribute("http.auth.target-scope");
            if (var4 == null) {
               this.log.debug("Target auth state not set in the context");
            } else {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Target auth state: " + var4.getState());
               }

               this.process(var4, var1, var2);
            }
         }
      }
   }
}
