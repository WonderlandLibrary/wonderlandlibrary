// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockDirectional extends Block
{
    public static final PropertyDirection FACING;
    
    protected BlockDirectional(final Material materialIn) {
        super(materialIn);
    }
    
    static {
        FACING = PropertyDirection.create("facing");
    }
}
