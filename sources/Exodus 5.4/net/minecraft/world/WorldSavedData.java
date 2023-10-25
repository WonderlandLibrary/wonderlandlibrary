/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
    public final String mapName;
    private boolean dirty;

    public void markDirty() {
        this.setDirty(true);
    }

    public WorldSavedData(String string) {
        this.mapName = string;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean bl) {
        this.dirty = bl;
    }

    public abstract void readFromNBT(NBTTagCompound var1);

    public abstract void writeToNBT(NBTTagCompound var1);
}

