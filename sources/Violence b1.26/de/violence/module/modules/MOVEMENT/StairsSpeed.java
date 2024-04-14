package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;

public class StairsSpeed extends Module {
   private VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"AAC 3.3.7", "AAC 3.3.8"}), "AAC 3.3.7");
   public VSetting speed = new VSetting("Max Speed", this, 0.5D, 8.0D, 1.0D, false);
   private VSetting pressSpace = new VSetting("On Keypress", this, false);
   double boost = 0.0D;
   private boolean hasJumped = false;
   double lastY = 0.0D;

   public StairsSpeed() {
      super("StairsSpeed", Category.MOVEMENT);
   }

   public void onDisable() {
      this.boost = 0.10000000149011612D;
      this.lastY = this.mc.thePlayer.posY;
      super.onDisable();
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      if(!this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.motionY <= 0.0D) {
         if(this.boost == 0.10000000149011612D) {
            this.hasJumped = false;
         }

         if(!this.pressSpace.isToggled()) {
            this.hasJumped = true;
         }

         BlockPos pos1 = new BlockPos(this.mc.thePlayer.posX + 0.3D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + 0.3D);
         BlockPos pos2 = new BlockPos(this.mc.thePlayer.posX + 0.3D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - 0.3D);
         BlockPos pos3 = new BlockPos(this.mc.thePlayer.posX - 0.3D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + 0.3D);
         BlockPos pos4 = new BlockPos(this.mc.thePlayer.posX - 0.3D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - 0.3D);
         ArrayList pos = new ArrayList();
         pos.add(pos1);
         pos.add(pos2);
         pos.add(pos3);
         pos.add(pos4);
         boolean doIt = false;
         Iterator var8 = pos.iterator();

         BlockPos next;
         while(var8.hasNext()) {
            next = (BlockPos)var8.next();
            if(this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockStairs || this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockSnow) {
               doIt = true;
               break;
            }
         }

         if(this.lastY < this.mc.thePlayer.posY && this.mc.thePlayer.motionY <= 0.0D && doIt) {
            this.boost += 0.10000000149011612D;
         }

         if(this.boost > 0.7D) {
            this.boost = 0.699999988079071D;
         }

         if(this.boost > 0.2D) {
            this.move(this.mc.thePlayer.rotationYaw, (float)this.boost);
            this.boost /= 1.0199999809265137D;
         }

         this.lastY = this.mc.thePlayer.posY;
         if(doIt && this.mc.gameSettings.keyBindJump.pressed) {
            this.mc.gameSettings.keyBindJump.pressed = false;
            this.hasJumped = true;
         }

         if(this.hasJumped) {
            if(this.mMode.getActiveMode().equalsIgnoreCase("AAC 3.3.7")) {
               if(this.mc.thePlayer.onGround && this.mc.gameSettings.keyBindForward.isKeyDown()) {
                  if(!doIt) {
                     if(this.boost > 0.10000000149011612D) {
                        this.boost /= 1.0199999809265137D;
                     }
                  } else if(this.boost < this.speed.getCurrent()) {
                     next = new BlockPos(this.mc.thePlayer.posX + this.mc.thePlayer.motionX, this.mc.thePlayer.posY - 0.1D, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ);
                     if(this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockStairs && this.boost < 1.0D) {
                        this.boost += 0.20000000298023224D;
                     } else {
                        this.boost += 0.10000000149011612D;
                     }
                  }

                  if(this.boost > 0.2D) {
                     this.move(this.mc.thePlayer.rotationYaw, (float)this.boost);
                     this.mc.gameSettings.keyBindRight.pressed = false;
                     this.mc.gameSettings.keyBindLeft.pressed = false;
                  }
               } else {
                  this.boost = 0.10000000149011612D;
               }
            } else if(this.mMode.getActiveMode().equalsIgnoreCase("AAC 3.3.8")) {
               if(!this.mc.gameSettings.keyBindForward.isKeyDown() || !this.mc.thePlayer.onGround) {
                  this.boost = 0.2D;
                  this.lastY = this.mc.thePlayer.posY;
                  return;
               }

               if(this.lastY < this.mc.thePlayer.posY && this.mc.thePlayer.motionY <= 0.0D) {
                  this.boost += 0.10000000149011612D;
               }

               if(this.boost > 0.7D) {
                  this.boost = 0.699999988079071D;
               }

               if(this.boost > 0.2D) {
                  this.move(this.mc.thePlayer.rotationYaw, (float)this.boost);
                  this.boost /= 1.0199999809265137D;
               }

               this.lastY = this.mc.thePlayer.posY;
            }

            super.onUpdate();
         }
      } else {
         this.boost = 0.10000000149011612D;
      }
   }
}
