package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListWhitelistEntry extends UserListEntry {
   public UserListWhitelistEntry(GameProfile var1) {
      super(var1);
   }

   public UserListWhitelistEntry(JsonObject var1) {
      super(gameProfileFromJsonObject(var1), var1);
   }

   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         var1.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
         var1.addProperty("name", ((GameProfile)this.getValue()).getName());
         super.onSerialization(var1);
      }

   }

   private static GameProfile gameProfileFromJsonObject(JsonObject var0) {
      if (var0.has("uuid") && var0.has("name")) {
         String var1 = var0.get("uuid").getAsString();

         UUID var2;
         try {
            var2 = UUID.fromString(var1);
         } catch (Throwable var4) {
            return null;
         }

         return new GameProfile(var2, var0.get("name").getAsString());
      } else {
         return null;
      }
   }
}
