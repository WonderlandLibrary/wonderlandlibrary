package com.mojang.realmsclient.client;

import java.net.Proxy;

public class RealmsClientConfig {
   private static Proxy proxy;

   public static Proxy getProxy() {
      return proxy;
   }

   public static void setProxy(Proxy var0) {
      if (proxy == null) {
         proxy = var0;
      }

   }
}
