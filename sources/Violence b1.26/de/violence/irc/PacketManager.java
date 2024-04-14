package de.violence.irc;

import de.violence.irc.IrcPacket;
import de.violence.irc.components.AddPlayerPacket;
import de.violence.irc.components.AdminInfoPacket;
import de.violence.irc.components.ChatPacket;
import de.violence.irc.components.InfoPacket;
import de.violence.irc.components.RemovePlayerPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PacketManager {
   static List ircPacketList = new ArrayList();

   public PacketManager() {
      ircPacketList.add(new AddPlayerPacket());
      ircPacketList.add(new ChatPacket());
      ircPacketList.add(new RemovePlayerPacket());
      ircPacketList.add(new AdminInfoPacket());
      ircPacketList.add(new InfoPacket());
   }

   public static List getIrcPacketList() {
      return ircPacketList;
   }

   public void handle(String args) {
      Iterator var3 = ircPacketList.iterator();

      while(var3.hasNext()) {
         IrcPacket ircPacket = (IrcPacket)var3.next();
         String packetName = ircPacket.getName();
         if(args.replace("$", "").startsWith(packetName)) {
            ircPacket.onReceivePacket(args.replaceFirst(packetName + "\\$", ""));
            return;
         }
      }

   }
}
