package com.mojang.authlib.exceptions;

public class UserMigratedException extends InvalidCredentialsException {
   public UserMigratedException() {
   }

   public UserMigratedException(String var1) {
      super(var1);
   }

   public UserMigratedException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public UserMigratedException(Throwable var1) {
      super(var1);
   }
}
