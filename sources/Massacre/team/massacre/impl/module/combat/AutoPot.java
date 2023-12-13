package team.massacre.impl.module.combat;

import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.TimerUtil;

public final class AutoPot extends Module {
   private TimerUtil potTimer = new TimerUtil();
   public boolean potting;
   private float prevPitch;
   public DoubleProperty delay = new DoubleProperty("Delay", 750.0D, 0.0D, 2000.0D, 10.0D);
   public DoubleProperty healPercent = new DoubleProperty("Health", 10.0D, 0.0D, 20.0D, 1.0D);
   public Property<Boolean> jumpBoost = new Property("Frog Pots", false);

   public AutoPot() {
      super("AutoPot", 0, Category.COMBAT);
      this.addValues(new Property[]{this.delay, this.healPercent, this.jumpBoost});
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @Handler
   public void onUpdate(EventUpdate e) {
      if (!Massacre.INSTANCE.getModuleManager().getModule("Scaffold").getState()) {
         int prevSlot = this.mc.thePlayer.inventory.currentItem;
         int index;
         ItemStack stack;
         if (e.isPre() && !(this.mc.currentScreen instanceof GuiChest) && this.mc.thePlayer.onGround && !this.mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.potTimer.hasHit((float)((Double)this.delay.getValue()).longValue())) {
            if (this.isSpeedPotsInHotBar() && !this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashSpeedPot(stack)) {
                     this.potting = true;
                     e.setPitch(90.0F);
                  }
               }

               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashSpeedPot(stack) && this.potting) {
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, 90.0F, this.mc.thePlayer.onGround));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(index - 36));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
                     this.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.prevPitch, this.mc.thePlayer.onGround));
                     break;
                  }
               }

               this.potting = false;
            }

            this.potTimer.reset();
            this.getSpeedPotsFromInventory();
         }

         if (!this.mc.thePlayer.isPotionActive(Potion.regeneration) && this.mc.thePlayer.getHealth() <= ((Double)this.healPercent.getValue()).floatValue() && this.potTimer.hasHit((float)((Double)this.delay.getValue()).longValue())) {
            if (this.isRegenPotsInHotBar()) {
               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashRegenPot(stack)) {
                     this.potting = true;
                     e.setPitch(90.0F);
                  }
               }

               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashRegenPot(stack)) {
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, 90.0F, this.mc.thePlayer.onGround));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(index - 36));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
                     this.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.prevPitch, this.mc.thePlayer.onGround));
                     break;
                  }
               }

               this.potTimer.reset();
               this.potting = false;
            } else {
               this.getRegenPotsFromInventory();
            }
         }

         if (this.mc.thePlayer.getHealth() <= ((Double)this.healPercent.getValue()).floatValue() && this.potTimer.hasHit((float)((Double)this.delay.getValue()).longValue())) {
            if (this.isHealthPotsInHotBar()) {
               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashHealthPot(stack)) {
                     this.potting = true;
                     e.setPitch(90.0F);
                  }
               }

               for(index = 36; index < 45; ++index) {
                  stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && this.isStackSplashHealthPot(stack)) {
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, 90.0F, this.mc.thePlayer.onGround));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(index - 36));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
                     this.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.prevPitch, this.mc.thePlayer.onGround));
                     break;
                  }
               }

               this.potTimer.reset();
               this.potting = false;
            } else {
               this.getPotsFromInventory();
            }
         } else if (e.isPost()) {
            this.potting = false;
         }

      }
   }

   public boolean isPotting() {
      return this.potting;
   }

   private boolean isSpeedPotsInHotBar() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && !stack.getDisplayName().contains("Frog") && this.isStackSplashSpeedPot(stack)) {
            return true;
         }
      }

      return false;
   }

   private boolean isHealthPotsInHotBar() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSplashHealthPot(stack)) {
            return true;
         }
      }

      return false;
   }

   private boolean isRegenPotsInHotBar() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSplashRegenPot(stack)) {
            return true;
         }
      }

      return false;
   }

   private void getSpeedPotsFromInventory() {
      if (!(this.mc.currentScreen instanceof GuiChest)) {
         for(int index = 9; index < 36; ++index) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && !stack.getDisplayName().contains("Frog") && this.isStackSplashSpeedPot(stack)) {
               this.mc.playerController.windowClick(0, index, 6, 2, this.mc.thePlayer);
               break;
            }
         }

      }
   }

   private void getPotsFromInventory() {
      if (!(this.mc.currentScreen instanceof GuiChest)) {
         for(int index = 9; index < 36; ++index) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
               this.mc.playerController.windowClick(0, index, 6, 2, this.mc.thePlayer);
               break;
            }
         }

      }
   }

   private boolean isStackSplashSpeedPot(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         if (stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
               Iterator var3 = potion.getEffects(stack).iterator();

               while(var3.hasNext()) {
                  Object o = var3.next();
                  PotionEffect effect = (PotionEffect)o;
                  if (stack.getDisplayName().contains("Frog")) {
                     return false;
                  }

                  if (effect.getPotionID() == Potion.moveSpeed.id && effect.getPotionID() != Potion.jump.id) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private boolean isStackSplashHealthPot(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         if (stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
               Iterator var3 = potion.getEffects(stack).iterator();

               while(var3.hasNext()) {
                  Object o = var3.next();
                  PotionEffect effect = (PotionEffect)o;
                  if (effect.getPotionID() == Potion.heal.id) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private void getRegenPotsFromInventory() {
      if (!(this.mc.currentScreen instanceof GuiChest)) {
         for(int index = 9; index < 36; ++index) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSplashRegenPot(stack)) {
               this.mc.playerController.windowClick(0, index, 6, 2, this.mc.thePlayer);
               break;
            }
         }

      }
   }

   private boolean isStackSplashRegenPot(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         if (stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
               Iterator var3 = potion.getEffects(stack).iterator();

               while(var3.hasNext()) {
                  Object o = var3.next();
                  PotionEffect effect = (PotionEffect)o;
                  if (effect.getPotionID() == Potion.regeneration.id) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }
}
