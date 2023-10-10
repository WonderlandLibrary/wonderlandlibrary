package com.thealtening.auth.service;

import java.net.URL;

public final class ServiceSwitcher {
   private final String MINECRAFT_SESSION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
   private final String MINECRAFT_AUTHENTICATION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
   private final String[] WHITELISTED_DOMAINS = new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"};
   private final FieldAdapter minecraftSessionServer = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
   private final FieldAdapter userAuthentication = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");

   public ServiceSwitcher() {
      try {
         this.minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", this.WHITELISTED_DOMAINS);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public AlteningServiceType switchToService(AlteningServiceType var1) {
      try {
         String var2 = var1.getAuthServer();
         FieldAdapter var3 = this.userAuthentication;
         var3.updateFieldIfPresent("BASE_URL", var2);
         var3.updateFieldIfPresent("ROUTE_AUTHENTICATE", new URL(var2 + "authenticate"));
         var3.updateFieldIfPresent("ROUTE_INVALIDATE", new URL(var2 + "invalidate"));
         var3.updateFieldIfPresent("ROUTE_REFRESH", new URL(var2 + "refresh"));
         var3.updateFieldIfPresent("ROUTE_VALIDATE", new URL(var2 + "validate"));
         var3.updateFieldIfPresent("ROUTE_SIGNOUT", new URL(var2 + "signout"));
         String var4 = var1.getSessionServer();
         FieldAdapter var5 = this.minecraftSessionServer;
         var5.updateFieldIfPresent("BASE_URL", var4 + "session/minecraft/");
         var5.updateFieldIfPresent("JOIN_URL", new URL(var4 + "session/minecraft/join"));
         var5.updateFieldIfPresent("CHECK_URL", new URL(var4 + "session/minecraft/hasJoined"));
         return var1;
      } catch (Exception var6) {
         var6.printStackTrace();
         return AlteningServiceType.MOJANG;
      }
   }
}
