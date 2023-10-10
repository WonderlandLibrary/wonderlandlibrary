package org.apache.http.impl.client;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
class AuthenticationStrategyAdaptor implements AuthenticationStrategy {
   private final Log log = LogFactory.getLog(this.getClass());
   private final AuthenticationHandler handler;

   public AuthenticationStrategyAdaptor(AuthenticationHandler var1) {
      this.handler = var1;
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3) {
      return this.handler.isAuthenticationRequested(var2, var3);
   }

   public Map getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException {
      return this.handler.getChallenges(var2, var3);
   }

   public Queue select(Map var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException {
      Args.notNull(var1, "Map of auth challenges");
      Args.notNull(var2, "Host");
      Args.notNull(var3, "HTTP response");
      Args.notNull(var4, "HTTP context");
      LinkedList var5 = new LinkedList();
      CredentialsProvider var6 = (CredentialsProvider)var4.getAttribute("http.auth.credentials-provider");
      if (var6 == null) {
         this.log.debug("Credentials provider not set in the context");
         return var5;
      } else {
         AuthScheme var7;
         try {
            var7 = this.handler.selectScheme(var1, var3, var4);
         } catch (AuthenticationException var12) {
            if (this.log.isWarnEnabled()) {
               this.log.warn(var12.getMessage(), var12);
            }

            return var5;
         }

         String var8 = var7.getSchemeName();
         Header var9 = (Header)var1.get(var8.toLowerCase(Locale.US));
         var7.processChallenge(var9);
         AuthScope var10 = new AuthScope(var2.getHostName(), var2.getPort(), var7.getRealm(), var7.getSchemeName());
         Credentials var11 = var6.getCredentials(var10);
         if (var11 != null) {
            var5.add(new AuthOption(var7, var11));
         }

         return var5;
      }
   }

   public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3) {
      Object var4 = (AuthCache)var3.getAttribute("http.auth.auth-cache");
      if (var2 != null) {
         if (var4 == null) {
            var4 = new BasicAuthCache();
            var3.setAttribute("http.auth.auth-cache", var4);
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("Caching '" + var2.getSchemeName() + "' auth scheme for " + var1);
         }

         ((AuthCache)var4).put(var1, var2);
      }

   }

   public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3) {
      AuthCache var4 = (AuthCache)var3.getAttribute("http.auth.auth-cache");
      if (var4 != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Removing from cache '" + var2.getSchemeName() + "' auth scheme for " + var1);
         }

         var4.remove(var1);
      }
   }

   public AuthenticationHandler getHandler() {
      return this.handler;
   }
}
