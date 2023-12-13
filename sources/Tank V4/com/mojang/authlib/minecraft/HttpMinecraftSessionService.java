package com.mojang.authlib.minecraft;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.HttpAuthenticationService;

public abstract class HttpMinecraftSessionService extends BaseMinecraftSessionService {
   protected HttpMinecraftSessionService(HttpAuthenticationService var1) {
      super(var1);
   }

   public HttpAuthenticationService getAuthenticationService() {
      return (HttpAuthenticationService)super.getAuthenticationService();
   }

   public AuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }
}
