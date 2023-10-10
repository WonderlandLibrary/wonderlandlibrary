package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList {
   protected String getObjectKey(GameProfile var1) {
      return var1.getId().toString();
   }

   protected String getObjectKey(Object var1) {
      return this.getObjectKey((GameProfile)var1);
   }

   public String[] getKeys() {
      String[] var1 = new String[this.getValues().size()];
      int var2 = 0;

      UserListOpsEntry var3;
      for(Iterator var4 = this.getValues().values().iterator(); var4.hasNext(); var1[var2++] = ((GameProfile)var3.getValue()).getName()) {
         var3 = (UserListOpsEntry)var4.next();
      }

      return var1;
   }

   public UserListOps(File var1) {
      super(var1);
   }

   public GameProfile getGameProfileFromName(String var1) {
      Iterator var3 = this.getValues().values().iterator();

      while(var3.hasNext()) {
         UserListOpsEntry var2 = (UserListOpsEntry)var3.next();
         if (var1.equalsIgnoreCase(((GameProfile)var2.getValue()).getName())) {
            return (GameProfile)var2.getValue();
         }
      }

      return null;
   }

   public boolean func_183026_b(GameProfile var1) {
      UserListOpsEntry var2 = (UserListOpsEntry)this.getEntry(var1);
      return var2 != null ? var2.func_183024_b() : false;
   }

   protected UserListEntry createEntry(JsonObject var1) {
      return new UserListOpsEntry(var1);
   }
}
