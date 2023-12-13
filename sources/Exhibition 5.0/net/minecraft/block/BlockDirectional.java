// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockDirectional extends Block
{
    public static final PropertyDirection AGE;
    private static final String __OBFID = "CL_00000227";
    
    protected BlockDirectional(final Material p_i45401_1_) {
        super(p_i45401_1_);
    }
    
    static {
        AGE = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
}
