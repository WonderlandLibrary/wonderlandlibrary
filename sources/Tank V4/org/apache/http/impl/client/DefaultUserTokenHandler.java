package org.apache.http.impl.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultUserTokenHandler implements UserTokenHandler {
   public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();

   public Object getUserToken(HttpContext var1) {
      HttpClientContext var2 = HttpClientContext.adapt(var1);
      Principal var3 = null;
      AuthState var4 = var2.getTargetAuthState();
      if (var4 != null) {
         var3 = getAuthPrincipal(var4);
         if (var3 == null) {
            AuthState var5 = var2.getProxyAuthState();
            var3 = getAuthPrincipal(var5);
         }
      }

      if (var3 == null) {
         HttpConnection var7 = var2.getConnection();
         if (var7.isOpen() && var7 instanceof ManagedHttpClientConnection) {
            SSLSession var6 = ((ManagedHttpClientConnection)var7).getSSLSession();
            if (var6 != null) {
               var3 = var6.getLocalPrincipal();
            }
         }
      }

      return var3;
   }

   private static Principal getAuthPrincipal(AuthState var0) {
      AuthScheme var1 = var0.getAuthScheme();
      if (var1 != null && var1.isComplete() && var1.isConnectionBased()) {
         Credentials var2 = var0.getCredentials();
         if (var2 != null) {
            return var2.getUserPrincipal();
         }
      }

      return null;
   }
}
