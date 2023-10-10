package org.alphacentauri.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.StringUtils;

public class CommandFriend implements ICommandHandler {
   public String getName() {
      return "Friends";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: friend <add <name> [alias|/remove <name>/setalias <name> <new_alias>/list/clear/togglemidclick>");
      } else if(args[0].equalsIgnoreCase("list")) {
         AC.addChat(this.getName(), "Friends: ");

         for(Entry<String, String> entry : AC.getFriendManager().getAll()) {
            AC.addChat(this.getName(), StringUtils.nonAlias((String)entry.getKey()) + " aka " + (String)entry.getValue());
         }
      } else if(args[0].equalsIgnoreCase("clear")) {
         int i = 0;
         Set<Entry<String, String>> all = AC.getFriendManager().getAll();
         Set<Entry<String, String>> copy = new HashSet();
         copy.addAll(all);

         for(Entry<String, String> entry : copy) {
            AC.getFriendManager().removeFriend((String)entry.getKey());
            ++i;
         }

         AC.addChat(this.getName(), "Removed " + i + " Friends!");
      } else if(args[0].equalsIgnoreCase("add")) {
         String name;
         String alias;
         if(args.length == 2) {
            alias = name = args[1];
         } else {
            if(args.length != 3) {
               AC.addChat(this.getName(), "Invalid Syntax! Correct: friend add <name> [alias]");
               return true;
            }

            name = args[1];
            alias = args[2];
         }

         AC.getFriendManager().addFriend(name, alias);
         AC.addChat(this.getName(), "Added " + name + " as " + alias + " to your friends!");
      } else if(args[0].equalsIgnoreCase("remove")) {
         if(args.length == 2) {
            AC.getFriendManager().removeFriend(args[1]);
            AC.addChat(this.getName(), "Removed " + args[1] + " from your friends!");
         } else {
            AC.addChat(this.getName(), "Invalid Syntax! Correct: friend remove <name>");
         }
      } else if(args[0].equalsIgnoreCase("setalias")) {
         if(args.length == 3) {
            AC.getFriendManager().removeFriend(args[1]);
            AC.getFriendManager().addFriend(args[1], args[2]);
            AC.addChat(this.getName(), args[1] + " is now called " + args[2]);
         } else {
            AC.addChat(this.getName(), "Invalid Syntax! Correct: friend setalias <name> <new_alias>");
         }
      } else if(args[0].equalsIgnoreCase("togglemidclick")) {
         AC.getConfig().setMidClickEnabled(!AC.getConfig().isMidClickFriendsEnabled());
         AC.addChat(this.getName(), "MidClickFriends: " + StringUtils.getModuleState(AC.getConfig().isMidClickFriendsEnabled()));
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"friend"};
   }

   public String getDesc() {
      return "Manage your friends";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         String[] options = new String[]{"add", "remove", "setalias", "list", "clear", "togglemidclick"};

         for(String option : options) {
            if(option.startsWith(args[0].toLowerCase())) {
               ret.add(option);
            }
         }
      }

      return ret;
   }
}
