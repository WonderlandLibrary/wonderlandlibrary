/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable
extends TileEntity
implements ILockableContainer {
    private LockCode code = LockCode.EMPTY_CODE;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.code = LockCode.fromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.code != null) {
            this.code.toNBT(compound);
        }
        return compound;
    }

    @Override
    public boolean isLocked() {
        return this.code != null && !this.code.isEmpty();
    }

    @Override
    public LockCode getLockCode() {
        return this.code;
    }

    @Override
    public void setLockCode(LockCode code) {
        this.code = code;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
}

