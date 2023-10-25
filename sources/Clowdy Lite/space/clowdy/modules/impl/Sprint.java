package space.clowdy.modules.impl;

import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class Sprint extends Module {
     public void onDisable2() {
          super.onDisable();
     }

     @SubscribeEvent
     public void onPlayerTickEvent(PlayerTickEvent playerTickEvent) {
          if (!this.mc.player.collidedHorizontally && this.mc.player.movementInput.isMovingForward()) {
               this.mc.player.setSprinting(true);
          }

     }

     public void onEnable2() {
          super.onEnable();
     }

     public Sprint() {
          super("Sprint", "\u00102B><0B8G5A:>5 CA:>@5=85", 0, Category.MISC);
     }
}
