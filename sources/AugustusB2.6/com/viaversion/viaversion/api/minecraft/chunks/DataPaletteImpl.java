// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;

public final class DataPaletteImpl implements DataPalette
{
    private final IntList palette;
    private final Int2IntMap inversePalette;
    private final int[] values;
    private final int sizeBits;
    
    public DataPaletteImpl(final int valuesLength) {
        this(valuesLength, 8);
    }
    
    public DataPaletteImpl(final int valuesLength, final int expectedPaletteLength) {
        this.values = new int[valuesLength];
        this.sizeBits = Integer.numberOfTrailingZeros(valuesLength) / 3;
        this.palette = new IntArrayList(expectedPaletteLength);
        (this.inversePalette = new Int2IntOpenHashMap(expectedPaletteLength)).defaultReturnValue(-1);
    }
    
    @Override
    public int index(final int x, final int y, final int z) {
        return (y << this.sizeBits | z) << this.sizeBits | x;
    }
    
    @Override
    public int idAt(final int sectionCoordinate) {
        final int index = this.values[sectionCoordinate];
        return this.palette.getInt(index);
    }
    
    @Override
    public void setIdAt(final int sectionCoordinate, final int id) {
        int index = this.inversePalette.get(id);
        if (index == -1) {
            index = this.palette.size();
            this.palette.add(id);
            this.inversePalette.put(id, index);
        }
        this.values[sectionCoordinate] = index;
    }
    
    @Override
    public int paletteIndexAt(final int packedCoordinate) {
        return this.values[packedCoordinate];
    }
    
    @Override
    public void setPaletteIndexAt(final int sectionCoordinate, final int index) {
        this.values[sectionCoordinate] = index;
    }
    
    @Override
    public int size() {
        return this.palette.size();
    }
    
    @Override
    public int idByIndex(final int index) {
        return this.palette.getInt(index);
    }
    
    @Override
    public void setIdByIndex(final int index, final int id) {
        final int oldId = this.palette.set(index, id);
        if (oldId == id) {
            return;
        }
        this.inversePalette.put(id, index);
        if (this.inversePalette.get(oldId) == index) {
            this.inversePalette.remove(oldId);
            for (int i = 0; i < this.palette.size(); ++i) {
                if (this.palette.getInt(i) == oldId) {
                    this.inversePalette.put(oldId, i);
                    break;
                }
            }
        }
    }
    
    @Override
    public void replaceId(final int oldId, final int newId) {
        final int index = this.inversePalette.remove(oldId);
        if (index == -1) {
            return;
        }
        this.inversePalette.put(newId, index);
        for (int i = 0; i < this.palette.size(); ++i) {
            if (this.palette.getInt(i) == oldId) {
                this.palette.set(i, newId);
            }
        }
    }
    
    @Override
    public void addId(final int id) {
        this.inversePalette.put(id, this.palette.size());
        this.palette.add(id);
    }
    
    @Override
    public void clear() {
        this.palette.clear();
        this.inversePalette.clear();
    }
}
