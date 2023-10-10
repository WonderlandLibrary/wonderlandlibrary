// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import java.util.EnumMap;

public class ChunkSectionImpl implements ChunkSection
{
    private final EnumMap<PaletteType, DataPalette> palettes;
    private ChunkSectionLight light;
    private int nonAirBlocksCount;
    
    public ChunkSectionImpl() {
        this.palettes = new EnumMap<PaletteType, DataPalette>(PaletteType.class);
    }
    
    public ChunkSectionImpl(final boolean holdsLight) {
        this.palettes = new EnumMap<PaletteType, DataPalette>(PaletteType.class);
        this.addPalette(PaletteType.BLOCKS, new DataPaletteImpl(4096));
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
    }
    
    public ChunkSectionImpl(final boolean holdsLight, final int expectedPaletteLength) {
        this.palettes = new EnumMap<PaletteType, DataPalette>(PaletteType.class);
        this.addPalette(PaletteType.BLOCKS, new DataPaletteImpl(4096, expectedPaletteLength));
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
    }
    
    @Override
    public int getNonAirBlocksCount() {
        return this.nonAirBlocksCount;
    }
    
    @Override
    public void setNonAirBlocksCount(final int nonAirBlocksCount) {
        this.nonAirBlocksCount = nonAirBlocksCount;
    }
    
    @Override
    public ChunkSectionLight getLight() {
        return this.light;
    }
    
    @Override
    public void setLight(final ChunkSectionLight light) {
        this.light = light;
    }
    
    @Override
    public DataPalette palette(final PaletteType type) {
        return this.palettes.get(type);
    }
    
    @Override
    public void addPalette(final PaletteType type, final DataPalette palette) {
        this.palettes.put(type, palette);
    }
    
    @Override
    public void removePalette(final PaletteType type) {
        this.palettes.remove(type);
    }
}
