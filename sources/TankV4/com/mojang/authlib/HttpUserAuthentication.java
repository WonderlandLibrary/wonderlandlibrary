package com.mojang.authlib;

public abstract class HttpUserAuthentication extends BaseUserAuthentication {
   protected HttpUserAuthentication(HttpAuthenticationService var1) {
      super(var1);
   }

   public HttpAuthenticationService getAuthenticationService() {
      return (HttpAuthenticationService)super.getAuthenticationService();
   }

   public AuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }
}
