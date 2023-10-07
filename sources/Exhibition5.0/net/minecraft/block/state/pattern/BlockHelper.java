// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state.pattern;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import com.google.common.base.Predicate;

public class BlockHelper implements Predicate
{
    private final Block block;
    private static final String __OBFID = "CL_00002020";
    
    private BlockHelper(final Block p_i45654_1_) {
        this.block = p_i45654_1_;
    }
    
    public static BlockHelper forBlock(final Block p_177642_0_) {
        return new BlockHelper(p_177642_0_);
    }
    
    public boolean isBlockEqualTo(final IBlockState p_177643_1_) {
        return p_177643_1_ != null && p_177643_1_.getBlock() == this.block;
    }
    
    public boolean apply(final Object p_apply_1_) {
        return this.isBlockEqualTo((IBlockState)p_apply_1_);
    }
}
