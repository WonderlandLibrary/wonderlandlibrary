package com.thealtening.auth.service;

public enum AlteningServiceType {
  MOJANG("https://authserver.mojang.com/", "https://sessionserver.mojang.com/"),
  THEALTENING("http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/");
  
  private final String authServer;
  
  private final String sessionServer;
  
  AlteningServiceType(String authServer, String sessionServer) {
    this.authServer = authServer;
    this.sessionServer = sessionServer;
  }
  
  public String getAuthServer() {
    return this.authServer;
  }
  
  public String getSessionServer() {
    return this.sessionServer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\thealtening\auth\service\AlteningServiceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */