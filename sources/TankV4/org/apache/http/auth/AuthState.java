package org.apache.http.auth;

import java.util.Collection;
import java.util.Queue;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class AuthState {
   private AuthProtocolState state;
   private AuthScheme authScheme;
   private AuthScope authScope;
   private Credentials credentials;
   private Queue authOptions;

   public AuthState() {
      this.state = AuthProtocolState.UNCHALLENGED;
   }

   public void reset() {
      this.state = AuthProtocolState.UNCHALLENGED;
      this.authOptions = null;
      this.authScheme = null;
      this.authScope = null;
      this.credentials = null;
   }

   public AuthProtocolState getState() {
      return this.state;
   }

   public void setState(AuthProtocolState var1) {
      this.state = var1 != null ? var1 : AuthProtocolState.UNCHALLENGED;
   }

   public AuthScheme getAuthScheme() {
      return this.authScheme;
   }

   public Credentials getCredentials() {
      return this.credentials;
   }

   public void update(AuthScheme var1, Credentials var2) {
      Args.notNull(var1, "Auth scheme");
      Args.notNull(var2, "Credentials");
      this.authScheme = var1;
      this.credentials = var2;
      this.authOptions = null;
   }

   public Queue getAuthOptions() {
      return this.authOptions;
   }

   public boolean hasAuthOptions() {
      return this.authOptions != null && !this.authOptions.isEmpty();
   }

   public void update(Queue var1) {
      Args.notEmpty((Collection)var1, "Queue of auth options");
      this.authOptions = var1;
      this.authScheme = null;
      this.credentials = null;
   }

   /** @deprecated */
   @Deprecated
   public void invalidate() {
      this.reset();
   }

   /** @deprecated */
   @Deprecated
   public boolean isValid() {
      return this.authScheme != null;
   }

   /** @deprecated */
   @Deprecated
   public void setAuthScheme(AuthScheme var1) {
      if (var1 == null) {
         this.reset();
      } else {
         this.authScheme = var1;
      }
   }

   /** @deprecated */
   @Deprecated
   public void setCredentials(Credentials var1) {
      this.credentials = var1;
   }

   /** @deprecated */
   @Deprecated
   public AuthScope getAuthScope() {
      return this.authScope;
   }

   /** @deprecated */
   @Deprecated
   public void setAuthScope(AuthScope var1) {
      this.authScope = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("state:").append(this.state).append(";");
      if (this.authScheme != null) {
         var1.append("auth scheme:").append(this.authScheme.getSchemeName()).append(";");
      }

      if (this.credentials != null) {
         var1.append("credentials present");
      }

      return var1.toString();
   }
}
