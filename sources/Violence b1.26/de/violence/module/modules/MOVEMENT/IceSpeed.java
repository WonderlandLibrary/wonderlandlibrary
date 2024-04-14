package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;

public class IceSpeed extends Module {
   private VSetting iceSpeedMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Vanilla", "AAC 3.3.7"}), "Vanilla");
   private boolean moved;
   private double speed;

   public IceSpeed() {
      super("IceSpeed", Category.MOVEMENT);
   }

   public void onUpdate() {
      if(!(this.mc.theWorld.getBlockState(this.mc.thePlayer.getPosition().add(0.0D, -0.5D, 0.0D)).getBlock() instanceof BlockIce) && !(this.mc.theWorld.getBlockState(this.mc.thePlayer.getPosition().add(0.0D, -0.5D, 0.0D)).getBlock() instanceof BlockPackedIce)) {
         if(this.moved) {
            this.mc.thePlayer.capabilities.isFlying = false;
            this.moved = false;
         }
      } else {
         String mode = this.iceSpeedMode.getActiveMode();
         if(this.mc.gameSettings.keyBindJump.pressed) {
            this.mc.thePlayer.capabilities.isFlying = false;
            this.moved = false;
            return;
         }

         this.moved = true;
         if(mode.equalsIgnoreCase("Vanilla")) {
            this.vanillaSpeed();
         } else if(mode.equalsIgnoreCase("AAC 3.3.7")) {
            this.aac337Speed();
         }
      }

      super.onUpdate();
   }

   private void vanillaSpeed() {
      this.mc.thePlayer.motionX *= 1.1D;
      this.mc.thePlayer.motionZ *= 1.1D;
   }

   private void aac337Speed() {
      this.mc.thePlayer.capabilities.isFlying = true;
   }
}
