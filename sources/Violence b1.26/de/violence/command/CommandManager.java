package de.violence.command;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.command.cmds.Bind;
import de.violence.command.cmds.BlockESP;
import de.violence.command.cmds.Config;
import de.violence.command.cmds.Friend;
import de.violence.command.cmds.Fucker;
import de.violence.command.cmds.IGN;
import de.violence.command.cmds.Info;
import de.violence.command.cmds.Irc;
import de.violence.command.cmds.Say;
import de.violence.command.cmds.Toggle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandManager {
   static List commandList = new ArrayList();

   public static void onExecute(String text) {
      String[] args = text.split(" ");
      if(args.length >= 1) {
         Iterator var3 = getCommandList().iterator();

         while(var3.hasNext()) {
            Command commands = (Command)var3.next();
            String cmd = "." + commands.getName();
            if(args[0].equalsIgnoreCase(cmd)) {
               String[] cmdArgs = commands.getUsage().split(" ");
               if(args.length < cmdArgs.length) {
                  Violence.getViolence().sendChat(commands.getUsage());
                  return;
               }

               String a = "";
               String[] var10;
               int var9 = (var10 = text.split(" ")).length;

               for(int var8 = 0; var8 < var9; ++var8) {
                  String s = var10[var8];
                  if(!s.equalsIgnoreCase(args[0])) {
                     a = a + s + " ";
                  }
               }

               commands.onCommand(a.split(" "));
               return;
            }
         }

         Violence.getViolence().sendChat("Command " + args[0] + " not found!");
      }
   }

   public CommandManager() {
      commandList.add(new Friend());
      commandList.add(new IGN());
      commandList.add(new Bind());
      commandList.add(new Say());
      commandList.add(new Irc());
      commandList.add(new Fucker());
      commandList.add(new Config());
      commandList.add(new BlockESP());
      commandList.add(new Toggle());
      commandList.add(new Info());
   }

   public static List getCommandList() {
      return commandList;
   }
}
