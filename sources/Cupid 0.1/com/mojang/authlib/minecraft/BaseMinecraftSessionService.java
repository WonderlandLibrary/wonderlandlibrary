package com.mojang.authlib.minecraft;

import com.mojang.authlib.AuthenticationService;

public abstract class BaseMinecraftSessionService implements MinecraftSessionService {
  private final AuthenticationService authenticationService;
  
  protected BaseMinecraftSessionService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }
  
  public AuthenticationService getAuthenticationService() {
    return this.authenticationService;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\minecraft\BaseMinecraftSessionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */