package space.clowdy.modules.impl;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class GlowESP extends Module {
     @SubscribeEvent
     public void renderPlayerEvent(RenderPlayerEvent renderPlayerEvent) {
          if (renderPlayerEvent.getEntity() != this.mc.player) {
               if (this.enabled) {
                    renderPlayerEvent.getEntity().setGlowing(true);
               } else {
                    renderPlayerEvent.getEntity().setGlowing(false);
               }
          }

     }

     public void onDisable() {
		 super.onDisable();
     }

     public GlowESP() {
          super("GlowESP", "\u001284=> 83@>:>2 G5@57 AB5=K", 0, Category.VISUAL);
     }

     public void onEnable() {
          super.onEnable();
     }
}
