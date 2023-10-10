package org.alphacentauri.modules;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.Timer;

public class ModuleAutoArmor extends Module implements EventListener {
   private Timer time = new Timer();
   private final int[] boots = new int[]{313, 309, 305, 317, 301};
   private final int[] chestplate = new int[]{311, 307, 303, 315, 299};
   private final int[] helmet = new int[]{310, 306, 302, 314, 298};
   private final int[] leggings = new int[]{312, 308, 304, 316, 300};
   int j;
   int i;

   public ModuleAutoArmor() {
      super("AutoArmor", "Automatically equips best armor", new String[]{"autoarmor"}, Module.Category.Combat, 5478556);
   }

   private int getArrayIndex(int[] array, int input) {
      for(int i = 0; i < array.length - 1; ++i) {
         if(array[i] == input) {
            return i;
         }
      }

      return -1;
   }

   public boolean armourIsBetter(int slot, int[] armourtype) {
      if(AC.getMC().thePlayer.inventory.armorInventory[slot] != null) {
         int currentIndex = 0;
         int finalCurrentIndex = -1;
         int invIndex = 0;
         int finalInvIndex = -1;

         for(int armour2 : armourtype) {
            if(Item.getIdFromItem(AC.getMC().thePlayer.inventory.armorInventory[slot].getItem()) == armour2) {
               finalCurrentIndex = currentIndex;
               break;
            }

            ++currentIndex;
         }

         int j = armourtype.length;

         for(this.i = 0; this.i < j; ++this.i) {
            int armour3 = armourtype[this.i];
            if(this.findItem(armour3) != -1) {
               finalInvIndex = invIndex;
               break;
            }

            ++invIndex;
         }

         if(finalInvIndex > -1) {
            return finalInvIndex < finalCurrentIndex;
         }
      }

      return false;
   }

   private int findItem(int id) {
      for(int index = 9; index < 45; ++index) {
         ItemStack item = AC.getMC().thePlayer.inventoryContainer.getSlot(index).getStack();
         if(item != null && Item.getIdFromItem(item.getItem()) == id) {
            return index;
         }
      }

      return -1;
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(AC.getMC().currentScreen instanceof GuiContainer && !(AC.getMC().currentScreen instanceof GuiInventory)) {
            return;
         }

         if(!this.time.hasMSPassed(125L) && this.getBypass() == AntiCheat.NCP) {
            return;
         }

         this.time.reset();
         if(AC.getMC().thePlayer.openContainer != null && AC.getMC().thePlayer.openContainer.windowId != 0 && AC.getMC().currentScreen != null && !(AC.getMC().currentScreen instanceof GuiInventory)) {
            return;
         }

         boolean dropkek = false;
         int item = -1;
         if(AC.getMC().thePlayer.inventory.armorInventory[0] == null) {
            for(int id : this.boots) {
               if(this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if(this.armourIsBetter(0, this.boots)) {
            item = 8;
         }

         if(AC.getMC().thePlayer.inventory.armorInventory[3] == null) {
            for(int id : this.helmet) {
               if(this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if(this.armourIsBetter(3, this.helmet)) {
            item = 5;
         }

         if(AC.getMC().thePlayer.inventory.armorInventory[1] == null) {
            for(int id : this.leggings) {
               if(this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if(this.armourIsBetter(1, this.leggings)) {
            item = 7;
         }

         if(AC.getMC().thePlayer.inventory.armorInventory[2] == null) {
            for(int id : this.chestplate) {
               if(this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if(this.armourIsBetter(2, this.chestplate)) {
            item = 6;
         }

         boolean hasInvSpace = false;

         ItemStack[] arrayOfItemStack;
         for(ItemStack stack : arrayOfItemStack = AC.getMC().thePlayer.inventory.mainInventory) {
            if(stack == null) {
               hasInvSpace = true;
               break;
            }
         }

         if(item != -1) {
            AC.getMC().playerController.windowClick(0, item, 0, dropkek?4:1, AC.getMC().thePlayer);
            this.time.reset();
         }
      }

   }
}
