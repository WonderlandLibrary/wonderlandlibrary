// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData
{
    public final String mapName;
    private boolean dirty;
    
    public WorldSavedData(final String name) {
        this.mapName = name;
    }
    
    public abstract void readFromNBT(final NBTTagCompound p0);
    
    public abstract NBTTagCompound writeToNBT(final NBTTagCompound p0);
    
    public void markDirty() {
        this.setDirty(true);
    }
    
    public void setDirty(final boolean isDirty) {
        this.dirty = isDirty;
    }
    
    public boolean isDirty() {
        return this.dirty;
    }
}
