package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class LongJump extends Module {
   private VSetting sleepTicks = new VSetting("Sleep Ticks", this, 50.0D, 2000.0D, 50.0D, true);
   private VSetting sleepNext = new VSetting("Sleep", this, false, Arrays.asList(new String[]{"Sleep Ticks-LongJump"}));
   private VSetting pressSpace = new VSetting("On Keypress", this, false);
   private long startJump = 0L;
   private boolean setted;

   public LongJump() {
      super("LongJump", Category.MOVEMENT);
   }

   public void onDisable() {
      this.startJump = 0L;
      super.onDisable();
   }

   public void onUpdate() {
      super.onUpdate();
      if(this.mc.thePlayer.onGround && !this.setted) {
         this.startJump = System.currentTimeMillis();
         this.setted = true;
      }

      if((double)(System.currentTimeMillis() - this.startJump) < this.sleepTicks.getCurrent() && this.sleepNext.isToggled() && this.mc.thePlayer.onGround) {
         this.mc.thePlayer.motionY = 0.0D;
         this.mc.thePlayer.motionX = 0.0D;
         this.mc.thePlayer.motionZ = 0.0D;
      } else {
         if(this.mc.gameSettings.keyBindJump.pressed || !this.pressSpace.isToggled()) {
            if(this.mc.thePlayer.moveStrafing == 0.0F && this.mc.thePlayer.moveForward == 0.0F) {
               return;
            }

            if(this.mc.thePlayer.onGround) {
               this.mc.thePlayer.motionX = 0.0D;
               this.mc.thePlayer.motionZ = 0.0D;
               this.mc.thePlayer.jump();
               this.startJump = System.currentTimeMillis();
               this.setted = false;
            } else {
               long sinceStart = System.currentTimeMillis() - this.startJump;
               if(sinceStart > 150L) {
                  if(sinceStart < 200L) {
                     this.portMove(this.mc.thePlayer.rotationYaw, 0.2F, 0.0F);
                  }

                  if(sinceStart > 250L && sinceStart < 500L) {
                     this.move(this.mc.thePlayer.rotationYaw, 0.3F);
                  }

                  if(sinceStart > 500L && sinceStart < 1000L) {
                     this.move(this.mc.thePlayer.rotationYaw, 0.5F);
                  }
               }
            }
         }

      }
   }
}
