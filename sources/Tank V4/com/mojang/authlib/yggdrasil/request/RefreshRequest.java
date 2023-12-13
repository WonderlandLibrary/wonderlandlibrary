package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class RefreshRequest {
   private String clientToken;
   private String accessToken;
   private GameProfile selectedProfile;
   private boolean requestUser;

   public RefreshRequest(YggdrasilUserAuthentication var1) {
      this(var1, (GameProfile)null);
   }

   public RefreshRequest(YggdrasilUserAuthentication var1, GameProfile var2) {
      this.requestUser = true;
      this.clientToken = var1.getAuthenticationService().getClientToken();
      this.accessToken = var1.getAuthenticatedToken();
      this.selectedProfile = var2;
   }
}
