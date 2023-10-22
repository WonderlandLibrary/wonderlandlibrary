package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;

public class LegitVelocity extends Mod {
   public Value chance = new Value("LegitVelocity", "Chance", 100.0, 0.0, 100.0, 1.0);
   public Value verti = new Value("LegitVelocity", "Vertical", 100.0, 0.0, 100.0, 1.0);
   public Value hori = new Value("LegitVelocity", "Horizontal", 100.0, 0.0, 100.0, 1.0);

   public LegitVelocity() {
      super("LegitVelocity", Category.GHOST);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.thePlayer.maxHurtResistantTime == mc.thePlayer.hurtResistantTime && mc.thePlayer.maxHurtResistantTime != 0) {
         double random = Math.random();
         random *= 100.0;
         EntityPlayerSP var10000;
         if (random < (double)((Double)this.chance.getValueState()).intValue()) {
            float hori = ((Double)this.hori.getValueState()).floatValue();
            hori /= 100.0F;
            float verti = ((Double)this.verti.getValueState()).floatValue();
            verti /= 100.0F;
            var10000 = mc.thePlayer;
            var10000.motionX *= (double)hori;
            var10000 = mc.thePlayer;
            var10000.motionZ *= (double)hori;
            var10000 = mc.thePlayer;
            var10000.motionY *= (double)verti;
         } else {
            var10000 = mc.thePlayer;
            var10000.motionX *= 1.0;
            var10000 = mc.thePlayer;
            var10000.motionY *= 1.0;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 1.0;
         }

      }
   }
}
