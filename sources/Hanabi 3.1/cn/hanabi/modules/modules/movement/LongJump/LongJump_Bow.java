package cn.hanabi.modules.modules.movement.LongJump;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.rotation.Rotation;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ObfuscationClass
public class LongJump_Bow {
   final Minecraft mc = Minecraft.getMinecraft();
   private int stage;
   private int bowTick;
   private int bowSlot;
   private int startSlot;
   private double speed;
   private double last;
   private boolean dmged;
   private boolean jumped;

   public void onEnable() {
      this.stage = 0;
      this.bowTick = 0;
      this.bowSlot = -1;
      this.startSlot = this.mc.thePlayer.inventory.currentItem;
      this.speed = 0.0;
      this.last = 0.0;
      this.jumped = false;
      this.dmged = false;
   }

   public void onDisable() {
      PlayerUtil.setSpeed(0.0);
      if (this.bowTick != 0) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      }

      if (this.mc.thePlayer.inventory.currentItem != this.startSlot) {
         this.mc.thePlayer.inventory.currentItem = this.startSlot;
      }

   }

   public void onPre(EventPreMotion e) {
      if (!this.jumped && !this.mc.thePlayer.onGround && this.mc.thePlayer.motionY > 0.0) {
         this.jumped = true;
      }

      if (this.getBowCount() != 0 && this.getArrowCount() != 0) {
         if (this.stage == 0) {
            this.stage = 1;
         }

         label111:
         switch (this.stage) {
            case 1:
               int i;
               ItemStack stack;
               for(i = 0; i < 9; ++i) {
                  stack = this.mc.thePlayer.inventory.mainInventory[i];
                  if (stack != null && stack.getItem() instanceof ItemBow) {
                     this.bowSlot = i;
                     break;
                  }
               }

               if (this.bowSlot == -1) {
                  for(i = 9; i < 36; ++i) {
                     if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        int count = 0;
                        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBow) {
                           for(int a = 36; a < 45; ++a) {
                              Container var10000 = this.mc.thePlayer.inventoryContainer;
                              if (Container.canAddItemToSlot(this.mc.thePlayer.inventoryContainer.getSlot(a), new ItemStack(Item.getItemById(261)), true)) {
                                 this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, a - 36, 2, this.mc.thePlayer);
                                 ++count;
                                 break;
                              }
                           }

                           if (count == 0) {
                              this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, 7, 2, this.mc.thePlayer);
                           }
                           break;
                        }
                     }
                  }
               }

               for(i = 0; i < 9; ++i) {
                  stack = this.mc.thePlayer.inventory.mainInventory[i];
                  if (stack != null && stack.getItem() instanceof ItemBow) {
                     this.bowSlot = i;
                     break;
                  }
               }

               if (this.bowSlot == -1) {
                  ClientUtil.sendClientMessage("Unknow exception , maybe your inventory is in desync", Notification.Type.ERROR);
                  ModManager.getModule("LongJump").set(false);
                  return;
               }

               this.stage = 2;
               break;
            case 2:
               if (this.mc.thePlayer.inventory.currentItem != this.bowSlot) {
                  this.mc.thePlayer.inventory.currentItem = this.bowSlot;
                  this.bowTick = 0;
                  return;
               }

               if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                  (new Rotation(this.mc.thePlayer.rotationYaw, -90.0F)).apply(e);
                  switch (++this.bowTick) {
                     case 1:
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                        break label111;
                     case 5:
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        this.stage = 3;
                  }
               }
               break;
            case 3:
               if (this.mc.thePlayer.hurtTime >= 9 && !this.dmged) {
                  this.mc.thePlayer.inventory.currentItem = this.startSlot;
                  this.dmged = true;
                  this.stage = 4;
               }
         }

         if (this.stage > 5) {
            this.last = PlayerUtil.getLastDist();
         }

      } else {
         ClientUtil.sendClientMessage("You need bow & arrows", Notification.Type.ERROR);
         ModManager.getModule("LongJump").set(false);
      }
   }

   public void onPost(EventPostMotion e) {
   }

   public void onMove(EventMove event) {
      if (!this.dmged && this.stage > 0) {
         PlayerUtil.setSpeed(event, 0.0);
      } else {
         if (this.stage == 4) {
            event.setY(this.mc.thePlayer.motionY = PlayerUtil.getBaseJumpHeight() * 1.9500000476837158);
            this.speed = PlayerUtil.getBaseMoveSpeed() * 1.3;
         } else if (this.stage >= 4 && this.stage != 5) {
            if (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).isEmpty() || this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround) {
               ModManager.getModule("LongJump").set(false);
            }

            event.setY(this.mc.thePlayer.motionY += 0.004999999888241291 - this.speed / PlayerUtil.getBaseMoveSpeed() * 0.004999999888241291);
            this.speed = this.last - this.last / 159.0;
         }

         ++this.stage;
         this.speed = Math.max(this.speed, PlayerUtil.getBaseMoveSpeed());
         MoveUtils.setMotion(event, this.speed);
      }

   }

   private int getArrowCount() {
      int arrowCount = 0;

      for(int i = 0; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is.getItem() == Item.getItemById(262)) {
               arrowCount += is.stackSize;
            }
         }
      }

      return arrowCount;
   }

   private int getBowCount() {
      int bowCount = 0;

      for(int i = 0; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is.getItem() instanceof ItemBow) {
               bowCount += is.stackSize;
            }
         }
      }

      return bowCount;
   }
}
