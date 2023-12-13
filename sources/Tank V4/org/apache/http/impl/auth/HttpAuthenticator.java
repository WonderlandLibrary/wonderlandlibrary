package org.apache.http.impl.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public class HttpAuthenticator {
   private final Log log;

   public HttpAuthenticator(Log var1) {
      this.log = var1 != null ? var1 : LogFactory.getLog(this.getClass());
   }

   public HttpAuthenticator() {
      this((Log)null);
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, AuthenticationStrategy var3, AuthState var4, HttpContext var5) {
      if (var3.isAuthenticationRequested(var1, var2, var5)) {
         this.log.debug("Authentication required");
         if (var4.getState() == AuthProtocolState.SUCCESS) {
            var3.authFailed(var1, var4.getAuthScheme(), var5);
         }

         return true;
      } else {
         switch(var4.getState()) {
         case CHALLENGED:
         case HANDSHAKE:
            this.log.debug("Authentication succeeded");
            var4.setState(AuthProtocolState.SUCCESS);
            var3.authSucceeded(var1, var4.getAuthScheme(), var5);
         case SUCCESS:
            break;
         default:
            var4.setState(AuthProtocolState.UNCHALLENGED);
         }

         return false;
      }
   }

   public boolean handleAuthChallenge(HttpHost var1, HttpResponse var2, AuthenticationStrategy var3, AuthState var4, HttpContext var5) {
      try {
         if (this.log.isDebugEnabled()) {
            this.log.debug(var1.toHostString() + " requested authentication");
         }

         Map var6 = var3.getChallenges(var1, var2, var5);
         if (var6.isEmpty()) {
            this.log.debug("Response contains no authentication challenges");
            return false;
         } else {
            AuthScheme var7 = var4.getAuthScheme();
            switch(var4.getState()) {
            case CHALLENGED:
            case HANDSHAKE:
               if (var7 == null) {
                  this.log.debug("Auth scheme is null");
                  var3.authFailed(var1, (AuthScheme)null, var5);
                  var4.reset();
                  var4.setState(AuthProtocolState.FAILURE);
                  return false;
               }
            case UNCHALLENGED:
               if (var7 != null) {
                  String var8 = var7.getSchemeName();
                  Header var9 = (Header)var6.get(var8.toLowerCase(Locale.US));
                  if (var9 != null) {
                     this.log.debug("Authorization challenge processed");
                     var7.processChallenge(var9);
                     if (var7.isComplete()) {
                        this.log.debug("Authentication failed");
                        var3.authFailed(var1, var4.getAuthScheme(), var5);
                        var4.reset();
                        var4.setState(AuthProtocolState.FAILURE);
                        return false;
                     }

                     var4.setState(AuthProtocolState.HANDSHAKE);
                     return true;
                  }

                  var4.reset();
               }
               break;
            case SUCCESS:
               var4.reset();
               break;
            case FAILURE:
               return false;
            }

            Queue var11 = var3.select(var6, var1, var2, var5);
            if (var11 != null && !var11.isEmpty()) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Selected authentication options: " + var11);
               }

               var4.setState(AuthProtocolState.CHALLENGED);
               var4.update(var11);
               return true;
            } else {
               return false;
            }
         }
      } catch (MalformedChallengeException var10) {
         if (this.log.isWarnEnabled()) {
            this.log.warn("Malformed challenge: " + var10.getMessage());
         }

         var4.reset();
         return false;
      }
   }

   public void generateAuthResponse(HttpRequest var1, AuthState var2, HttpContext var3) throws HttpException, IOException {
      AuthScheme var4 = var2.getAuthScheme();
      Credentials var5 = var2.getCredentials();
      switch(var2.getState()) {
      case CHALLENGED:
         Queue var6 = var2.getAuthOptions();
         if (var6 != null) {
            while(!var6.isEmpty()) {
               AuthOption var7 = (AuthOption)var6.remove();
               var4 = var7.getAuthScheme();
               var5 = var7.getCredentials();
               var2.update(var4, var5);
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Generating response to an authentication challenge using " + var4.getSchemeName() + " scheme");
               }

               try {
                  Header var8 = this.doAuth(var4, var5, var1, var3);
                  var1.addHeader(var8);
                  break;
               } catch (AuthenticationException var9) {
                  if (this.log.isWarnEnabled()) {
                     this.log.warn(var4 + " authentication error: " + var9.getMessage());
                  }
               }
            }

            return;
         }

         this.ensureAuthScheme(var4);
      case HANDSHAKE:
      default:
         break;
      case SUCCESS:
         this.ensureAuthScheme(var4);
         if (var4.isConnectionBased()) {
            return;
         }
         break;
      case FAILURE:
         return;
      }

      if (var4 != null) {
         try {
            Header var11 = this.doAuth(var4, var5, var1, var3);
            var1.addHeader(var11);
         } catch (AuthenticationException var10) {
            if (this.log.isErrorEnabled()) {
               this.log.error(var4 + " authentication error: " + var10.getMessage());
            }
         }
      }

   }

   private void ensureAuthScheme(AuthScheme var1) {
      Asserts.notNull(var1, "Auth scheme");
   }

   private Header doAuth(AuthScheme var1, Credentials var2, HttpRequest var3, HttpContext var4) throws AuthenticationException {
      return var1 instanceof ContextAwareAuthScheme ? ((ContextAwareAuthScheme)var1).authenticate(var2, var3, var4) : var1.authenticate(var2, var3);
   }
}
