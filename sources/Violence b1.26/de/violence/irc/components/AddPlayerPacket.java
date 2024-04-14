package de.violence.irc.components;

import de.violence.irc.IRCPlayer;
import de.violence.irc.IrcPacket;

public class AddPlayerPacket extends IrcPacket {
   public String getName() {
      return "addPlayer";
   }

   public void onReceivePacket(String s) {
      try {
         String[] values = s.substring(11).split("\\$");

         for(int i = 0; i < values.length; ++i) {
            String name = values[i].split(":")[0];
            String ign = values[i].split(":")[1];
            boolean admin = values[i].split(":")[2].equalsIgnoreCase("2");
            boolean mod = values[i].split(":")[2].equalsIgnoreCase("1");
            boolean vip = values[i].split(":")[2].equalsIgnoreCase("3");
            int clientID = Integer.valueOf(values[i].split(":")[3]).intValue();
            new IRCPlayer(name, ign, admin, mod, vip, clientID);
         }

      } catch (Exception var10) {
         ;
      }
   }
}
