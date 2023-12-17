// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class BlockMatcher implements Predicate<IBlockState>
{
    private final Block block;
    
    private BlockMatcher(final Block blockType) {
        this.block = blockType;
    }
    
    public static BlockMatcher forBlock(final Block blockType) {
        return new BlockMatcher(blockType);
    }
    
    public boolean apply(@Nullable final IBlockState p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getBlock() == this.block;
    }
}
