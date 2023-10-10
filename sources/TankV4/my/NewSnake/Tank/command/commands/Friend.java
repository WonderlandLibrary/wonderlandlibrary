package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.utils.ClientUtils;

@Com(
   names = {"friend", "f"}
)
public class Friend extends Command {
   public void runCommand(String[] var1) {
      if (var1.length < 3) {
         ClientUtils.sendMessage(this.getHelp());
      } else {
         if (!var1[1].equalsIgnoreCase("add") && !var1[1].equalsIgnoreCase("a")) {
            if (!var1[1].equalsIgnoreCase("del") && !var1[1].equalsIgnoreCase("d")) {
               ClientUtils.sendMessage(this.getHelp());
            } else if (FriendManager.isFriend(var1[2])) {
               FriendManager.removeFriend(var1[2]);
               ClientUtils.sendMessage("Removed friend: " + var1[2]);
            } else {
               ClientUtils.sendMessage(String.valueOf(var1[2]) + " is not your friend.");
            }
         } else {
            String var2 = var1[2];
            if (var1.length > 3) {
               var2 = var1[3];
               if (var2.startsWith("\"") && var1[var1.length - 1].endsWith("\"")) {
                  var2 = var2.substring(1, var2.length());

                  for(int var3 = 4; var3 < var1.length; ++var3) {
                     var2 = String.valueOf(var2) + " " + var1[var3].replace("\"", "");
                  }
               }
            }

            if (FriendManager.isFriend(var1[2]) && var1.length < 3) {
               ClientUtils.sendMessage(String.valueOf(var1[2]) + " is already your friend.");
               return;
            }

            FriendManager.removeFriend(var1[2]);
            FriendManager.addFriend(var1[2], var2);
            ClientUtils.sendMessage("Added " + var1[2] + (var1.length > 3 ? " as " + var2 : ""));
         }

      }
   }

   public String getHelp() {
      return "Friend - friend <f>  (add <a> | del <d>) (name) [alias | \"alias w/ spaces\"].";
   }
}
