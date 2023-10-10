package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
abstract class AuthenticationStrategyImpl implements AuthenticationStrategy {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final List DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("negotiate", "Kerberos", "NTLM", "Digest", "Basic"));
   private final int challengeCode;
   private final String headerName;

   AuthenticationStrategyImpl(int var1, String var2) {
      this.challengeCode = var1;
      this.headerName = var2;
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3) {
      Args.notNull(var2, "HTTP response");
      int var4 = var2.getStatusLine().getStatusCode();
      return var4 == this.challengeCode;
   }

   public Map getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException {
      Args.notNull(var2, "HTTP response");
      Header[] var4 = var2.getHeaders(this.headerName);
      HashMap var5 = new HashMap(var4.length);
      Header[] var6 = var4;
      int var7 = var4.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Header var9 = var6[var8];
         CharArrayBuffer var10;
         int var11;
         if (var9 instanceof FormattedHeader) {
            var10 = ((FormattedHeader)var9).getBuffer();
            var11 = ((FormattedHeader)var9).getValuePos();
         } else {
            String var12 = var9.getValue();
            if (var12 == null) {
               throw new MalformedChallengeException("Header value is null");
            }

            var10 = new CharArrayBuffer(var12.length());
            var10.append(var12);
            var11 = 0;
         }

         while(var11 < var10.length() && HTTP.isWhitespace(var10.charAt(var11))) {
            ++var11;
         }

         int var15;
         for(var15 = var11; var11 < var10.length() && !HTTP.isWhitespace(var10.charAt(var11)); ++var11) {
         }

         String var14 = var10.substring(var15, var11);
         var5.put(var14.toLowerCase(Locale.US), var9);
      }

      return var5;
   }

   abstract Collection getPreferredAuthSchemes(RequestConfig var1);

   public Queue select(Map var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException {
      Args.notNull(var1, "Map of auth challenges");
      Args.notNull(var2, "Host");
      Args.notNull(var3, "HTTP response");
      Args.notNull(var4, "HTTP context");
      HttpClientContext var5 = HttpClientContext.adapt(var4);
      LinkedList var6 = new LinkedList();
      Lookup var7 = var5.getAuthSchemeRegistry();
      if (var7 == null) {
         this.log.debug("Auth scheme registry not set in the context");
         return var6;
      } else {
         CredentialsProvider var8 = var5.getCredentialsProvider();
         if (var8 == null) {
            this.log.debug("Credentials provider not set in the context");
            return var6;
         } else {
            RequestConfig var9 = var5.getRequestConfig();
            Object var10 = this.getPreferredAuthSchemes(var9);
            if (var10 == null) {
               var10 = DEFAULT_SCHEME_PRIORITY;
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug("Authentication schemes in the order of preference: " + var10);
            }

            Iterator var11 = ((Collection)var10).iterator();

            while(var11.hasNext()) {
               String var12 = (String)var11.next();
               Header var13 = (Header)var1.get(var12.toLowerCase(Locale.US));
               if (var13 != null) {
                  AuthSchemeProvider var14 = (AuthSchemeProvider)var7.lookup(var12);
                  if (var14 == null) {
                     if (this.log.isWarnEnabled()) {
                        this.log.warn("Authentication scheme " + var12 + " not supported");
                     }
                  } else {
                     AuthScheme var15 = var14.create(var4);
                     var15.processChallenge(var13);
                     AuthScope var16 = new AuthScope(var2.getHostName(), var2.getPort(), var15.getRealm(), var15.getSchemeName());
                     Credentials var17 = var8.getCredentials(var16);
                     if (var17 != null) {
                        var6.add(new AuthOption(var15, var17));
                     }
                  }
               } else if (this.log.isDebugEnabled()) {
                  this.log.debug("Challenge for " + var12 + " authentication scheme not available");
               }
            }

            return var6;
         }
      }
   }

   public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3) {
      Args.notNull(var1, "Host");
      Args.notNull(var2, "Auth scheme");
      Args.notNull(var3, "HTTP context");
      HttpClientContext var4 = HttpClientContext.adapt(var3);
      if (var2 != null) {
         Object var5 = var4.getAuthCache();
         if (var5 == null) {
            var5 = new BasicAuthCache();
            var4.setAuthCache((AuthCache)var5);
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("Caching '" + var2.getSchemeName() + "' auth scheme for " + var1);
         }

         ((AuthCache)var5).put(var1, var2);
      }

   }

   public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3) {
      Args.notNull(var1, "Host");
      Args.notNull(var3, "HTTP context");
      HttpClientContext var4 = HttpClientContext.adapt(var3);
      AuthCache var5 = var4.getAuthCache();
      if (var5 != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Clearing cached auth scheme for " + var1);
         }

         var5.remove(var1);
      }

   }
}
