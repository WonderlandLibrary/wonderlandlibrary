package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class ValidateRequest {
  private String clientToken;
  
  private String accessToken;
  
  public ValidateRequest(YggdrasilUserAuthentication authenticationService) {
    this.clientToken = authenticationService.getAuthenticationService().getClientToken();
    this.accessToken = authenticationService.getAuthenticatedToken();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\request\ValidateRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */