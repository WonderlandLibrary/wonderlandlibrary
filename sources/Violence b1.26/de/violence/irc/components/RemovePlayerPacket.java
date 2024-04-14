package de.violence.irc.components;

import de.violence.irc.IRCPlayer;
import de.violence.irc.IrcPacket;

public class RemovePlayerPacket extends IrcPacket {
   public String getName() {
      return "removePlayer";
   }

   public void onReceivePacket(String s) {
      String ign = s.split("\\$")[0];
      IRCPlayer.ignToPlayer.remove(ign);
   }
}
