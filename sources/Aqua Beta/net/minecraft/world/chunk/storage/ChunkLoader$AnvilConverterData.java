package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.storage.NibbleArrayReader;

public static class ChunkLoader.AnvilConverterData {
    public long lastUpdated;
    public boolean terrainPopulated;
    public byte[] heightmap;
    public NibbleArrayReader blockLight;
    public NibbleArrayReader skyLight;
    public NibbleArrayReader data;
    public byte[] blocks;
    public NBTTagList entities;
    public NBTTagList tileEntities;
    public NBTTagList tileTicks;
    public final int x;
    public final int z;

    public ChunkLoader.AnvilConverterData(int xIn, int zIn) {
        this.x = xIn;
        this.z = zIn;
    }
}
