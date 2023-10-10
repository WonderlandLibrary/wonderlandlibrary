package com.mojang.authlib.yggdrasil;

public class ProfileNotFoundException extends RuntimeException {
   public ProfileNotFoundException() {
   }

   public ProfileNotFoundException(String var1) {
      super(var1);
   }

   public ProfileNotFoundException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ProfileNotFoundException(Throwable var1) {
      super(var1);
   }
}
