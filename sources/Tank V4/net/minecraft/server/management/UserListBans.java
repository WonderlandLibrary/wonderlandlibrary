package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList {
   public boolean isBanned(GameProfile var1) {
      return this.hasEntry(var1);
   }

   public GameProfile isUsernameBanned(String var1) {
      Iterator var3 = this.getValues().values().iterator();

      while(var3.hasNext()) {
         UserListBansEntry var2 = (UserListBansEntry)var3.next();
         if (var1.equalsIgnoreCase(((GameProfile)var2.getValue()).getName())) {
            return (GameProfile)var2.getValue();
         }
      }

      return null;
   }

   protected String getObjectKey(Object var1) {
      return this.getObjectKey((GameProfile)var1);
   }

   public UserListBans(File var1) {
      super(var1);
   }

   public String[] getKeys() {
      String[] var1 = new String[this.getValues().size()];
      int var2 = 0;

      UserListBansEntry var3;
      for(Iterator var4 = this.getValues().values().iterator(); var4.hasNext(); var1[var2++] = ((GameProfile)var3.getValue()).getName()) {
         var3 = (UserListBansEntry)var4.next();
      }

      return var1;
   }

   protected UserListEntry createEntry(JsonObject var1) {
      return new UserListBansEntry(var1);
   }

   protected String getObjectKey(GameProfile var1) {
      return var1.getId().toString();
   }
}
