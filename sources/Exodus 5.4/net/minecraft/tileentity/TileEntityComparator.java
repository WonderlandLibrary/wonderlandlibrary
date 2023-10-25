/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComparator
extends TileEntity {
    private int outputSignal;

    public int getOutputSignal() {
        return this.outputSignal;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("OutputSignal", this.outputSignal);
    }

    public void setOutputSignal(int n) {
        this.outputSignal = n;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.outputSignal = nBTTagCompound.getInteger("OutputSignal");
    }
}

