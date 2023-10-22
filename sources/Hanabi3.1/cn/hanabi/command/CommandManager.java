package cn.hanabi.command;

import cn.hanabi.command.commands.BindCommand;
import cn.hanabi.command.commands.BindsCommand;
import cn.hanabi.command.commands.ConfigCommand;
import cn.hanabi.command.commands.CrashCommand;
import cn.hanabi.command.commands.FriendCommand;
import cn.hanabi.command.commands.HideCommand;
import cn.hanabi.command.commands.PrefixCommand;
import cn.hanabi.command.commands.SkinChangeCommand;
import cn.hanabi.command.commands.SpammerCommand;
import cn.hanabi.command.commands.TargetCommand;
import cn.hanabi.command.commands.ToggleCommand;
import cn.hanabi.command.commands.WaypointCommands;
import cn.hanabi.utils.ChatUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
   private final @NotNull List commands = new ArrayList();

   public void addCommands() {
      this.addCommand(new ToggleCommand());
      this.addCommand(new BindCommand());
      this.addCommand(new PrefixCommand());
      this.addCommand(new SpammerCommand());
      this.addCommand(new CrashCommand());
      this.addCommand(new BindsCommand());
      this.addCommand(new ConfigCommand());
      this.addCommand(new CrashCommand());
      this.addCommand(new WaypointCommands());
      this.addCommand(new FriendCommand());
      this.addCommand(new TargetCommand());
      this.addCommand(new HideCommand());
      this.addCommand(new SkinChangeCommand());
   }

   public void addCommand(Command cmd) {
      this.commands.add(cmd);
   }

   public boolean executeCommand(@NotNull String string) {
      String raw = string.substring(1);
      String[] split = raw.split(" ");
      if (split.length == 0) {
         return false;
      } else {
         String cmdName = split[0];
         Command command = (Command)this.commands.stream().filter((cmd) -> {
            return cmd.match(cmdName);
         }).findFirst().orElse((Object)null);

         try {
            if (command == null) {
               return true;
            } else {
               String[] args = new String[split.length - 1];
               System.arraycopy(split, 1, args, 0, split.length - 1);
               command.run(split[0], args);
               return true;
            }
         } catch (CommandException var7) {
            ChatUtils.send("§c" + var7.getMessage());
            return true;
         }
      }
   }

   public Collection autoComplete(@NotNull String currCmd) {
      String raw = currCmd.substring(1);
      String[] split = raw.split(" ");
      List ret = new ArrayList();
      Command currentCommand = split.length >= 1 ? (Command)this.commands.stream().filter((cmd) -> {
         return cmd.match(split[0]);
      }).findFirst().orElse((Object)null) : null;
      if (split.length < 2 && (currentCommand == null || !currCmd.endsWith(" "))) {
         if (split.length != 1) {
            return ret;
         } else {
            Iterator var8 = this.commands.iterator();

            while(var8.hasNext()) {
               Command command = (Command)var8.next();
               ret.addAll(command.getNameAndAliases());
            }

            return (Collection)ret.stream().map((str) -> {
               return "." + str;
            }).filter((str) -> {
               return str.toLowerCase().startsWith(currCmd.toLowerCase());
            }).collect(Collectors.toList());
         }
      } else if (currentCommand == null) {
         return ret;
      } else {
         String[] args = new String[split.length - 1];
         System.arraycopy(split, 1, args, 0, split.length - 1);
         List autocomplete = currentCommand.autocomplete(args.length + (currCmd.endsWith(" ") ? 1 : 0), args);
         return (Collection)(autocomplete == null ? new ArrayList() : autocomplete);
      }
   }
}
