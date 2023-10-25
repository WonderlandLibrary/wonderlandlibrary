package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListWhitelistEntry extends UserListEntry<GameProfile> {
  public UserListWhitelistEntry(GameProfile profile) {
    super(profile);
  }
  
  public UserListWhitelistEntry(JsonObject p_i1130_1_) {
    super(gameProfileFromJsonObject(p_i1130_1_), p_i1130_1_);
  }
  
  protected void onSerialization(JsonObject data) {
    if (getValue() != null) {
      data.addProperty("uuid", (getValue().getId() == null) ? "" : getValue().getId().toString());
      data.addProperty("name", getValue().getName());
      super.onSerialization(data);
    } 
  }
  
  private static GameProfile gameProfileFromJsonObject(JsonObject p_152646_0_) {
    if (p_152646_0_.has("uuid") && p_152646_0_.has("name")) {
      UUID uuid;
      String s = p_152646_0_.get("uuid").getAsString();
      try {
        uuid = UUID.fromString(s);
      } catch (Throwable var4) {
        return null;
      } 
      return new GameProfile(uuid, p_152646_0_.get("name").getAsString());
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\management\UserListWhitelistEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */