package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.keybinds.KeyBind;
import org.lwjgl.input.Keyboard;

public class CommandBind implements ICommandHandler {
   public String getName() {
      return "Bind";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length >= 3 || args.length == 1 && args[0].equalsIgnoreCase("list")) {
         if(args[0].equalsIgnoreCase("add")) {
            int key = Keyboard.getKeyIndex(args[1].toUpperCase());
            if(key == 0) {
               key = this.parseMouseKey(args[1].toUpperCase());
            }

            if(key == 0) {
               AC.addChat(this.getName(), "Unknown key \"" + args[1].toUpperCase() + "\"!");
               return true;
            }

            StringBuilder cmdAppend = new StringBuilder();

            for(int i = 2; i < args.length; ++i) {
               cmdAppend.append(args[i]).append(' ');
            }

            cmdAppend.deleteCharAt(cmdAppend.length() - 1);
            String command = cmdAppend.toString();
            AC.getKeyBindManager().addKeyBind(key, command);
            AC.addChat(this.getName(), "Created KeyBind for \"" + command + "\" using " + args[1].toUpperCase());
         } else if(args[0].equalsIgnoreCase("remove")) {
            int key = Keyboard.getKeyIndex(args[1].toUpperCase());
            if(key == 0) {
               key = this.parseMouseKey(args[1].toUpperCase());
            }

            if(key == 0) {
               AC.addChat(this.getName(), "Unknown key \"" + args[1].toUpperCase() + "\"!");
               return true;
            }

            StringBuilder cmdAppend = new StringBuilder();

            for(int i = 2; i < args.length; ++i) {
               cmdAppend.append(args[i]).append(' ');
            }

            cmdAppend.deleteCharAt(cmdAppend.length() - 1);
            String command = cmdAppend.toString();
            AC.getKeyBindManager().removeKeyBind(key, command);
            AC.addChat(this.getName(), "Removed KeyBind for \"" + command + "\" using " + args[1].toUpperCase());
         } else {
            if(!args[0].equalsIgnoreCase("list")) {
               AC.addChat(this.getName(), "Usage: bind <list/<add/remove> <key> <command>");
               return true;
            }

            AC.addChat(this.getName(), "Active KeyBinds:");

            for(KeyBind keyBind : AC.getKeyBindManager().all()) {
               AC.addChat(this.getName(), Keyboard.getKeyName(keyBind.keyCode) + ": " + keyBind.command);
            }
         }

         return true;
      } else {
         AC.addChat(this.getName(), "Usage: bind <list/<add/remove> <key> <command>");
         return true;
      }
   }

   public String[] getAliases() {
      return new String[]{"bind", "keybind"};
   }

   public String getDesc() {
      return "Let\'s you create keybind\'s!";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         if(args[0].toLowerCase().startsWith("r")) {
            ret.add("remove");
         } else if(args[0].toLowerCase().startsWith("a")) {
            ret.add("add");
         } else if(args[0].toLowerCase().startsWith("l")) {
            ret.add("list");
         } else {
            ret.add("remove");
            ret.add("add");
            ret.add("list");
         }
      }

      return ret;
   }

   private int parseMouseKey(String key) {
      if(!key.startsWith("MOUSE")) {
         return 0;
      } else if(!key.equalsIgnoreCase("MOUSE")) {
         int e = key.indexOf(69);
         String nr = key.substring(e + 1);

         try {
            int mb = Integer.parseInt(nr);
            return mb <= 100 && mb >= 0?mb:0;
         } catch (Exception var5) {
            return 0;
         }
      } else {
         return 0;
      }
   }
}
