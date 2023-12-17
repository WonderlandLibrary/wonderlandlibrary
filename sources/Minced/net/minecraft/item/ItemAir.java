// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class ItemAir extends Item
{
    private final Block block;
    
    public ItemAir(final Block blockIn) {
        this.block = blockIn;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return this.block.getTranslationKey();
    }
    
    @Override
    public String getTranslationKey() {
        return this.block.getTranslationKey();
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.block.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
