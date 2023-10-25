package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class InvalidateRequest {
  private String accessToken;
  
  private String clientToken;
  
  public InvalidateRequest(YggdrasilUserAuthentication authenticationService) {
    this.accessToken = authenticationService.getAuthenticatedToken();
    this.clientToken = authenticationService.getAuthenticationService().getClientToken();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\request\InvalidateRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */