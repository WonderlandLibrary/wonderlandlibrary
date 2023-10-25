package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.GameProfile;

public class AuthenticationResponse extends Response {
  private String accessToken;
  
  private String clientToken;
  
  private GameProfile selectedProfile;
  
  private GameProfile[] availableProfiles;
  
  private User user;
  
  public String getAccessToken() {
    return this.accessToken;
  }
  
  public String getClientToken() {
    return this.clientToken;
  }
  
  public GameProfile[] getAvailableProfiles() {
    return this.availableProfiles;
  }
  
  public GameProfile getSelectedProfile() {
    return this.selectedProfile;
  }
  
  public User getUser() {
    return this.user;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\response\AuthenticationResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */