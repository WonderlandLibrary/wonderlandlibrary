package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleChestStealer extends Module implements EventListener {
   private Property set_cooldown = new Property(this, "CoolDown", Integer.valueOf(0));
   private int cooldown;

   public ModuleChestStealer() {
      super("ChestStealer", "Oh, a chest, now it\'s empty", new String[]{"cheststealer"}, Module.Category.World, 15712561);
   }

   public void setBypass(AntiCheat ac) {
      if(ac != AntiCheat.NCP && ac != AntiCheat.Reflex) {
         this.set_cooldown.value = Integer.valueOf(0);
      } else if(ac == AntiCheat.NCP) {
         this.set_cooldown.value = Integer.valueOf(1);
      }

      super.setBypass(ac);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(this.cooldown > 0) {
            --this.cooldown;
            return;
         }

         if(AC.getMC().currentScreen instanceof GuiChest) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            Container chest = player.openContainer;
            boolean hasSpace = false;

            for(int i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
               Slot slot = chest.getSlot(i);
               if(slot == null || !slot.getHasStack()) {
                  hasSpace = true;
                  break;
               }
            }

            if(!hasSpace) {
               return;
            }

            while(this.cooldown == 0) {
               boolean item_found = false;

               for(int i = 0; i < chest.inventorySlots.size() - 36; ++i) {
                  Slot slot = chest.getSlot(i);
                  if(slot.getHasStack() && slot.getStack() != null) {
                     AC.getMC().playerController.windowClick(chest.windowId, i, 0, 1, player);
                     this.cooldown = ((Integer)this.set_cooldown.value).intValue();
                     item_found = true;
                     break;
                  }
               }

               if(!item_found) {
                  AC.getMC().displayGuiScreen((GuiScreen)null);
                  player.sendQueue.addToSendQueue(new C0DPacketCloseWindow(chest.windowId));
                  KeyBinding.fixKeybinds();
                  break;
               }

               hasSpace = false;

               for(int i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
                  Slot slot = chest.getSlot(i);
                  if(slot == null || !slot.getHasStack()) {
                     hasSpace = true;
                     break;
                  }
               }

               if(!hasSpace) {
                  return;
               }
            }
         }
      }

   }
}
