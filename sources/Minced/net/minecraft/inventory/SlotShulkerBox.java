// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemStack;

public class SlotShulkerBox extends Slot
{
    public SlotShulkerBox(final IInventory p_i47265_1_, final int slotIndexIn, final int xPosition, final int yPosition) {
        super(p_i47265_1_, slotIndexIn, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
    }
}
