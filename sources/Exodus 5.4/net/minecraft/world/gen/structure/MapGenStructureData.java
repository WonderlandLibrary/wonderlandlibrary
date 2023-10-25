/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapGenStructureData
extends WorldSavedData {
    private NBTTagCompound tagCompound = new NBTTagCompound();

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.tagCompound = nBTTagCompound.getCompoundTag("Features");
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setTag("Features", this.tagCompound);
    }

    public static String formatChunkCoords(int n, int n2) {
        return "[" + n + "," + n2 + "]";
    }

    public MapGenStructureData(String string) {
        super(string);
    }

    public void writeInstance(NBTTagCompound nBTTagCompound, int n, int n2) {
        this.tagCompound.setTag(MapGenStructureData.formatChunkCoords(n, n2), nBTTagCompound);
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }
}

