package team.massacre.impl.module.render;

import net.minecraft.util.EnumParticleTypes;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventUpdate;

public class Fard extends Module {
   public Fard() {
      super("Fard", 0, Category.RENDER);
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      for(int i = 1; i < 200; ++i) {
         this.mc.theWorld.spawnParticle(EnumParticleTypes.SLIME, this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.5D, this.mc.thePlayer.posZ, 1.0D, 0.0D, 0.0D, new int[0]);
      }

   }

   public void onEnable() {
      super.onEnable();
   }
}
