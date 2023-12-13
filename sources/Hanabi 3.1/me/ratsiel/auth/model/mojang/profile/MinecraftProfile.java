package me.ratsiel.auth.model.mojang.profile;

import java.util.List;
import java.util.UUID;

public class MinecraftProfile {
   private UUID uuid;
   private String username;
   private List skins;
   private List capes;

   public MinecraftProfile() {
   }

   public MinecraftProfile(UUID uuid, String username, List skins, List capes) {
      this.uuid = uuid;
      this.username = username;
      this.skins = skins;
      this.capes = capes;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getUsername() {
      return this.username;
   }

   public List getSkins() {
      return this.skins;
   }

   public List getCapes() {
      return this.capes;
   }

   public String toString() {
      return "MinecraftProfile{uuid=" + this.uuid + ", username='" + this.username + '\'' + ", skins=" + this.skins + ", capes=" + this.capes + '}';
   }
}
