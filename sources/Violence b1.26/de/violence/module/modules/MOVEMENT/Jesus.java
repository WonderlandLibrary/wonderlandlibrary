package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Jesus extends Module {
   private VSetting mode = new VSetting("Mode", this, Arrays.asList(new String[]{"Vanilla", "NCP", "AAC 3.3.7"}), "Vanilla");
   private long lastSwim = 0L;

   public Jesus() {
      super("Jesus", Category.MOVEMENT);
   }

   public void onUpdate() {
      BlockPos under;
      if(this.mode.getActiveMode().equalsIgnoreCase("NCP")) {
         under = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.prevPosY, this.mc.thePlayer.posZ);
         BlockPos water = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.prevPosY + 0.6D, this.mc.thePlayer.posZ);
         boolean water1 = this.mc.theWorld.getBlockState(under).getBlock() == Blocks.water && this.mc.theWorld.getBlockState(water).getBlock() != Blocks.water;
         if(this.mc.thePlayer.motionY < 0.0D && water1) {
            this.mc.thePlayer.motionY = 0.4000000059604645D;
            this.mc.thePlayer.motionX *= 1.0299999713897705D;
            this.mc.thePlayer.motionZ *= 1.0299999713897705D;
         }
      } else if(this.mode.getActiveMode().equalsIgnoreCase("Vanilla")) {
         if(this.mc.thePlayer.isInWater()) {
            this.mc.thePlayer.motionY = 0.0D;
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)((int)this.mc.thePlayer.posY + 1), this.mc.thePlayer.posZ);
            return;
         }

         under = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.prevPosY - 0.4000000059604645D, this.mc.thePlayer.posZ);
         boolean water2 = this.mc.theWorld.getBlockState(under).getBlock() == Blocks.water;
         if(water2) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)((int)this.mc.thePlayer.posY), this.mc.thePlayer.posZ);
            this.mc.thePlayer.onGround = true;
            this.mc.thePlayer.motionY = 0.0D;
         }
      } else if(this.mode.getActiveMode().equalsIgnoreCase("AAC 3.3.7")) {
         if(this.mc.gameSettings.keyBindJump.pressed) {
            return;
         }

         if(this.mc.thePlayer.worldObj.handleMaterialAcceleration(this.mc.thePlayer.getEntityBoundingBox().expand(0.0D, -1.0D, 0.0D).contract(0.001D, 0.0D, 0.001D), Material.water, this.mc.thePlayer)) {
            this.mc.gameSettings.keyBindJump.pressed = true;
            this.lastSwim = 1L;
         } else if(this.lastSwim == 1L) {
            this.mc.gameSettings.keyBindJump.pressed = false;
            this.lastSwim = 0L;
         }
      }

      super.onUpdate();
   }
}
