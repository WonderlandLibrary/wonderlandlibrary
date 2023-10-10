package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class InvalidateRequest {
   private String accessToken;
   private String clientToken;

   public InvalidateRequest(YggdrasilUserAuthentication var1) {
      this.accessToken = var1.getAuthenticatedToken();
      this.clientToken = var1.getAuthenticationService().getClientToken();
   }
}
