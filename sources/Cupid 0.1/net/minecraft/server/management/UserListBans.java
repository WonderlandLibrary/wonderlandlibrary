package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListBans extends UserList<GameProfile, UserListBansEntry> {
  public UserListBans(File bansFile) {
    super(bansFile);
  }
  
  protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
    return new UserListBansEntry(entryData);
  }
  
  public boolean isBanned(GameProfile profile) {
    return hasEntry(profile);
  }
  
  public String[] getKeys() {
    String[] astring = new String[getValues().size()];
    int i = 0;
    for (UserListBansEntry userlistbansentry : getValues().values())
      astring[i++] = userlistbansentry.getValue().getName(); 
    return astring;
  }
  
  protected String getObjectKey(GameProfile obj) {
    return obj.getId().toString();
  }
  
  public GameProfile isUsernameBanned(String username) {
    for (UserListBansEntry userlistbansentry : getValues().values()) {
      if (username.equalsIgnoreCase(userlistbansentry.getValue().getName()))
        return userlistbansentry.getValue(); 
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\management\UserListBans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */