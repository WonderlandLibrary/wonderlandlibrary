package org.alphacentauri.management.managers;

import java.util.Set;
import org.alphacentauri.AC;
import org.alphacentauri.core.CloudAPI;
import org.alphacentauri.management.io.ConfigFile;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.modules.ModuleClientFriends;
import org.alphacentauri.modules.ModuleNoFriends;

public class FriendManager {
   private ConfigFile friends = new ConfigFile("friends");
   private Module noFriends;
   private Module clientFriends;

   public Set getAll() {
      return this.friends.all();
   }

   public void removeFriend(String name) {
      this.friends.set(name, (String)null);
      this.friends.save();
   }

   public String getAlias(String name) {
      String alias = this.friends.get(name);
      if(alias == null) {
         if(this.clientFriends == null) {
            this.clientFriends = AC.getModuleManager().get(ModuleClientFriends.class);
         }

         if(this.clientFriends.isEnabled() && CloudAPI.instance.users.containsKey(name)) {
            alias = (String)CloudAPI.instance.users.get(name);
         }

         if(alias == null) {
            return name;
         }
      }

      return alias;
   }

   public boolean isFriend(String name) {
      if(this.noFriends == null) {
         this.noFriends = AC.getModuleManager().get(ModuleNoFriends.class);
      }

      if(this.clientFriends == null) {
         this.clientFriends = AC.getModuleManager().get(ModuleClientFriends.class);
      }

      return (this.friends.contains(name) || this.clientFriends.isEnabled() && CloudAPI.instance.users.containsKey(name)) && !this.noFriends.isEnabled();
   }

   public void addFriend(String name, String alias) {
      if(alias == null) {
         alias = "-";
      }

      this.friends.set(name, alias);
      this.friends.save();
   }
}
