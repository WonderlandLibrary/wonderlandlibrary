// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

public class ItemBook extends Item
{
    private static final String __OBFID = "CL_00001775";
    
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return stack.stackSize == 1;
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
