package me.ratsiel.auth.model.mojang;

public class MinecraftToken {
   private String accessToken;
   private String username;

   public MinecraftToken() {
   }

   public MinecraftToken(String accessToken, String username) {
      this.accessToken = accessToken;
      this.username = username;
   }

   public String getAccessToken() {
      return this.accessToken;
   }

   public String getUsername() {
      return this.username;
   }

   public String toString() {
      return "MinecraftToken{accessToken='" + this.accessToken + '\'' + ", username='" + this.username + '\'' + '}';
   }
}
