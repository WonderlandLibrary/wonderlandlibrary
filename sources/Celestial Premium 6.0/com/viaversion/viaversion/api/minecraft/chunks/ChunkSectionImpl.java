/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLight;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLightImpl;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChunkSectionImpl
implements ChunkSection {
    private final IntList palette;
    private final Int2IntMap inversePalette;
    private final int[] blocks = new int[4096];
    private ChunkSectionLight light;
    private int nonAirBlocksCount;

    public ChunkSectionImpl(boolean holdsLight) {
        this.palette = new IntArrayList();
        this.inversePalette = new Int2IntOpenHashMap();
        this.inversePalette.defaultReturnValue(-1);
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
    }

    public ChunkSectionImpl(boolean holdsLight, int expectedPaletteLength) {
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
        this.palette = new IntArrayList(expectedPaletteLength);
        this.inversePalette = new Int2IntOpenHashMap(expectedPaletteLength);
        this.inversePalette.defaultReturnValue(-1);
    }

    @Override
    public int getFlatBlock(int idx) {
        int index = this.blocks[idx];
        return this.palette.getInt(index);
    }

    @Override
    public void setFlatBlock(int idx, int id) {
        int index = this.inversePalette.get(id);
        if (index == -1) {
            index = this.palette.size();
            this.palette.add(id);
            this.inversePalette.put(id, index);
        }
        this.blocks[idx] = index;
    }

    @Override
    public int getPaletteIndex(int idx) {
        return this.blocks[idx];
    }

    @Override
    public void setPaletteIndex(int idx, int index) {
        this.blocks[idx] = index;
    }

    @Override
    public int getPaletteSize() {
        return this.palette.size();
    }

    @Override
    public int getPaletteEntry(int index) {
        return this.palette.getInt(index);
    }

    @Override
    public void setPaletteEntry(int index, int id) {
        int oldId = this.palette.set(index, id);
        if (oldId == id) {
            return;
        }
        this.inversePalette.put(id, index);
        if (this.inversePalette.get(oldId) == index) {
            this.inversePalette.remove(oldId);
            for (int i = 0; i < this.palette.size(); ++i) {
                if (this.palette.getInt(i) != oldId) continue;
                this.inversePalette.put(oldId, i);
                break;
            }
        }
    }

    @Override
    public void replacePaletteEntry(int oldId, int newId) {
        int index = this.inversePalette.remove(oldId);
        if (index == -1) {
            return;
        }
        this.inversePalette.put(newId, index);
        for (int i = 0; i < this.palette.size(); ++i) {
            if (this.palette.getInt(i) != oldId) continue;
            this.palette.set(i, newId);
        }
    }

    @Override
    public void addPaletteEntry(int id) {
        this.inversePalette.put(id, this.palette.size());
        this.palette.add(id);
    }

    @Override
    public void clearPalette() {
        this.palette.clear();
        this.inversePalette.clear();
    }

    @Override
    public int getNonAirBlocksCount() {
        return this.nonAirBlocksCount;
    }

    @Override
    public void setNonAirBlocksCount(int nonAirBlocksCount) {
        this.nonAirBlocksCount = nonAirBlocksCount;
    }

    @Override
    public @Nullable ChunkSectionLight getLight() {
        return this.light;
    }

    @Override
    public void setLight(@Nullable ChunkSectionLight light) {
        this.light = light;
    }
}

