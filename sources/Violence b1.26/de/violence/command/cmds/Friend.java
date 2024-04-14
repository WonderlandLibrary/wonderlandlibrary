package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.friend.FriendManager;
import de.violence.irc.IRCPlayer;
import java.util.Iterator;

public class Friend extends Command {
   public String getDescription() {
      return "Add friends or remove them.";
   }

   public String getName() {
      return "friend";
   }

   public String getUsage() {
      return ".friend <Name> <Alias>";
   }

   public void onCommand(String[] args) {
      String username = args[0];
      String alias = args[1];
      if(!username.equalsIgnoreCase("irc")) {
         if(FriendManager.getAliasOf(username) == null) {
            FriendManager.getFriendList().put(username, alias);
            Violence.getViolence().sendChat("Friend has been added!");
         } else {
            FriendManager.getFriendList().remove(username);
            Violence.getViolence().sendChat("Friend has been removed!");
         }

      } else {
         Iterator var5 = IRCPlayer.ignToPlayer.values().iterator();

         while(var5.hasNext()) {
            IRCPlayer ircPlayer = (IRCPlayer)var5.next();
            if(ircPlayer.getName().equalsIgnoreCase(alias)) {
               if(FriendManager.getAliasOf(alias) == null) {
                  FriendManager.getFriendList().put(alias, alias);
                  Violence.getViolence().sendChat("Friend has been added!");
               } else {
                  FriendManager.getFriendList().remove(alias);
                  Violence.getViolence().sendChat("Friend has been removed!");
               }

               return;
            }
         }

         Violence.getViolence().sendChat("§3IRC-User: §7" + alias + " §cnot found!");
      }
   }
}
