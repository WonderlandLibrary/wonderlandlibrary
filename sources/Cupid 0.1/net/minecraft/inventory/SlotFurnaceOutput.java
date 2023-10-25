package net.minecraft.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.MathHelper;

public class SlotFurnaceOutput extends Slot {
  private EntityPlayer thePlayer;
  
  private int field_75228_b;
  
  public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
    this.thePlayer = player;
  }
  
  public boolean isItemValid(ItemStack stack) {
    return false;
  }
  
  public ItemStack decrStackSize(int amount) {
    if (getHasStack())
      this.field_75228_b += Math.min(amount, (getStack()).stackSize); 
    return super.decrStackSize(amount);
  }
  
  public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
    onCrafting(stack);
    super.onPickupFromSlot(playerIn, stack);
  }
  
  protected void onCrafting(ItemStack stack, int amount) {
    this.field_75228_b += amount;
    onCrafting(stack);
  }
  
  protected void onCrafting(ItemStack stack) {
    stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
    if (!this.thePlayer.worldObj.isRemote) {
      int i = this.field_75228_b;
      float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
      if (f == 0.0F) {
        i = 0;
      } else if (f < 1.0F) {
        int j = MathHelper.floor_float(i * f);
        if (j < MathHelper.ceiling_float_int(i * f) && Math.random() < (i * f - j))
          j++; 
        i = j;
      } 
      while (i > 0) {
        int k = EntityXPOrb.getXPSplit(i);
        i -= k;
        this.thePlayer.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
      } 
    } 
    this.field_75228_b = 0;
    if (stack.getItem() == Items.iron_ingot)
      this.thePlayer.triggerAchievement((StatBase)AchievementList.acquireIron); 
    if (stack.getItem() == Items.cooked_fish)
      this.thePlayer.triggerAchievement((StatBase)AchievementList.cookFish); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\inventory\SlotFurnaceOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */