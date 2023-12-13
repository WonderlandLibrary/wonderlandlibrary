package cn.hanabi.modules.modules.movement;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;

public class WaterSpeed extends Mod {
   public static Value speed = new Value("WaterSpeed", "Speed ", 1.2, 1.0, 1.5, 0.1);

   public WaterSpeed() {
      super("WaterSpeed", Category.MOVEMENT);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.thePlayer.isInWater() && BlockUtils.getBlock(mc.thePlayer.getPosition()) instanceof BlockLiquid) {
         EntityPlayerSP var10000 = mc.thePlayer;
         var10000.motionX *= (Double)speed.getValue();
         var10000 = mc.thePlayer;
         var10000.motionZ *= (Double)speed.getValue();
      }

   }
}
