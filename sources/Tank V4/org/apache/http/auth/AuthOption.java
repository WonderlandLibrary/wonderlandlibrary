package org.apache.http.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class AuthOption {
   private final AuthScheme authScheme;
   private final Credentials creds;

   public AuthOption(AuthScheme var1, Credentials var2) {
      Args.notNull(var1, "Auth scheme");
      Args.notNull(var2, "User credentials");
      this.authScheme = var1;
      this.creds = var2;
   }

   public AuthScheme getAuthScheme() {
      return this.authScheme;
   }

   public Credentials getCredentials() {
      return this.creds;
   }

   public String toString() {
      return this.authScheme.toString();
   }
}
