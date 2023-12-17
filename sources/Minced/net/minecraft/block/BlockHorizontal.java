// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockHorizontal extends Block
{
    public static final PropertyDirection FACING;
    
    protected BlockHorizontal(final Material materialIn) {
        super(materialIn);
    }
    
    protected BlockHorizontal(final Material materialIn, final MapColor colorIn) {
        super(materialIn, colorIn);
    }
    
    static {
        FACING = PropertyDirection.create("facing", (Predicate<EnumFacing>)EnumFacing.Plane.HORIZONTAL);
    }
}
