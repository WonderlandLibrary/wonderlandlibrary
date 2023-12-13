package net.minecraft.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

public class SlotFurnaceOutput extends Slot {
   private EntityPlayer thePlayer;
   private int field_75228_b;

   protected void onCrafting(ItemStack var1) {
      var1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
      if (!this.thePlayer.worldObj.isRemote) {
         int var2 = this.field_75228_b;
         float var3 = FurnaceRecipes.instance().getSmeltingExperience(var1);
         int var4;
         if (var3 == 0.0F) {
            var2 = 0;
         } else if (var3 < 1.0F) {
            var4 = MathHelper.floor_float((float)var2 * var3);
            if (var4 < MathHelper.ceiling_float_int((float)var2 * var3) && Math.random() < (double)((float)var2 * var3 - (float)var4)) {
               ++var4;
            }

            var2 = var4;
         }

         while(var2 > 0) {
            var4 = EntityXPOrb.getXPSplit(var2);
            var2 -= var4;
            this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, var4));
         }
      }

      this.field_75228_b = 0;
      if (var1.getItem() == Items.iron_ingot) {
         this.thePlayer.triggerAchievement(AchievementList.acquireIron);
      }

      if (var1.getItem() == Items.cooked_fish) {
         this.thePlayer.triggerAchievement(AchievementList.cookFish);
      }

   }

   public ItemStack decrStackSize(int var1) {
      if (this.getHasStack()) {
         this.field_75228_b += Math.min(var1, this.getStack().stackSize);
      }

      return super.decrStackSize(var1);
   }

   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(var2);
      super.onPickupFromSlot(var1, var2);
   }

   protected void onCrafting(ItemStack var1, int var2) {
      this.field_75228_b += var2;
      this.onCrafting(var1);
   }

   public SlotFurnaceOutput(EntityPlayer var1, IInventory var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.thePlayer = var1;
   }
}
