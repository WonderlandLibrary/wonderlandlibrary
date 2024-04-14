package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;

public class ChestStealer extends Module {
   private float cooldown = 0.0F;
   private VSetting chestStealerMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Delayed", "Instant"}), "Delayed");

   public ChestStealer() {
      super("ChestStealer", Category.PLAYER);
   }

   public void onUpdate() {
      if(this.isToggled() && this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChest) {
         if(this.chestStealerMode.getActiveMode().equalsIgnoreCase("Delayed")) {
            this.updateNormal();
         } else {
            this.updateFAST();
         }
      }

      super.onUpdate();
   }

   public void updateNormal() {
      GuiChest chest = (GuiChest)this.mc.currentScreen;
      this.cooldown += 0.1F;
      if(this.cooldown >= 0.3F) {
         if(chest.inventorySlots != null) {
            boolean grabbed = false;

            for(int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; ++i) {
               ItemStack stack = chest.inventorySlots.getSlot(i).getStack();
               if(stack != null) {
                  this.mc.playerController.windowClick(chest.inventorySlots.windowId, i, 1, 2, this.mc.thePlayer);
                  this.cooldown = 0.0F;
                  grabbed = true;
                  break;
               }
            }

            if(!grabbed) {
               this.mc.thePlayer.closeScreen();
            }

         }
      }
   }

   public void updateFAST() {
      GuiChest chest = (GuiChest)this.mc.currentScreen;
      if(chest.inventorySlots != null) {
         boolean grabbed = false;

         for(int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; ++i) {
            ItemStack stack = chest.inventorySlots.getSlot(i).getStack();
            if(stack != null) {
               this.mc.playerController.windowClick(chest.inventorySlots.windowId, i, 1, 2, this.mc.thePlayer);
            }
         }

         this.mc.thePlayer.closeScreen();
      }
   }
}
