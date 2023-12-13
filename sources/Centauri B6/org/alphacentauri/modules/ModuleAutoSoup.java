package org.alphacentauri.modules;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender2D;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.sexyness.ingame.InfoRenderer;

public class ModuleAutoSoup extends Module implements EventListener {
   public Property MinHealth = new Property(this, "Health", Float.valueOf(10.0F));
   public Property StockAmount = new Property(this, "StockAmount", Integer.valueOf(2));
   public Property ThrowAway = new Property(this, "ThrowAway", Boolean.valueOf(true));
   public Property KeepStock = new Property(this, "KeepStock", Boolean.valueOf(false));
   public Property DropDelay = new Property(this, "DropDelay", Integer.valueOf(0));
   public boolean isSouping = false;
   public boolean isRestocking = false;
   int item = 0;
   int stockcooldown = 0;
   int dropleft = 0;

   public ModuleAutoSoup() {
      super("AutoSoup", "Automatically consume soups", new String[]{"autosoup"}, Module.Category.Combat, 2289301);
   }

   private int PlaceSoupInHotbar() {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            AC.getMC().playerController.windowClick(AC.getMC().getPlayer().openContainer.windowId, i, 0, 2, AC.getMC().getPlayer());
            AC.getMC().playerController.updateController();
            return this.SoupInHotbar();
         }
      }

      return -1;
   }

   private void UseSoup() {
      AC.getMC().getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(AC.getMC().getPlayer().getHeldItem()));
   }

   private boolean HasSoups() {
      for(int i = 0; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            return true;
         }
      }

      return false;
   }

   private int GetSoupAmount() {
      int amount = 0;

      for(int i = 0; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            ++amount;
         }
      }

      return amount;
   }

   private boolean FreeSlotInHotbar() {
      boolean hasFree = false;

      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack == null || stack.getItem() == Items.bowl) {
            hasFree = true;
            break;
         }
      }

      return hasFree;
   }

   private boolean isSoup(ItemStack stack) {
      return stack == null?false:stack.getItem() == Items.mushroom_stew;
   }

   private int SoupsInHotbar() {
      int count = 0;

      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            ++count;
         }
      }

      return count;
   }

   private int GetFirstFreeSlot() {
      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack == null || stack.getItem() == Items.bowl) {
            return i - 36;
         }
      }

      return 1;
   }

   private boolean isUsedSoup(ItemStack stack) {
      return stack == null?false:stack.getItem() == Items.bowl;
   }

   private boolean HasSoupInInv() {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            return true;
         }
      }

      return false;
   }

   private int SoupInHotbar() {
      for(int i = 36; i < 45; ++i) {
         ItemStack stack = AC.getMC().getPlayer().inventoryContainer.getSlot(i).getStack();
         if(stack != null && this.isSoup(stack)) {
            return i - 36;
         }
      }

      return -1;
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender2D) {
         InfoRenderer.renderInfo("§7Soups: §r" + this.GetSoupAmount());
      } else if(event instanceof EventTick) {
         if(AC.getMC().getPlayer() == null) {
            return;
         }

         if(AC.getMC().getPlayer().inventory == null) {
            return;
         }

         if(!this.HasSoups() && !this.isSouping) {
            return;
         }

         if(this.isSouping && this.dropleft <= 0) {
            if(this.isUsedSoup(AC.getMC().getPlayer().inventory.getCurrentItem()) && ((Boolean)this.ThrowAway.value).booleanValue()) {
               AC.getMC().getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.DROP_ALL_ITEMS, new BlockPos(0, 0, 0), EnumFacing.NORTH));
            }

            if(this.item != -1) {
               AC.getMC().getPlayer().inventory.currentItem = this.item;
               AC.getMC().playerController.syncCurrentPlayItem();
            }

            AC.getMC().playerController.updateController();
            this.isSouping = false;
            this.item = -1;
         } else if(((Boolean)this.KeepStock.value).booleanValue()) {
            if(this.stockcooldown > 0) {
               --this.stockcooldown;
            } else {
               if(this.FreeSlotInHotbar() && (this.SoupsInHotbar() < ((Integer)this.StockAmount.value).intValue() || this.isRestocking)) {
                  if(this.HasSoupInInv()) {
                     this.PlaceSoupInHotbar();
                     this.isRestocking = true;
                  }
               } else if(!this.FreeSlotInHotbar()) {
                  this.isRestocking = false;
               }

               this.stockcooldown = 3;
            }
         }

         if(this.dropleft > 0) {
            --this.dropleft;
         }

         if(!this.isSouping && AC.getMC().getPlayer().getHealth() < AC.getMC().getPlayer().getMaxHealth() && AC.getMC().getPlayer().getHealth() <= ((Float)this.MinHealth.value).floatValue()) {
            int soup = this.SoupInHotbar();
            this.item = AC.getMC().getPlayer().inventory.currentItem;
            if(soup == -1) {
               soup = this.PlaceSoupInHotbar();
            }

            if(soup == -1) {
               return;
            }

            AC.getMC().getPlayer().inventory.currentItem = soup;
            AC.getMC().playerController.syncCurrentPlayItem();
            this.UseSoup();
            this.isSouping = true;
            this.dropleft = ((Integer)this.DropDelay.value).intValue();
         }
      }

   }
}
