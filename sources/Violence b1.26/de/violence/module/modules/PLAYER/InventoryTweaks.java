package de.violence.module.modules.PLAYER;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryTweaks extends Module {
   public InventoryTweaks() {
      super("InventoryTweaks", Category.PLAYER);
   }

   public void onUpdate() {
      Iterator var2 = this.getHotbar().entrySet().iterator();

      while(var2.hasNext()) {
         Entry itemStacks = (Entry)var2.next();
         ItemStack itemStack = (ItemStack)itemStacks.getValue();
         int slot = ((Integer)itemStacks.getKey()).intValue();
         if(this.isBroken(itemStack)) {
            int bestSlot = this.getBest(itemStack.getItem());
            if(bestSlot != -1) {
               this.sendClick(bestSlot, slot, itemStack.getMaxDamage() == 0);
            }
         }
      }

      super.onUpdate();
   }

   private void sendClick(int slotClick, int inSlot, boolean stackable) {
      this.mc.thePlayer.inventoryContainer.slotClick(slotClick, 0, 1, this.mc.thePlayer);
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slotClick, 0, 1, this.mc.thePlayer);
   }

   private List searchForItem(Item item) {
      ArrayList itemStacks = new ArrayList();

      for(int i = 8; i < 36; ++i) {
         ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if(itemStack != null && itemStack.getItem() == item) {
            itemStacks.add(Integer.valueOf(i));
         }
      }

      return itemStacks;
   }

   private int getBest(Item item) {
      int currentItem = -1;
      int currentValence = 0;
      Iterator var5 = this.searchForItem(item).iterator();

      while(var5.hasNext()) {
         int slots = ((Integer)var5.next()).intValue();
         ItemStack itemStacks = this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack();
         if(currentItem == -1) {
            currentItem = slots;
            currentValence = this.getValence(itemStacks);
         }

         if(currentValence < this.getValence(itemStacks)) {
            currentItem = slots;
            currentValence = this.getValence(itemStacks);
         }
      }

      return currentItem;
   }

   private int getValence(ItemStack itemStack) {
      int valence = 0;
      if(itemStack == null) {
         return valence;
      } else {
         if(itemStack.hasTagCompound()) {
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(32).getDouble("lvl"));
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(33).getDouble("lvl"));
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(35).getDouble("lvl"));
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(16).getDouble("lvl"));
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(19).getDouble("lvl"));
            valence = (int)((double)valence + itemStack.getEnchantmentTagList().getCompoundTagAt(20).getDouble("lvl"));
         }

         return valence;
      }
   }

   private Map getHotbar() {
      HashMap itemStacks = new HashMap();

      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();
         if(itemStack != null) {
            itemStacks.put(Integer.valueOf(i + 36), itemStack);
         }
      }

      return itemStacks;
   }

   private boolean isBroken(ItemStack itemStack) {
      return itemStack.getMaxDamage() == 0?itemStack.stackSize == 1:itemStack.getItemDamage() >= itemStack.getMaxDamage() - 1;
   }
}
