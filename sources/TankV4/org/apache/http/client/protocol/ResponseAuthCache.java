package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class ResponseAuthCache implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      Object var3 = (AuthCache)var2.getAttribute("http.auth.auth-cache");
      HttpHost var4 = (HttpHost)var2.getAttribute("http.target_host");
      AuthState var5 = (AuthState)var2.getAttribute("http.auth.target-scope");
      if (var4 != null && var5 != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Target auth state: " + var5.getState());
         }

         if (var5 != null) {
            SchemeRegistry var6 = (SchemeRegistry)var2.getAttribute("http.scheme-registry");
            if (var4.getPort() < 0) {
               Scheme var7 = var6.getScheme(var4);
               var4 = new HttpHost(var4.getHostName(), var7.resolvePort(var4.getPort()), var4.getSchemeName());
            }

            if (var3 == null) {
               var3 = new BasicAuthCache();
               var2.setAttribute("http.auth.auth-cache", var3);
            }

            switch(var5.getState()) {
            case CHALLENGED:
               this.cache((AuthCache)var3, var4, var5.getAuthScheme());
               break;
            case FAILURE:
               this.uncache((AuthCache)var3, var4, var5.getAuthScheme());
            }
         }
      }

      HttpHost var8 = (HttpHost)var2.getAttribute("http.proxy_host");
      AuthState var9 = (AuthState)var2.getAttribute("http.auth.proxy-scope");
      if (var8 != null && var9 != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Proxy auth state: " + var9.getState());
         }

         if (var9 != null) {
            if (var3 == null) {
               var3 = new BasicAuthCache();
               var2.setAttribute("http.auth.auth-cache", var3);
            }

            switch(var9.getState()) {
            case CHALLENGED:
               this.cache((AuthCache)var3, var8, var9.getAuthScheme());
               break;
            case FAILURE:
               this.uncache((AuthCache)var3, var8, var9.getAuthScheme());
            }
         }
      }

   }

   private void cache(AuthCache var1, HttpHost var2, AuthScheme var3) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Caching '" + var3.getSchemeName() + "' auth scheme for " + var2);
      }

      var1.put(var2, var3);
   }

   private void uncache(AuthCache var1, HttpHost var2, AuthScheme var3) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Removing from cache '" + var3.getSchemeName() + "' auth scheme for " + var2);
      }

      var1.remove(var2);
   }
}
