/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class MapGenStructureData
extends WorldSavedData {
    private NBTTagCompound tagCompound = new NBTTagCompound();

    public MapGenStructureData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.tagCompound = nbt.getCompoundTag("Features");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("Features", this.tagCompound);
        return compound;
    }

    public void writeInstance(NBTTagCompound tagCompoundIn, int chunkX, int chunkZ) {
        this.tagCompound.setTag(MapGenStructureData.formatChunkCoords(chunkX, chunkZ), tagCompoundIn);
    }

    public static String formatChunkCoords(int chunkX, int chunkZ) {
        return "[" + chunkX + "," + chunkZ + "]";
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }
}

