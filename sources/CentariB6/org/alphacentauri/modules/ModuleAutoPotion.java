package org.alphacentauri.modules;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender2D;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.sexyness.ingame.InfoRenderer;
import org.alphacentauri.management.util.RotationUtils;

public class ModuleAutoPotion extends Module implements EventListener {
   private Property MinHealth = new Property(this, "Health", Float.valueOf(10.0F));
   private int item = 0;
   private int cooldown = 3;
   public boolean isPoting = false;

   public ModuleAutoPotion() {
      super("AutoPotion", "Automatically throws potions", new String[]{"autopotion"}, Module.Category.Combat, 13204235);
   }

   private int GetFirstFreeSlot() {
      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack == null) {
            return i - 36;
         }
      }

      return 1;
   }

   private void UsePot() {
      AC.getMC().getPlayer().sendQueue.addToSendQueue(new C05PacketPlayerLook(RotationUtils.isSet()?RotationUtils.getYaw():AC.getMC().getPlayer().rotationYaw, 90.0F, AC.getMC().getPlayer().onGround));
      AC.getMC().getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(AC.getMC().getPlayer().getHeldItem()));
   }

   private int PlacePotInHotbar() {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isPot(stack)) {
            AC.getMC().playerController.windowClick(AC.getMC().getPlayer().openContainer.windowId, i, this.GetFirstFreeSlot(), 2, AC.getMC().getPlayer());
            AC.getMC().playerController.updateController();
            return this.PotInHotbar();
         }
      }

      return -1;
   }

   private int PotInHotbar() {
      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isPot(stack)) {
            return i - 36;
         }
      }

      return -1;
   }

   private boolean isPot(ItemStack stack) {
      if(stack == null) {
         return false;
      } else if(stack.getItem() == null) {
         return false;
      } else {
         boolean returnment = false;

         try {
            if(stack.getItem() instanceof ItemPotion) {
               for(PotionEffect effect : ((ItemPotion)stack.getItem()).getEffects(stack)) {
                  if(effect.getPotionID() == Potion.heal.getId()) {
                     returnment = true;
                     break;
                  }
               }
            }

            return returnment;
         } catch (Exception var5) {
            return false;
         }
      }
   }

   private boolean HasPots() {
      for(int i = 0; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isPot(stack)) {
            return true;
         }
      }

      return false;
   }

   private int GetPotAmount() {
      int amount = 0;

      for(int i = 0; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isPot(stack)) {
            ++amount;
         }
      }

      return amount;
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender2D) {
         InfoRenderer.renderInfo("§7Potions: §r" + this.GetPotAmount());
      } else if(event instanceof EventTick) {
         if(AC.getMC().getPlayer() == null) {
            return;
         }

         if(AC.getMC().getPlayer().inventory == null) {
            return;
         }

         if(!this.HasPots()) {
            return;
         }

         if(AC.getMC().getPlayer().getHealth() < AC.getMC().getPlayer().getMaxHealth() && AC.getMC().getPlayer().getHealth() <= ((Float)this.MinHealth.value).floatValue() && this.cooldown <= 0) {
            int pot = this.PotInHotbar();
            this.item = AC.getMC().getPlayer().inventory.currentItem;
            if(pot == -1) {
               pot = this.PlacePotInHotbar();
            }

            if(pot == -1) {
               return;
            }

            AC.getMC().getPlayer().inventory.currentItem = pot;
            AC.getMC().playerController.syncCurrentPlayItem();
            this.UsePot();
            this.isPoting = true;
            AC.getMC().getPlayer().inventory.currentItem = this.item;
            AC.getMC().playerController.syncCurrentPlayItem();
            AC.getMC().playerController.updateController();
            this.cooldown = 3;
            this.isPoting = false;
         }

         if(this.cooldown > 0) {
            --this.cooldown;
         }
      }

   }
}
