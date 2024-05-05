package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public abstract interface IBehaviorDispenseItem
{
  public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem()
  {
    private static final String __OBFID = "CL_00001200";
    
    public ItemStack dispense(IBlockSource source, ItemStack stack)
    {
      return stack;
    }
  };
  
  public abstract ItemStack dispense(IBlockSource paramIBlockSource, ItemStack paramItemStack);
}
