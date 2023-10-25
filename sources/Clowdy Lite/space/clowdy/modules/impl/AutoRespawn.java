package space.clowdy.modules.impl;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class AutoRespawn extends Module {
     @SubscribeEvent
     public void onRenderWorldLastEvent(RenderWorldLastEvent renderWorldLastEvent) {
          if (this.mc.player.getShouldBeDead()) {
               this.mc.player.respawnPlayer();
          }

     }

     public AutoRespawn() {
          super("AutoRespawn", "\u00102B><0B8G5A:>5 2>7@>645=85", 0, Category.MISC);
     }
}
