package net.minecraft.client.gui.inventory;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CreativeCrafting implements ICrafting {
   private final Minecraft mc;

   public void sendProgressBarUpdate(Container var1, int var2, int var3) {
   }

   public CreativeCrafting(Minecraft var1) {
      this.mc = var1;
   }

   public void sendSlotContents(Container var1, int var2, ItemStack var3) {
      Minecraft.playerController.sendSlotPacket(var3, var2);
   }

   public void updateCraftingInventory(Container var1, List var2) {
   }

   public void func_175173_a(Container var1, IInventory var2) {
   }
}
