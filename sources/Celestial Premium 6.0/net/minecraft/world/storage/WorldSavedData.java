/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
    public final String mapName;
    private boolean dirty;

    public WorldSavedData(String name) {
        this.mapName = name;
    }

    public abstract void readFromNBT(NBTTagCompound var1);

    public abstract NBTTagCompound writeToNBT(NBTTagCompound var1);

    public void markDirty() {
        this.setDirty(true);
    }

    public void setDirty(boolean isDirty) {
        this.dirty = isDirty;
    }

    public boolean isDirty() {
        return this.dirty;
    }
}

