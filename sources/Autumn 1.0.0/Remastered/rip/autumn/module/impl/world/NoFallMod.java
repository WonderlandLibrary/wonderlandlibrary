package rip.autumn.module.impl.world;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.movement.FlightMod;
import rip.autumn.module.option.impl.EnumOption;

@Label("No Fall")
@Category(ModuleCategory.WORLD)
@Aliases({"nofall"})
public final class NoFallMod extends Module {
   public final EnumOption mode;

   public NoFallMod() {
      this.mode = new EnumOption("Mode", Mode.GROUND);
      this.setMode(this.mode);
      this.addOptions(this.mode);
   }

   @Listener(MotionUpdateEvent.class)
   public void onMotionUpdate(MotionUpdateEvent event) {
      if (event.isPre()) {
         switch(mode.getValue().toString()) {
         case "GROUND":
            if (mc.thePlayer.fallDistance > 3.0F && !FlightMod.getInstance().isEnabled()) {
               event.setOnGround(true);
            }
            break;
         }
      }
   }

   private boolean isBlockUnder() {
      for(int i = (int)(mc.thePlayer.posY - 1.0D); i > 0; --i) {
         BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
         if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
            return true;
         }
      }

      return false;
   }

   public enum Mode {
      GROUND;
   }
}
