package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestAuthCache implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      HttpClientContext var3 = HttpClientContext.adapt(var2);
      AuthCache var4 = var3.getAuthCache();
      if (var4 == null) {
         this.log.debug("Auth cache not set in the context");
      } else {
         CredentialsProvider var5 = var3.getCredentialsProvider();
         if (var5 == null) {
            this.log.debug("Credentials provider not set in the context");
         } else {
            RouteInfo var6 = var3.getHttpRoute();
            HttpHost var7 = var3.getTargetHost();
            if (var7.getPort() < 0) {
               var7 = new HttpHost(var7.getHostName(), var6.getTargetHost().getPort(), var7.getSchemeName());
            }

            AuthState var8 = var3.getTargetAuthState();
            if (var8 != null && var8.getState() == AuthProtocolState.UNCHALLENGED) {
               AuthScheme var9 = var4.get(var7);
               if (var9 != null) {
                  this.doPreemptiveAuth(var7, var9, var8, var5);
               }
            }

            HttpHost var12 = var6.getProxyHost();
            AuthState var10 = var3.getProxyAuthState();
            if (var12 != null && var10 != null && var10.getState() == AuthProtocolState.UNCHALLENGED) {
               AuthScheme var11 = var4.get(var12);
               if (var11 != null) {
                  this.doPreemptiveAuth(var12, var11, var10, var5);
               }
            }

         }
      }
   }

   private void doPreemptiveAuth(HttpHost var1, AuthScheme var2, AuthState var3, CredentialsProvider var4) {
      String var5 = var2.getSchemeName();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Re-using cached '" + var5 + "' auth scheme for " + var1);
      }

      AuthScope var6 = new AuthScope(var1, AuthScope.ANY_REALM, var5);
      Credentials var7 = var4.getCredentials(var6);
      if (var7 != null) {
         if ("BASIC".equalsIgnoreCase(var2.getSchemeName())) {
            var3.setState(AuthProtocolState.CHALLENGED);
         } else {
            var3.setState(AuthProtocolState.SUCCESS);
         }

         var3.update(var2, var7);
      } else {
         this.log.debug("No credentials for preemptive authentication");
      }

   }
}
