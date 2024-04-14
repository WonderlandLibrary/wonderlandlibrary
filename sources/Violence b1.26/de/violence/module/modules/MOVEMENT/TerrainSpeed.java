package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.util.BlockPos;

public class TerrainSpeed extends Module {
   public VSetting speed = new VSetting("Max Speed", this, 0.5D, 8.0D, 1.0D, false);
   private VSetting pressSpace = new VSetting("On Keypress", this, false);
   double boost = 0.0D;
   private boolean hasJumped = false;

   public TerrainSpeed() {
      super("TerrainSpeed", Category.MOVEMENT);
   }

   public void onDisable() {
      this.boost = 0.10000000149011612D;
      super.onDisable();
   }

   public void onUpdate() {
      this.nameAddon = "max:" + this.speed.getCurrent();
      if(!this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.motionY <= 0.0D) {
         if(!this.mc.thePlayer.isUsingItem() || this.mc.thePlayer.getCurrentEquippedItem() == null || !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
            if(this.boost == 0.10000000149011612D) {
               this.hasJumped = false;
            }

            if(!this.pressSpace.isToggled()) {
               this.hasJumped = true;
            }

            if(this.mc.thePlayer.onGround && this.mc.gameSettings.keyBindForward.isKeyDown()) {
               boolean over = false;
               BlockPos up = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 2.0D, this.mc.thePlayer.posZ);
               if(this.mc.theWorld.getBlockState(up).getBlock() != Blocks.air) {
                  over = true;
               }

               if(over && this.mc.gameSettings.keyBindJump.pressed) {
                  this.mc.gameSettings.keyBindJump.pressed = false;
                  this.hasJumped = true;
               }

               if(!this.hasJumped) {
                  return;
               }

               if(!over) {
                  if(this.boost > 0.10000000149011612D) {
                     this.boost /= 1.0199999809265137D;
                  }
               } else if(this.boost < this.speed.getCurrent()) {
                  BlockPos next = new BlockPos(this.mc.thePlayer.posX + this.mc.thePlayer.motionX, this.mc.thePlayer.posY - 0.1D, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ);
                  if(this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockStairs && this.boost < 1.0D) {
                     this.boost += 0.20000000298023224D;
                  } else {
                     this.boost += 0.10000000149011612D;
                  }
               }

               if(this.boost > 0.2D) {
                  this.move(this.mc.thePlayer.rotationYaw, (float)this.boost / 2.0F);
                  this.mc.gameSettings.keyBindRight.pressed = false;
                  this.mc.gameSettings.keyBindLeft.pressed = false;
               }
            } else {
               this.boost = 0.10000000149011612D;
            }

            super.onUpdate();
         }
      } else {
         this.boost = 0.10000000149011612D;
      }
   }
}
