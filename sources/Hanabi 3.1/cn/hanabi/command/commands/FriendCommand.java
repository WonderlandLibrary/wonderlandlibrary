package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.command.CommandException;
import cn.hanabi.utils.ChatUtils;
import cn.hanabi.utils.FriendManager;
import cn.hanabi.utils.TargetManager;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend");
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length == 0) {
         throw new CommandException("Usage: ." + alias + " <add/a/remove/r/clear/c> <name>");
      } else {
         String option = args[0];
         if (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("add")) {
            if (option.equalsIgnoreCase("r") || option.equalsIgnoreCase("remove")) {
               if (FriendManager.getFriends().contains(args[1])) {
                  FriendManager.getFriends().remove(args[1]);
                  ChatUtils.success("Removed friend" + args[1]);
               } else {
                  ChatUtils.success("This friend is already on your list");
               }
            }
         } else {
            boolean isFriendlist = TargetManager.getTarget().contains(args[1]);
            if (!FriendManager.getFriends().contains(args[1]) && !isFriendlist) {
               ChatUtils.success("Added friend " + args[1]);
               FriendManager.getFriends().add(args[1]);
            } else {
               ChatUtils.success("This friend is already on your " + (isFriendlist ? "Targetlist" : "list"));
            }
         }

         if (option.equalsIgnoreCase("clear") || option.equalsIgnoreCase("c")) {
            ChatUtils.success("Clear list");
            FriendManager.getFriends().clear();
         }

      }
   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }
}
