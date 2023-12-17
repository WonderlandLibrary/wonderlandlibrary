// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.EnumFacing;

public class TileEntityEndPortal extends TileEntity
{
    public boolean shouldRenderFace(final EnumFacing p_184313_1_) {
        return p_184313_1_ == EnumFacing.UP;
    }
}
