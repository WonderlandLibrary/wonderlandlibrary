package de.violence.module.modules.WORLD;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.ui.Category;
import de.violence.ui.Timings;
import de.violence.utils.RotationHandler;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class Tower extends Module {
   public VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Normal", "NCP", "AAC", "Legit"}), "Normal");
   private VSetting pressSpace = new VSetting("On Keypress", this, false);
   private VSetting bSilent = new VSetting("Silent", this, false);
   private Timings timings = new Timings();
   private long onGround;
   double groundy = 0.0D;
   private int blocksLeft;
   public boolean timerSetted = false;

   public Tower() {
      super("Tower", Category.WORLD);
   }

   private boolean isMode(String mode) {
      return this.mMode.getActiveMode().toLowerCase().replace(".", "").equalsIgnoreCase(mode.toLowerCase().replace(".", ""));
   }

   private int findBlocks() {
      for(int i = 0; i < 9; ++i) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + i).getHasStack() && this.mc.thePlayer.inventoryContainer.getSlot(36 + i).getStack().getItem() instanceof ItemBlock) {
            return 36 + i;
         }
      }

      return -1;
   }

   public void onDisable() {
      if(this.bSilent.isToggled()) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
      }

      this.blocksLeft = 0;
      super.onDisable();
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      super.onUpdate();
      if(Killaura.shouldAction && this.bSilent.isToggled()) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
      }

      ItemStack itemStack = this.mc.thePlayer.getCurrentEquippedItem();
      if(this.bSilent.isToggled() && this.findBlocks() != -1) {
         itemStack = this.mc.thePlayer.inventoryContainer.getSlot(this.findBlocks()).getStack();
      }

      if(itemStack != null) {
         if(itemStack.getItem() instanceof ItemBlock) {
            if(this.timerSetted) {
               this.mc.timer.timerSpeed = 1.0F;
               this.timerSetted = false;
            }

            if(this.mc.gameSettings.keyBindJump.pressed || !this.pressSpace.isToggled()) {
               this.blocksLeft = 5;
            }

            if(this.blocksLeft != 0) {
               if(!Module.getByName("Scaffold").isToggled()) {
                  if(this.mc.thePlayer.onGround && !this.timings.hasReached(1000L)) {
                     this.onGround = System.currentTimeMillis();
                  }

                  if(this.timings.hasReached(1000L) && this.isMode("aac")) {
                     if(this.timings.hasReached(1200L) && this.mc.thePlayer.onGround && System.currentTimeMillis() - this.onGround > 300L) {
                        this.timings.resetTimings();
                     }

                  } else {
                     BlockPos at = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ);
                     if(this.mc.theWorld.getBlockState(at).getBlock() == Blocks.air) {
                        if(!this.mc.thePlayer.onGround) {
                           return;
                        }

                        this.mc.thePlayer.jump();
                     }

                     RotationHandler.server_pitch = 90.0F;
                     if(this.mc.thePlayer.onGround) {
                        this.groundy = this.mc.thePlayer.posY;
                        this.jump();
                     }

                     if(this.isMode("normal")) {
                        if(this.mc.thePlayer.posY > this.groundy + 1.2D) {
                           this.placeBlock(itemStack);
                        }
                     } else if(this.isMode("ncp")) {
                        if(this.mc.thePlayer.posY > this.groundy + 0.9D) {
                           this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)((int)this.mc.thePlayer.posY), this.mc.thePlayer.posZ);
                           this.placeBlock(itemStack);
                           this.jump();
                           this.groundy = this.mc.thePlayer.posY;
                           this.mc.timer.timerSpeed = 1.0F;
                        }
                     } else if(this.isMode("aac")) {
                        this.timerSetted = true;
                        this.mc.timer.timerSpeed = 1.1F;
                        if(this.mc.thePlayer.motionY < 0.0D) {
                           this.mc.thePlayer.motionY = -0.5D;
                        }

                        if(this.mc.thePlayer.posY > this.groundy + 0.800000011920929D) {
                           this.placeBlock(itemStack);
                        }
                     } else if(this.isMode("Legit")) {
                        if(this.mc.thePlayer.onGround) {
                           this.mc.thePlayer.motionY = 0.42D;
                        }

                        if(this.mc.thePlayer.posY > this.groundy + 0.6499999761581421D) {
                           this.placeBlock(itemStack);
                        }
                     }

                  }
               }
            }
         }
      }
   }

   public void placeBlock(ItemStack itemStack) {
      if(this.findBlocks() != -1) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.findBlocks() - 36));
      }

      BlockPos at = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ);
      if(this.mc.theWorld.getBlockState(at.add(0, 1, 0)).getBlock() == Blocks.air) {
         this.mc.thePlayer.swingItem();
         IBlockState ibl = ((ItemBlock)itemStack.getItem()).getBlock().getDefaultState();
         this.mc.theWorld.setBlockState(at.add(0, 1, 0), ibl);
         C08PacketPlayerBlockPlacement bl = new C08PacketPlayerBlockPlacement(at, 1, itemStack, 0.0F, 0.0F, 0.0F);
         this.mc.thePlayer.sendQueue.addToSendQueue(bl);
         if(this.blocksLeft > 0) {
            --this.blocksLeft;
         }

      }
   }

   public void jump() {
      if(this.isMode("aac")) {
         this.mc.thePlayer.motionY = 0.38999998569488525D;
      } else {
         this.mc.thePlayer.motionY = 0.41999998688697815D;
      }

   }
}
