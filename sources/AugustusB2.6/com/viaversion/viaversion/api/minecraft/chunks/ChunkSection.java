// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

public interface ChunkSection
{
    public static final int SIZE = 4096;
    public static final int BIOME_SIZE = 64;
    
    default int index(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    @Deprecated
    default int getFlatBlock(final int idx) {
        return this.palette(PaletteType.BLOCKS).idAt(idx);
    }
    
    @Deprecated
    default int getFlatBlock(final int x, final int y, final int z) {
        return this.getFlatBlock(index(x, y, z));
    }
    
    @Deprecated
    default void setFlatBlock(final int idx, final int id) {
        this.palette(PaletteType.BLOCKS).setIdAt(idx, id);
    }
    
    @Deprecated
    default void setFlatBlock(final int x, final int y, final int z, final int id) {
        this.setFlatBlock(index(x, y, z), id);
    }
    
    @Deprecated
    default int getBlockWithoutData(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) >> 4;
    }
    
    @Deprecated
    default int getBlockData(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) & 0xF;
    }
    
    @Deprecated
    default void setBlockWithData(final int x, final int y, final int z, final int type, final int data) {
        this.setFlatBlock(index(x, y, z), type << 4 | (data & 0xF));
    }
    
    @Deprecated
    default void setBlockWithData(final int idx, final int type, final int data) {
        this.setFlatBlock(idx, type << 4 | (data & 0xF));
    }
    
    @Deprecated
    default void setPaletteIndex(final int idx, final int index) {
        this.palette(PaletteType.BLOCKS).setPaletteIndexAt(idx, index);
    }
    
    @Deprecated
    default int getPaletteIndex(final int idx) {
        return this.palette(PaletteType.BLOCKS).paletteIndexAt(idx);
    }
    
    @Deprecated
    default int getPaletteSize() {
        return this.palette(PaletteType.BLOCKS).size();
    }
    
    @Deprecated
    default int getPaletteEntry(final int index) {
        return this.palette(PaletteType.BLOCKS).idByIndex(index);
    }
    
    @Deprecated
    default void setPaletteEntry(final int index, final int id) {
        this.palette(PaletteType.BLOCKS).setIdByIndex(index, id);
    }
    
    @Deprecated
    default void replacePaletteEntry(final int oldId, final int newId) {
        this.palette(PaletteType.BLOCKS).replaceId(oldId, newId);
    }
    
    @Deprecated
    default void addPaletteEntry(final int id) {
        this.palette(PaletteType.BLOCKS).addId(id);
    }
    
    @Deprecated
    default void clearPalette() {
        this.palette(PaletteType.BLOCKS).clear();
    }
    
    int getNonAirBlocksCount();
    
    void setNonAirBlocksCount(final int p0);
    
    default boolean hasLight() {
        return this.getLight() != null;
    }
    
    ChunkSectionLight getLight();
    
    void setLight(final ChunkSectionLight p0);
    
    DataPalette palette(final PaletteType p0);
    
    void addPalette(final PaletteType p0, final DataPalette p1);
    
    void removePalette(final PaletteType p0);
}
