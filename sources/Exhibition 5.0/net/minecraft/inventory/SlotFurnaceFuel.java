// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.item.ItemStack;

public class SlotFurnaceFuel extends Slot
{
    private static final String __OBFID = "CL_00002184";
    
    public SlotFurnaceFuel(final IInventory p_i45795_1_, final int p_i45795_2_, final int p_i45795_3_, final int p_i45795_4_) {
        super(p_i45795_1_, p_i45795_2_, p_i45795_3_, p_i45795_4_);
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack) || func_178173_c_(stack);
    }
    
    @Override
    public int func_178170_b(final ItemStack p_178170_1_) {
        return func_178173_c_(p_178170_1_) ? 1 : super.func_178170_b(p_178170_1_);
    }
    
    public static boolean func_178173_c_(final ItemStack p_178173_0_) {
        return p_178173_0_ != null && p_178173_0_.getItem() != null && p_178173_0_.getItem() == Items.bucket;
    }
}
