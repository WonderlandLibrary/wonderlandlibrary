package com.mojang.authlib.exceptions;

public class AuthenticationException extends Exception {
   public AuthenticationException() {
   }

   public AuthenticationException(String var1) {
      super(var1);
   }

   public AuthenticationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public AuthenticationException(Throwable var1) {
      super(var1);
   }
}
