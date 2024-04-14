package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Line3D;
import de.violence.ui.Location3D;
import java.util.Arrays;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Step extends Module {
   private VSetting mode = new VSetting("Mode", this, Arrays.asList(new String[]{"NCP", "AAC 3.3.5", "AAC 3.3.0 (2 Blocks)", "AAC 3.3.5 (2 Blocks)"}), "NCP");
   private VSetting sStepHeigth = new VSetting("Step height", this, 0.5D, 3.0D, 0.5D, false);
   public static boolean Step = false;
   double groundy = 0.0D;

   public Step() {
      super("Step", Category.MOVEMENT);
   }

   public void onDisable() {
      this.mc.thePlayer.stepHeight = 0.5F;
      super.onDisable();
   }

   private boolean isMode(String mode) {
      return this.mode.getActiveMode().toLowerCase().replace(".", "").replace(" ", "").equalsIgnoreCase(mode.toLowerCase().replace(".", ""));
   }

   public void onUpdate() {
      this.nameAddon = this.mode.getActiveMode();
      if(this.isMode("aac335 (2 blocks)")) {
         this.onAAC331();
      } else if(this.isMode("aac330 (2 blocks)")) {
         this.onAAC15();
      } else if(this.isMode("aac335")) {
         this.onAAC330();
      } else if(this.isMode("ncp")) {
         this.onNCP();
      } else {
         this.mc.thePlayer.stepHeight = (float)this.sStepHeigth.getCurrent();
      }

      super.onUpdate();
   }

   public void onAAC330() {
      if(this.mc.gameSettings.keyBindForward.isKeyDown()) {
         if(!this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava()) {
            Line3D line = new Line3D(new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ), (double)(-this.mc.thePlayer.rotationYaw), 0.0D, 1.0D);
            BlockPos willBeTop = new BlockPos(line.getEnd().getX(), line.getEnd().getY() + 1.0D, line.getEnd().getZ());
            if(this.mc.theWorld.getBlockState(willBeTop).getBlock().getBlockBoundsMaxY() != this.mc.theWorld.getBlockState(willBeTop).getBlock().getBlockBoundsMinY() + 1.0D || this.mc.theWorld.getBlockState(willBeTop).getBlock().isTranslucent() || this.mc.theWorld.getBlockState(willBeTop).getBlock() == Blocks.water || this.mc.theWorld.getBlockState(willBeTop).getBlock() instanceof BlockSlab) {
               if(this.mc.thePlayer.isCollidedHorizontally && Math.abs(this.mc.thePlayer.motionX) + Math.abs(this.mc.thePlayer.motionZ) < 0.1D && this.mc.thePlayer.motionY <= 0.0D) {
                  this.mc.thePlayer.jump();
               } else {
                  BlockPos willBe = new BlockPos(line.getEnd().getX(), line.getEnd().getY(), line.getEnd().getZ());
                  if(this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY() == this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0D && !this.mc.theWorld.getBlockState(willBe).getBlock().isTranslucent() && this.mc.theWorld.getBlockState(willBe).getBlock() != Blocks.water && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab) && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockStairs) && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockFence) && this.mc.thePlayer.posY < (double)((int)this.mc.thePlayer.posY) + 0.5D && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSign) && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockFence) && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockStainedGlassPane)) {
                     if(this.mc.thePlayer.motionY <= 0.0D) {
                        this.groundy = this.mc.thePlayer.posY;
                        this.mc.thePlayer.motionY = 0.41999998688697815D;
                     }

                     this.move(this.mc.thePlayer.rotationYaw, 0.25F);
                     Step = true;
                  }

                  if(this.mc.thePlayer.posY > this.groundy + 0.8D && Step) {
                     this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)((int)this.groundy + 1), this.mc.thePlayer.posZ);
                     this.mc.thePlayer.motionY = -1.0D;
                     this.move(this.mc.thePlayer.rotationYaw, 0.2F);
                     Step = false;
                  }

               }
            }
         }
      }
   }

   public void onAAC15() {
      if(this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround) {
         this.mc.thePlayer.motionY = 0.5999999046325684D;
         Step = true;
      }

      if(this.mc.thePlayer.motionY <= 0.0D && Step) {
         Step = false;
         this.move(this.mc.thePlayer.rotationYaw, 0.27F);
      }

   }

   public void onAAC331() {
      if(this.mc.thePlayer.isCollidedHorizontally) {
         if(this.mc.thePlayer.onGround) {
            this.portMove(0.0F, 0.0F, 0.513F);
            Step = true;
         } else {
            if(Step) {
               this.mc.thePlayer.motionY = 0.36250001192092896D;
               Step = false;
            } else if(this.mc.thePlayer.motionY > 0.0D) {
               this.mc.thePlayer.motionY += 0.012000000104308128D;
            }

            if(this.mc.thePlayer.motionY < 0.07D) {
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)((int)this.mc.thePlayer.posY + 1), this.mc.thePlayer.posZ);
            }
         }
      }

   }

   public void onNCP() {
      if(this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround) {
         this.mc.thePlayer.motionY = 0.42489999532699585D;
         this.groundy = this.mc.thePlayer.posY;
      } else if(this.mc.thePlayer.posY > this.groundy + 1.0D && this.mc.thePlayer.motionY > 0.0D) {
         this.mc.thePlayer.posY = this.groundy + 1.0D;
         this.mc.thePlayer.motionY = -0.0D;
         if(this.mc.gameSettings.keyBindForward.isKeyDown()) {
            this.move(this.mc.thePlayer.rotationYaw, 0.5F);
         }
      }

   }
}
