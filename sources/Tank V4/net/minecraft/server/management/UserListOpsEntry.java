package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListOpsEntry extends UserListEntry {
   private final boolean field_183025_b;
   private final int field_152645_a;

   private static GameProfile func_152643_b(JsonObject var0) {
      if (var0.has("uuid") && var0.has("name")) {
         String var1 = var0.get("uuid").getAsString();

         UUID var2;
         try {
            var2 = UUID.fromString(var1);
         } catch (Throwable var5) {
            return null;
         }

         return new GameProfile(var2, var0.get("name").getAsString());
      } else {
         return null;
      }
   }

   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         var1.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
         var1.addProperty("name", ((GameProfile)this.getValue()).getName());
         super.onSerialization(var1);
         var1.addProperty("level", (Number)this.field_152645_a);
         var1.addProperty("bypassesPlayerLimit", this.field_183025_b);
      }

   }

   public UserListOpsEntry(JsonObject var1) {
      super(func_152643_b(var1), var1);
      this.field_152645_a = var1.has("level") ? var1.get("level").getAsInt() : 0;
      this.field_183025_b = var1.has("bypassesPlayerLimit") && var1.get("bypassesPlayerLimit").getAsBoolean();
   }

   public int getPermissionLevel() {
      return this.field_152645_a;
   }

   public boolean func_183024_b() {
      return this.field_183025_b;
   }

   public UserListOpsEntry(GameProfile var1, int var2, boolean var3) {
      super(var1);
      this.field_152645_a = var2;
      this.field_183025_b = var3;
   }
}
