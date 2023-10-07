// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockRotatedPillar extends Block
{
    public static final PropertyEnum field_176298_M;
    private static final String __OBFID = "CL_00000302";
    
    protected BlockRotatedPillar(final Material p_i45425_1_) {
        super(p_i45425_1_);
    }
    
    static {
        field_176298_M = PropertyEnum.create("axis", EnumFacing.Axis.class);
    }
}
