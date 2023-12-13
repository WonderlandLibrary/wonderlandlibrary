package me.ratsiel.auth.model.mojang.profile;

import me.ratsiel.auth.abstracts.TextureVariable;

public class MinecraftSkin extends TextureVariable {
   private String variant;

   public MinecraftSkin() {
   }

   public MinecraftSkin(String variant) {
      this.variant = variant;
   }

   public MinecraftSkin(String id, String state, String url, String alias, String variant) {
      super(id, state, url, alias);
      this.variant = variant;
   }

   public String getVariant() {
      return this.variant;
   }

   public String toString() {
      return "MinecraftSkin{id='" + this.getId() + '\'' + ", state='" + this.getState() + '\'' + ", url='" + this.getUrl() + '\'' + ", alias='" + this.getAlias() + '\'' + "variant='" + this.variant + '\'' + '}';
   }
}
