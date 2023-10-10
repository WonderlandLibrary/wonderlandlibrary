package org.alphacentauri.modules;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventIsBot;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.RenderUtils;

public class ModuleTracers extends Module implements EventListener {
   public ModuleTracers() {
      super("Tracers", "Draws lines to other players", new String[]{"tracers", "tracer"}, Module.Category.Render, 6805136);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         AC.getMC().getWorld().loadedEntityList.stream().filter((entity) -> {
            return entity instanceof EntityPlayer && !(entity instanceof EntityPlayerSP) && !((EventIsBot)(new EventIsBot((EntityLivingBase)entity)).fire()).isBot();
         }).forEach(RenderUtils::tracerLine);
      }

   }
}
