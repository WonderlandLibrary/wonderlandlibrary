package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class RefreshRequest {
  private String clientToken;
  
  private String accessToken;
  
  private GameProfile selectedProfile;
  
  private boolean requestUser = true;
  
  public RefreshRequest(YggdrasilUserAuthentication authenticationService) {
    this(authenticationService, null);
  }
  
  public RefreshRequest(YggdrasilUserAuthentication authenticationService, GameProfile profile) {
    this.clientToken = authenticationService.getAuthenticationService().getClientToken();
    this.accessToken = authenticationService.getAuthenticatedToken();
    this.selectedProfile = profile;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\request\RefreshRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */