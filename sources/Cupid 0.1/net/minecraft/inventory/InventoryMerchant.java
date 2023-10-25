package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
  private final IMerchant theMerchant;
  
  private ItemStack[] theInventory = new ItemStack[3];
  
  private final EntityPlayer thePlayer;
  
  private MerchantRecipe currentRecipe;
  
  private int currentRecipeIndex;
  
  public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
    this.thePlayer = thePlayerIn;
    this.theMerchant = theMerchantIn;
  }
  
  public int getSizeInventory() {
    return this.theInventory.length;
  }
  
  public ItemStack getStackInSlot(int index) {
    return this.theInventory[index];
  }
  
  public ItemStack decrStackSize(int index, int count) {
    if (this.theInventory[index] != null) {
      if (index == 2) {
        ItemStack itemstack2 = this.theInventory[index];
        this.theInventory[index] = null;
        return itemstack2;
      } 
      if ((this.theInventory[index]).stackSize <= count) {
        ItemStack itemstack1 = this.theInventory[index];
        this.theInventory[index] = null;
        if (inventoryResetNeededOnSlotChange(index))
          resetRecipeAndSlots(); 
        return itemstack1;
      } 
      ItemStack itemstack = this.theInventory[index].splitStack(count);
      if ((this.theInventory[index]).stackSize == 0)
        this.theInventory[index] = null; 
      if (inventoryResetNeededOnSlotChange(index))
        resetRecipeAndSlots(); 
      return itemstack;
    } 
    return null;
  }
  
  private boolean inventoryResetNeededOnSlotChange(int p_70469_1_) {
    return (p_70469_1_ == 0 || p_70469_1_ == 1);
  }
  
  public ItemStack removeStackFromSlot(int index) {
    if (this.theInventory[index] != null) {
      ItemStack itemstack = this.theInventory[index];
      this.theInventory[index] = null;
      return itemstack;
    } 
    return null;
  }
  
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.theInventory[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit())
      stack.stackSize = getInventoryStackLimit(); 
    if (inventoryResetNeededOnSlotChange(index))
      resetRecipeAndSlots(); 
  }
  
  public String getName() {
    return "mob.villager";
  }
  
  public boolean hasCustomName() {
    return false;
  }
  
  public IChatComponent getDisplayName() {
    return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer player) {
    return (this.theMerchant.getCustomer() == player);
  }
  
  public void openInventory(EntityPlayer player) {}
  
  public void closeInventory(EntityPlayer player) {}
  
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  
  public void markDirty() {
    resetRecipeAndSlots();
  }
  
  public void resetRecipeAndSlots() {
    this.currentRecipe = null;
    ItemStack itemstack = this.theInventory[0];
    ItemStack itemstack1 = this.theInventory[1];
    if (itemstack == null) {
      itemstack = itemstack1;
      itemstack1 = null;
    } 
    if (itemstack == null) {
      setInventorySlotContents(2, (ItemStack)null);
    } else {
      MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
      if (merchantrecipelist != null) {
        MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
          this.currentRecipe = merchantrecipe;
          setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
        } else if (itemstack1 != null) {
          merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
          if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
            this.currentRecipe = merchantrecipe;
            setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
          } else {
            setInventorySlotContents(2, (ItemStack)null);
          } 
        } else {
          setInventorySlotContents(2, (ItemStack)null);
        } 
      } 
    } 
    this.theMerchant.verifySellingItem(getStackInSlot(2));
  }
  
  public MerchantRecipe getCurrentRecipe() {
    return this.currentRecipe;
  }
  
  public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
    this.currentRecipeIndex = currentRecipeIndexIn;
    resetRecipeAndSlots();
  }
  
  public int getField(int id) {
    return 0;
  }
  
  public void setField(int id, int value) {}
  
  public int getFieldCount() {
    return 0;
  }
  
  public void clear() {
    for (int i = 0; i < this.theInventory.length; i++)
      this.theInventory[i] = null; 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\inventory\InventoryMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */