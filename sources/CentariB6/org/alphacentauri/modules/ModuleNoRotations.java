package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketRecv;
import org.alphacentauri.management.modules.Module;

public class ModuleNoRotations extends Module implements EventListener {
   public ModuleNoRotations() {
      super("NoRotations", "Stops the server from setbacking your rotation", new String[]{"norotations"}, Module.Category.Misc, 3641351);
   }

   public void onEvent(Event event) {
      if(event instanceof EventPacketRecv) {
         Packet packet = ((EventPacketRecv)event).getPacket();
         if(packet instanceof S08PacketPlayerPosLook) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player == null) {
               return;
            }

            ((S08PacketPlayerPosLook)packet).pitch = player.rotationPitch;
            ((S08PacketPlayerPosLook)packet).yaw = player.rotationYaw;
         }
      }

   }
}
