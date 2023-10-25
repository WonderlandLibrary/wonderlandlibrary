package com.mojang.authlib.minecraft;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.HttpAuthenticationService;

public abstract class HttpMinecraftSessionService extends BaseMinecraftSessionService {
  protected HttpMinecraftSessionService(HttpAuthenticationService authenticationService) {
    super((AuthenticationService)authenticationService);
  }
  
  public HttpAuthenticationService getAuthenticationService() {
    return (HttpAuthenticationService)super.getAuthenticationService();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\minecraft\HttpMinecraftSessionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */