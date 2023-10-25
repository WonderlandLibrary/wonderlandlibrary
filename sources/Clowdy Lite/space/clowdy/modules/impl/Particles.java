package space.clowdy.modules.impl;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class Particles extends Module {
     public void ず() {
          super.onDisable();
     }

     @SubscribeEvent
     public void つ(AttackEntityEvent attackEntityEvent) {
          for(int integer3 = 1; integer3 <= 12; ++integer3) {
               this.mc.player.onEnchantmentCritical(attackEntityEvent.getTarget());
          }

     }

     public void ぜ() {
          super.onEnable();
     }

     public Particles() {
          super("Particles", "\u0011>;LH5 ?0@B8:;>2 ?@8 C40@5", 0, Category.GHOST);
     }
}
