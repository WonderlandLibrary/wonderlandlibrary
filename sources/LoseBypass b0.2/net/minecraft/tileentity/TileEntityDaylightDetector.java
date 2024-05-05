/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityDaylightDetector
extends TileEntity
implements ITickable {
    @Override
    public void update() {
        if (this.worldObj == null) return;
        if (this.worldObj.isRemote) return;
        if (this.worldObj.getTotalWorldTime() % 20L != 0L) return;
        this.blockType = this.getBlockType();
        if (!(this.blockType instanceof BlockDaylightDetector)) return;
        ((BlockDaylightDetector)this.blockType).updatePower(this.worldObj, this.pos);
    }
}

