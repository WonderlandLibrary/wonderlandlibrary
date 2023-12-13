package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class AuthenticationRequest {
   private Agent agent;
   private String username;
   private String password;
   private String clientToken;
   private boolean requestUser = true;

   public AuthenticationRequest(YggdrasilUserAuthentication var1, String var2, String var3) {
      this.agent = var1.getAgent();
      this.username = var2;
      this.clientToken = var1.getAuthenticationService().getClientToken();
      this.password = var3;
   }
}
