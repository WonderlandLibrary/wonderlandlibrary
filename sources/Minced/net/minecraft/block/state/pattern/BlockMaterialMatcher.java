// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class BlockMaterialMatcher implements Predicate<IBlockState>
{
    private final Material material;
    
    private BlockMaterialMatcher(final Material materialIn) {
        this.material = materialIn;
    }
    
    public static BlockMaterialMatcher forMaterial(final Material materialIn) {
        return new BlockMaterialMatcher(materialIn);
    }
    
    public boolean apply(@Nullable final IBlockState p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getMaterial() == this.material;
    }
}
