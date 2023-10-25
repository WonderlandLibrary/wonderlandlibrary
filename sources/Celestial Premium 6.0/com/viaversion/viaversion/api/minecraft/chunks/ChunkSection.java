/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLight;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ChunkSection {
    public static final int SIZE = 4096;

    public static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public int getFlatBlock(int var1);

    default public int getFlatBlock(int x, int y, int z) {
        return this.getFlatBlock(ChunkSection.index(x, y, z));
    }

    public void setFlatBlock(int var1, int var2);

    default public void setFlatBlock(int x, int y, int z, int id) {
        this.setFlatBlock(ChunkSection.index(x, y, z), id);
    }

    default public int getBlockWithoutData(int x, int y, int z) {
        return this.getFlatBlock(x, y, z) >> 4;
    }

    default public int getBlockData(int x, int y, int z) {
        return this.getFlatBlock(x, y, z) & 0xF;
    }

    default public void setBlockWithData(int x, int y, int z, int type, int data) {
        this.setFlatBlock(ChunkSection.index(x, y, z), type << 4 | data & 0xF);
    }

    default public void setBlockWithData(int idx, int type, int data) {
        this.setFlatBlock(idx, type << 4 | data & 0xF);
    }

    public void setPaletteIndex(int var1, int var2);

    public int getPaletteIndex(int var1);

    public int getPaletteSize();

    public int getPaletteEntry(int var1);

    public void setPaletteEntry(int var1, int var2);

    public void replacePaletteEntry(int var1, int var2);

    public void addPaletteEntry(int var1);

    public void clearPalette();

    public int getNonAirBlocksCount();

    public void setNonAirBlocksCount(int var1);

    default public boolean hasLight() {
        return this.getLight() != null;
    }

    public @Nullable ChunkSectionLight getLight();

    public void setLight(@Nullable ChunkSectionLight var1);
}

