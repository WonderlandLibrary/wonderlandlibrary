package org.apache.http.impl.client;

import org.apache.commons.logging.Log;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
public class HttpAuthenticator extends org.apache.http.impl.auth.HttpAuthenticator {
   public HttpAuthenticator(Log var1) {
      super(var1);
   }

   public HttpAuthenticator() {
   }

   public boolean authenticate(HttpHost var1, HttpResponse var2, AuthenticationStrategy var3, AuthState var4, HttpContext var5) {
      return this.handleAuthChallenge(var1, var2, var3, var4, var5);
   }
}
