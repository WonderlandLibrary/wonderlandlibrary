package space.clowdy.modules.impl;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class Crasher extends Module {
     @SubscribeEvent
     public void ゃ(RenderWorldLastEvent renderWorldLastEvent) {
          for(int integer3 = 1; integer3 <= 1000000; ++integer3) {
               this.mc.player.onEnchantmentCritical(this.mc.player);
          }

     }

     public Crasher() {
          super("Crasher", "\u0012:;NG09B5 ?@8 45<>=AB@0F88 M:@0=0 =0 ?@>25@:5", 0, Category.DETECT);
     }
}
