package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;

public class SlotCrafting extends Slot {
   private final InventoryCrafting craftMatrix;
   private final EntityPlayer thePlayer;
   private int amountCrafted;

   protected void onCrafting(ItemStack var1, int var2) {
      this.amountCrafted += var2;
      this.onCrafting(var1);
   }

   public SlotCrafting(EntityPlayer var1, InventoryCrafting var2, IInventory var3, int var4, int var5, int var6) {
      super(var3, var4, var5, var6);
      this.thePlayer = var1;
      this.craftMatrix = var2;
   }

   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   public ItemStack decrStackSize(int var1) {
      if (this.getHasStack()) {
         this.amountCrafted += Math.min(var1, this.getStack().stackSize);
      }

      return super.decrStackSize(var1);
   }

   protected void onCrafting(ItemStack var1) {
      if (this.amountCrafted > 0) {
         var1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
      }

      this.amountCrafted = 0;
      if (var1.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
         this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
      }

      if (var1.getItem() instanceof ItemPickaxe) {
         this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
      }

      if (var1.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
         this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
      }

      if (var1.getItem() instanceof ItemHoe) {
         this.thePlayer.triggerAchievement(AchievementList.buildHoe);
      }

      if (var1.getItem() == Items.bread) {
         this.thePlayer.triggerAchievement(AchievementList.makeBread);
      }

      if (var1.getItem() == Items.cake) {
         this.thePlayer.triggerAchievement(AchievementList.bakeCake);
      }

      if (var1.getItem() instanceof ItemPickaxe && ((ItemPickaxe)var1.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
         this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
      }

      if (var1.getItem() instanceof ItemSword) {
         this.thePlayer.triggerAchievement(AchievementList.buildSword);
      }

      if (var1.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
         this.thePlayer.triggerAchievement(AchievementList.enchantments);
      }

      if (var1.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
         this.thePlayer.triggerAchievement(AchievementList.bookcase);
      }

      if (var1.getItem() == Items.golden_apple && var1.getMetadata() == 1) {
         this.thePlayer.triggerAchievement(AchievementList.overpowered);
      }

   }

   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(var2);
      ItemStack[] var3 = CraftingManager.getInstance().func_180303_b(this.craftMatrix, var1.worldObj);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ItemStack var5 = this.craftMatrix.getStackInSlot(var4);
         ItemStack var6 = var3[var4];
         if (var5 != null) {
            this.craftMatrix.decrStackSize(var4, 1);
         }

         if (var6 != null) {
            if (this.craftMatrix.getStackInSlot(var4) == null) {
               this.craftMatrix.setInventorySlotContents(var4, var6);
            } else if (!this.thePlayer.inventory.addItemStackToInventory(var6)) {
               this.thePlayer.dropPlayerItemWithRandomChoice(var6, false);
            }
         }
      }

   }
}
