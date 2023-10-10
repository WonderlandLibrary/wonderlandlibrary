package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class ValidateRequest {
   private String clientToken;
   private String accessToken;

   public ValidateRequest(YggdrasilUserAuthentication var1) {
      this.clientToken = var1.getAuthenticationService().getClientToken();
      this.accessToken = var1.getAuthenticatedToken();
   }
}
