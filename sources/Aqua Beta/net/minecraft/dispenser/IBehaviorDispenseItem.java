package net.minecraft.dispenser;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
    public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new /* Unavailable Anonymous Inner Class!! */;

    public ItemStack dispense(IBlockSource var1, ItemStack var2);
}
