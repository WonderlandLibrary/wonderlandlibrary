package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModulePingSpoof extends Module implements EventListener {
   private Property delay = new Property(this, "Delay", Integer.valueOf(1337));
   public List keepAlives = new ArrayList();

   public ModulePingSpoof() {
      super("PingSpoof", "Ping = RealPing + Delay", new String[]{"pingspoof"}, Module.Category.Exploits, 13094557);
   }

   public void onEvent(Event event) {
      if(event instanceof EventPacketSend) {
         if(((EventPacketSend)event).getPacket() instanceof C00PacketKeepAlive) {
            if(this.keepAlives.contains(((EventPacketSend)event).getPacket())) {
               return;
            }

            ((EventPacketSend)event).cancel();
            Packet packet = ((EventPacketSend)event).getPacket();
            this.keepAlives.add((C00PacketKeepAlive)((EventPacketSend)event).getPacket());
            (new Thread(() -> {
               try {
                  Thread.sleep((long)((Integer)this.delay.value).intValue());
               } catch (InterruptedException var4) {
                  var4.printStackTrace();
               }

               try {
                  AC.getMC().getPlayer().sendQueue.addToSendQueue(packet);
               } catch (Exception var3) {
                  ;
               }

               this.keepAlives.remove(packet);
            }, "KeepAlive-Delay")).start();
         }
      } else if(event instanceof EventTick) {
         if(((Integer)this.delay.value).intValue() > 1500) {
            this.delay.value = Integer.valueOf(1500);
            AC.addChat(this.getName(), "Anything more that 1500ms may mess up some stuff.");
         } else if(((Integer)this.delay.value).intValue() <= 0) {
            this.delay.value = Integer.valueOf(1);
            AC.addChat(this.getName(), "No this will not work!");
         }

         this.setTag(String.valueOf(this.delay.value));
      }

   }
}
