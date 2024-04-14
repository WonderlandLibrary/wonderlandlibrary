package de.violence.packet;

import de.violence.packet.Packet;
import de.violence.packet.packets.VelocityPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PacketHandler {
   public List packetList = new ArrayList();

   public PacketHandler() {
      this.packetList.add(new VelocityPacket());
   }

   public List getPacketList() {
      return this.packetList;
   }

   public boolean handle(String packet, Object... objects) {
      Iterator var4 = this.getPacketList().iterator();

      while(var4.hasNext()) {
         Packet packets = (Packet)var4.next();
         if(packets.getName().equals(packet)) {
            return packets.dispatchPacket(objects);
         }
      }

      return false;
   }
}
