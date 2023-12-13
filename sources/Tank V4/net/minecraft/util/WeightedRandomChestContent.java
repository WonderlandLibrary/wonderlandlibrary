package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

public class WeightedRandomChestContent extends WeightedRandom.Item {
   private int maxStackSize;
   private ItemStack theItemId;
   private int minStackSize;

   public static void generateDispenserContents(Random var0, List var1, TileEntityDispenser var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(var0, var1);
         int var6 = var5.minStackSize + var0.nextInt(var5.maxStackSize - var5.minStackSize + 1);
         if (var5.theItemId.getMaxStackSize() >= var6) {
            ItemStack var9 = var5.theItemId.copy();
            var9.stackSize = var6;
            var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var9);
         } else {
            for(int var7 = 0; var7 < var6; ++var7) {
               ItemStack var8 = var5.theItemId.copy();
               var8.stackSize = 1;
               var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var8);
            }
         }
      }

   }

   public static void generateChestContents(Random var0, List var1, IInventory var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(var0, var1);
         int var6 = var5.minStackSize + var0.nextInt(var5.maxStackSize - var5.minStackSize + 1);
         if (var5.theItemId.getMaxStackSize() >= var6) {
            ItemStack var9 = var5.theItemId.copy();
            var9.stackSize = var6;
            var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var9);
         } else {
            for(int var7 = 0; var7 < var6; ++var7) {
               ItemStack var8 = var5.theItemId.copy();
               var8.stackSize = 1;
               var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var8);
            }
         }
      }

   }

   public static List func_177629_a(List var0, WeightedRandomChestContent... var1) {
      ArrayList var2 = Lists.newArrayList((Iterable)var0);
      Collections.addAll(var2, var1);
      return var2;
   }

   public WeightedRandomChestContent(ItemStack var1, int var2, int var3, int var4) {
      super(var4);
      this.theItemId = var1;
      this.minStackSize = var2;
      this.maxStackSize = var3;
   }

   public WeightedRandomChestContent(Item var1, int var2, int var3, int var4, int var5) {
      super(var5);
      this.theItemId = new ItemStack(var1, 1, var2);
      this.minStackSize = var3;
      this.maxStackSize = var4;
   }
}
