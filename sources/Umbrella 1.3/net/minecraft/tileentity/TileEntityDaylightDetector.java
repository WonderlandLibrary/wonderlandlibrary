/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDaylightDetector
extends TileEntity
implements IUpdatePlayerListBox {
    private static final String __OBFID = "CL_00000350";

    @Override
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L) {
            this.blockType = this.getBlockType();
            if (this.blockType instanceof BlockDaylightDetector) {
                ((BlockDaylightDetector)this.blockType).func_180677_d(this.worldObj, this.pos);
            }
        }
    }
}

