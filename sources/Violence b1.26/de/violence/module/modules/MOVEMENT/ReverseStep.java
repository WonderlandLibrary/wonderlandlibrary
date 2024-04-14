package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.block.material.Material;

public class ReverseStep extends Module {
   private boolean lastGround;

   public ReverseStep() {
      super("ReverseStep", Category.MOVEMENT);
   }

   public void onUpdate() {
      if(this.mc.thePlayer.fallDistance > 0.07F && this.lastGround && this.mc.theWorld.getBlockState(this.mc.thePlayer.getPosition().add(0.0D, -1.5D, 0.0D)).getBlock().getMaterial() != Material.air) {
         this.mc.thePlayer.motionY = -1000.0D;
      }

      this.lastGround = this.mc.thePlayer.onGround;
      super.onUpdate();
   }
}
