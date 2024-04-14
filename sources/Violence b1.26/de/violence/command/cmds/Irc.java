package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;

public class Irc extends Command {
   public String getDescription() {
      return "Irc-chat.";
   }

   public String getName() {
      return "irc";
   }

   public String getUsage() {
      return ".irc <Message>";
   }

   public void onCommand(String[] args) {
      StringBuilder stringBuilder = new StringBuilder();

      for(int allowed = 0; allowed < args.length; ++allowed) {
         if(allowed != args.length) {
            stringBuilder.append(args[allowed] + " ");
         } else {
            stringBuilder.append(args[allowed]);
         }
      }

      if(!args[0].equalsIgnoreCase("setnick")) {
         if(args[0].equalsIgnoreCase("list")) {
            Violence.getViolence();
            Violence.getSocketConnector().writeOutput("getList$me");
         } else if(args[0].equalsIgnoreCase("setrank")) {
            if(args.length >= 3) {
               Violence.getViolence();
               Violence.getSocketConnector().writeOutput("rank$" + args[1] + "$" + args[2]);
            }
         } else {
            Violence.getViolence();
            Violence.getSocketConnector().writeOutput("c$" + stringBuilder.toString());
         }
      } else {
         String var5 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789誰申誰申誰申-_.";
         if(args.length >= 1) {
            for(int i = 0; i < args[1].length(); ++i) {
               if(!var5.contains(String.valueOf(args[1].charAt(i)))) {
                  Violence.getViolence().sendChat("Invalid charakters.");
                  return;
               }
            }

            Violence.getViolence();
            Violence.getSocketConnector().writeOutput("changeNick$" + args[1]);
         }
      }
   }
}
