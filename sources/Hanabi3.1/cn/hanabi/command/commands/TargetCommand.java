package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.command.CommandException;
import cn.hanabi.utils.ChatUtils;
import cn.hanabi.utils.FriendManager;
import cn.hanabi.utils.TargetManager;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TargetCommand extends Command {
   public TargetCommand() {
      super("target");
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length == 0) {
         throw new CommandException("Usage: ." + alias + " <add/a/remove/r/clear/c> <name>");
      } else {
         String option = args[0];
         if (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("add")) {
            if (option.equalsIgnoreCase("r") || option.equalsIgnoreCase("remove")) {
               if (TargetManager.getTarget().contains(args[1])) {
                  TargetManager.getTarget().remove(args[1]);
                  ChatUtils.success("Removed target" + args[1]);
               } else {
                  ChatUtils.success("This target is already on your list");
               }
            }
         } else {
            boolean isFriendlist = FriendManager.getFriends().contains(args[1]);
            if (!TargetManager.getTarget().contains(args[1]) && !isFriendlist) {
               ChatUtils.success("Added target " + args[1]);
               TargetManager.getTarget().add(args[1]);
            } else {
               ChatUtils.success("This target is already on your " + (isFriendlist ? "Friendlist" : "list"));
            }
         }

         if (option.equalsIgnoreCase("clear") || option.equalsIgnoreCase("c")) {
            ChatUtils.success("Clear Target");
            TargetManager.getTarget().clear();
         }

      }
   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }
}
