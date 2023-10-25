package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.UUID;

public class UserListBansEntry extends BanEntry<GameProfile> {
  public UserListBansEntry(GameProfile profile) {
    this(profile, (Date)null, (String)null, (Date)null, (String)null);
  }
  
  public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason) {
    super(profile, endDate, banner, endDate, banReason);
  }
  
  public UserListBansEntry(JsonObject p_i1136_1_) {
    super(func_152648_b(p_i1136_1_), p_i1136_1_);
  }
  
  protected void onSerialization(JsonObject data) {
    if (getValue() != null) {
      data.addProperty("uuid", (getValue().getId() == null) ? "" : getValue().getId().toString());
      data.addProperty("name", getValue().getName());
      super.onSerialization(data);
    } 
  }
  
  private static GameProfile func_152648_b(JsonObject p_152648_0_) {
    if (p_152648_0_.has("uuid") && p_152648_0_.has("name")) {
      UUID uuid;
      String s = p_152648_0_.get("uuid").getAsString();
      try {
        uuid = UUID.fromString(s);
      } catch (Throwable var4) {
        return null;
      } 
      return new GameProfile(uuid, p_152648_0_.get("name").getAsString());
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\management\UserListBansEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */