package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WaterSpeed extends Module {
   public VSetting mode = new VSetting("Mode", this, Arrays.asList(new String[]{"Vanilla", "AAC 3.3.5"}), "Vanilla");
   boolean goUp = false;
   boolean ready = false;

   public WaterSpeed() {
      super("WaterSpeed", Category.MOVEMENT);
   }

   public void onDisable() {
      super.onDisable();
   }

   public void onUpdate() {
      this.onAAC321();
      super.onUpdate();
   }

   public void onAAC321() {
      if(!this.mc.thePlayer.isInWater()) {
         this.ready = false;
      } else if(this.mc.gameSettings.keyBindJump.isKeyDown()) {
         this.ready = false;
      } else {
         BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.5D, this.mc.thePlayer.posZ);
         BlockPos pos2 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.55D, this.mc.thePlayer.posZ);
         if(this.goUp && this.mc.theWorld.getBlockState(pos).getBlock() != Blocks.water) {
            this.goUp = false;
         }

         if(!this.goUp && this.mc.theWorld.getBlockState(pos2).getBlock() == Blocks.water) {
            this.goUp = true;
            this.ready = true;
         }

         if(this.ready) {
            if(this.mode.getActiveMode().equalsIgnoreCase("AAC 3.3.5")) {
               if(this.goUp) {
                  this.mc.thePlayer.motionY += 0.02800000086426735D;
                  this.mc.thePlayer.motionX *= 1.1729999780654907D;
                  this.mc.thePlayer.motionZ *= 1.1729999780654907D;
               } else {
                  this.mc.thePlayer.motionY += -0.05999999865889549D;
               }
            } else if(this.goUp) {
               this.mc.thePlayer.motionY += 0.029999999329447746D;
               this.mc.thePlayer.motionX *= 1.2000000476837158D;
               this.mc.thePlayer.motionZ *= 1.2000000476837158D;
            } else {
               this.mc.thePlayer.motionY += -0.05999999865889549D;
               this.mc.thePlayer.motionX *= 1.2000000476837158D;
               this.mc.thePlayer.motionZ *= 1.2000000476837158D;
            }
         } else {
            this.mc.thePlayer.motionY += 0.029999999329447746D;
         }

      }
   }
}
