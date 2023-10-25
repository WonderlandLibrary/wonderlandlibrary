/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable
extends TileEntity
implements IInteractionObject,
ILockableContainer {
    private LockCode code = LockCode.EMPTY_CODE;

    @Override
    public LockCode getLockCode() {
        return this.code;
    }

    @Override
    public boolean isLocked() {
        return this.code != null && !this.code.isEmpty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.code = LockCode.fromNBT(nBTTagCompound);
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        if (this.code != null) {
            this.code.toNBT(nBTTagCompound);
        }
    }

    @Override
    public void setLockCode(LockCode lockCode) {
        this.code = lockCode;
    }
}

