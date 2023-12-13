package me.ratsiel.auth.abstracts.exception;

public class AuthenticationException extends RuntimeException {
   public AuthenticationException(String message) {
      super(message);
   }
}
