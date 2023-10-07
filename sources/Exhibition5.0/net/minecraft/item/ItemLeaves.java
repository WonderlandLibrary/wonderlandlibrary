// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock
{
    private final BlockLeaves field_150940_b;
    private static final String __OBFID = "CL_00000046";
    
    public ItemLeaves(final BlockLeaves p_i45344_1_) {
        super(p_i45344_1_);
        this.field_150940_b = p_i45344_1_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage | 0x4;
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return this.field_150940_b.getRenderColor(this.field_150940_b.getStateFromMeta(stack.getMetadata()));
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.field_150940_b.func_176233_b(stack.getMetadata()).func_176840_c();
    }
}
