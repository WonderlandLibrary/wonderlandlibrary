/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentStyle;
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
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.code = LockCode.fromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.code == null) return;
        this.code.toNBT(compound);
    }

    @Override
    public boolean isLocked() {
        if (this.code == null) return false;
        if (this.code.isEmpty()) return false;
        return true;
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
    public IChatComponent getDisplayName() {
        ChatComponentStyle chatComponentStyle;
        if (this.hasCustomName()) {
            chatComponentStyle = new ChatComponentText(this.getName());
            return chatComponentStyle;
        }
        chatComponentStyle = new ChatComponentTranslation(this.getName(), new Object[0]);
        return chatComponentStyle;
    }
}

