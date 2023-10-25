package space.clowdy.modules.impl;

import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class AntiAFK extends Module {
     public AntiAFK() {
          super("AntiAFK", "\u0015A;8 2K AB>8B5 =0 <5AB5, 2K ?@K305B5", 0, Category.MISC);
     }

     @SubscribeEvent
     public void し(PlayerTickEvent playerTickEvent) {
          if (this.mc.player.isOnGround() && !this.mc.player.movementInput.isMovingForward()) {
               this.mc.player.jump();
          }

     }
}
