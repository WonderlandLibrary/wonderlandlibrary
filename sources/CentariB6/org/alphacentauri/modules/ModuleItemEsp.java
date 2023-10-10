package org.alphacentauri.modules;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.entity.item.EntityItem;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.RenderUtils;

public class ModuleItemEsp extends Module implements EventListener {
   public ModuleItemEsp() {
      super("ItemESP", "Shows items", new String[]{"itemesp"}, Module.Category.Render, 10555984);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         AC.getMC().getWorld().loadedEntityList.stream().filter((item) -> {
            return item instanceof EntityItem;
         }).forEach((entity) -> {
            RenderUtils.drawItemEsp((EntityItem)entity);
         });
      }

   }
}
