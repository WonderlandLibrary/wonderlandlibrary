package com.thealtening.auth.service;

public enum AlteningServiceType {
   MOJANG("https://authserver.mojang.com/", "https://sessionserver.mojang.com/"),
   THEALTENING("http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/");

   private final String authServer;
   private final String sessionServer;
   private static final AlteningServiceType[] ENUM$VALUES = new AlteningServiceType[]{MOJANG, THEALTENING};

   private AlteningServiceType(String var3, String var4) {
      this.authServer = var3;
      this.sessionServer = var4;
   }

   public String getAuthServer() {
      return this.authServer;
   }

   public String getSessionServer() {
      return this.sessionServer;
   }
}
