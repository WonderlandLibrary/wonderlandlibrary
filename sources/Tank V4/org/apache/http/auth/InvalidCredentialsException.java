package org.apache.http.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public class InvalidCredentialsException extends AuthenticationException {
   private static final long serialVersionUID = -4834003835215460648L;

   public InvalidCredentialsException() {
   }

   public InvalidCredentialsException(String var1) {
      super(var1);
   }

   public InvalidCredentialsException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
