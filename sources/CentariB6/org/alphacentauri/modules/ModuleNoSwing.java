package org.alphacentauri.modules;

import net.minecraft.network.play.client.C0APacketAnimation;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.modules.Module;

public class ModuleNoSwing extends Module implements EventListener {
   public ModuleNoSwing() {
      super("NoSwing", "Swinging? No", new String[]{"noswing"}, Module.Category.Exploits, 12032450);
   }

   public void onEvent(Event event) {
      if(event instanceof EventPacketSend && ((EventPacketSend)event).getPacket() instanceof C0APacketAnimation) {
         ((EventPacketSend)event).cancel();
      }

   }
}
