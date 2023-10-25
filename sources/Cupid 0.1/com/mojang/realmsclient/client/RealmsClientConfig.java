package com.mojang.realmsclient.client;

import java.net.Proxy;

public class RealmsClientConfig {
  private static Proxy proxy;
  
  public static Proxy getProxy() {
    return proxy;
  }
  
  public static void setProxy(Proxy proxy) {
    if (RealmsClientConfig.proxy == null)
      RealmsClientConfig.proxy = proxy; 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\client\RealmsClientConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */