// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state.pattern;

import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.state.BlockState;
import com.google.common.base.Predicate;

public class BlockStateHelper implements Predicate
{
    private final BlockState field_177641_a;
    private final Map field_177640_b;
    private static final String __OBFID = "CL_00002019";
    
    private BlockStateHelper(final BlockState p_i45653_1_) {
        this.field_177640_b = Maps.newHashMap();
        this.field_177641_a = p_i45653_1_;
    }
    
    public static BlockStateHelper forBlock(final Block p_177638_0_) {
        return new BlockStateHelper(p_177638_0_.getBlockState());
    }
    
    public boolean func_177639_a(final IBlockState p_177639_1_) {
        if (p_177639_1_ != null && p_177639_1_.getBlock().equals(this.field_177641_a.getBlock())) {
            for (final Map.Entry var3 : this.field_177640_b.entrySet()) {
                final Comparable var4 = p_177639_1_.getValue(var3.getKey());
                if (!var3.getValue().apply((Object)var4)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public BlockStateHelper func_177637_a(final IProperty p_177637_1_, final Predicate p_177637_2_) {
        if (!this.field_177641_a.getProperties().contains(p_177637_1_)) {
            throw new IllegalArgumentException(this.field_177641_a + " cannot support property " + p_177637_1_);
        }
        this.field_177640_b.put(p_177637_1_, p_177637_2_);
        return this;
    }
    
    public boolean apply(final Object p_apply_1_) {
        return this.func_177639_a((IBlockState)p_apply_1_);
    }
}
