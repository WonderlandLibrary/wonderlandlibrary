/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.dispenser;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
    public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem(){
        private static final String __OBFID = "CL_00001200";

        @Override
        public ItemStack dispense(IBlockSource source, ItemStack stack) {
            return stack;
        }
    };

    public ItemStack dispense(IBlockSource var1, ItemStack var2);
}

