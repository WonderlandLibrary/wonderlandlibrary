package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
   final Log log = LogFactory.getLog(this.getClass());

   public RequestAuthenticationBase() {
   }

   void process(AuthState var1, HttpRequest var2, HttpContext var3) {
      AuthScheme var4 = var1.getAuthScheme();
      Credentials var5 = var1.getCredentials();
      switch(var1.getState()) {
      case FAILURE:
         return;
      case SUCCESS:
         this.ensureAuthScheme(var4);
         if (var4.isConnectionBased()) {
            return;
         }
         break;
      case CHALLENGED:
         Queue var6 = var1.getAuthOptions();
         if (var6 != null) {
            while(!var6.isEmpty()) {
               AuthOption var7 = (AuthOption)var6.remove();
               var4 = var7.getAuthScheme();
               var5 = var7.getCredentials();
               var1.update(var4, var5);
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Generating response to an authentication challenge using " + var4.getSchemeName() + " scheme");
               }

               try {
                  Header var8 = this.authenticate(var4, var5, var2, var3);
                  var2.addHeader(var8);
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
      }

      if (var4 != null) {
         try {
            Header var11 = this.authenticate(var4, var5, var2, var3);
            var2.addHeader(var11);
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

   private Header authenticate(AuthScheme var1, Credentials var2, HttpRequest var3, HttpContext var4) throws AuthenticationException {
      Asserts.notNull(var1, "Auth scheme");
      return var1 instanceof ContextAwareAuthScheme ? ((ContextAwareAuthScheme)var1).authenticate(var2, var3, var4) : var1.authenticate(var2, var3);
   }
}
