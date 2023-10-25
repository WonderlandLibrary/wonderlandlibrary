package com.mojang.authlib;

public abstract class HttpUserAuthentication extends BaseUserAuthentication {
  protected HttpUserAuthentication(HttpAuthenticationService authenticationService) {
    super(authenticationService);
  }
  
  public HttpAuthenticationService getAuthenticationService() {
    return (HttpAuthenticationService)super.getAuthenticationService();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\HttpUserAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */