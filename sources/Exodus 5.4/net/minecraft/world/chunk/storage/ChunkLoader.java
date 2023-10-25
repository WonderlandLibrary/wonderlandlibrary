/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.NibbleArrayReader;

public class ChunkLoader {
    public static void convertToAnvilFormat(AnvilConverterData anvilConverterData, NBTTagCompound nBTTagCompound, WorldChunkManager worldChunkManager) {
        nBTTagCompound.setInteger("xPos", anvilConverterData.x);
        nBTTagCompound.setInteger("zPos", anvilConverterData.z);
        nBTTagCompound.setLong("LastUpdate", anvilConverterData.lastUpdated);
        int[] nArray = new int[anvilConverterData.heightmap.length];
        int n = 0;
        while (n < anvilConverterData.heightmap.length) {
            nArray[n] = anvilConverterData.heightmap[n];
            ++n;
        }
        nBTTagCompound.setIntArray("HeightMap", nArray);
        nBTTagCompound.setBoolean("TerrainPopulated", anvilConverterData.terrainPopulated);
        NBTTagList nBTTagList = new NBTTagList();
        int n2 = 0;
        while (n2 < 8) {
            int n3;
            boolean bl = true;
            int n4 = 0;
            while (n4 < 16 && bl) {
                int n5 = 0;
                while (n5 < 16 && bl) {
                    int n6 = 0;
                    while (n6 < 16) {
                        int n7 = n4 << 11 | n6 << 7 | n5 + (n2 << 4);
                        n3 = anvilConverterData.blocks[n7];
                        if (n3 != 0) {
                            bl = false;
                            break;
                        }
                        ++n6;
                    }
                    ++n5;
                }
                ++n4;
            }
            if (!bl) {
                byte[] byArray = new byte[4096];
                NibbleArray nibbleArray = new NibbleArray();
                NibbleArray nibbleArray2 = new NibbleArray();
                NibbleArray nibbleArray3 = new NibbleArray();
                n3 = 0;
                while (n3 < 16) {
                    int n8 = 0;
                    while (n8 < 16) {
                        int n9 = 0;
                        while (n9 < 16) {
                            int n10 = n3 << 11 | n9 << 7 | n8 + (n2 << 4);
                            byte by = anvilConverterData.blocks[n10];
                            byArray[n8 << 8 | n9 << 4 | n3] = (byte)(by & 0xFF);
                            nibbleArray.set(n3, n8, n9, anvilConverterData.data.get(n3, n8 + (n2 << 4), n9));
                            nibbleArray2.set(n3, n8, n9, anvilConverterData.skyLight.get(n3, n8 + (n2 << 4), n9));
                            nibbleArray3.set(n3, n8, n9, anvilConverterData.blockLight.get(n3, n8 + (n2 << 4), n9));
                            ++n9;
                        }
                        ++n8;
                    }
                    ++n3;
                }
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Y", (byte)(n2 & 0xFF));
                nBTTagCompound2.setByteArray("Blocks", byArray);
                nBTTagCompound2.setByteArray("Data", nibbleArray.getData());
                nBTTagCompound2.setByteArray("SkyLight", nibbleArray2.getData());
                nBTTagCompound2.setByteArray("BlockLight", nibbleArray3.getData());
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n2;
        }
        nBTTagCompound.setTag("Sections", nBTTagList);
        byte[] byArray = new byte[256];
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n11 = 0;
        while (n11 < 16) {
            int n12 = 0;
            while (n12 < 16) {
                mutableBlockPos.func_181079_c(anvilConverterData.x << 4 | n11, 0, anvilConverterData.z << 4 | n12);
                byArray[n12 << 4 | n11] = (byte)(worldChunkManager.getBiomeGenerator((BlockPos)mutableBlockPos, (BiomeGenBase)BiomeGenBase.field_180279_ad).biomeID & 0xFF);
                ++n12;
            }
            ++n11;
        }
        nBTTagCompound.setByteArray("Biomes", byArray);
        nBTTagCompound.setTag("Entities", anvilConverterData.entities);
        nBTTagCompound.setTag("TileEntities", anvilConverterData.tileEntities);
        if (anvilConverterData.tileTicks != null) {
            nBTTagCompound.setTag("TileTicks", anvilConverterData.tileTicks);
        }
    }

    public static AnvilConverterData load(NBTTagCompound nBTTagCompound) {
        int n = nBTTagCompound.getInteger("xPos");
        int n2 = nBTTagCompound.getInteger("zPos");
        AnvilConverterData anvilConverterData = new AnvilConverterData(n, n2);
        anvilConverterData.blocks = nBTTagCompound.getByteArray("Blocks");
        anvilConverterData.data = new NibbleArrayReader(nBTTagCompound.getByteArray("Data"), 7);
        anvilConverterData.skyLight = new NibbleArrayReader(nBTTagCompound.getByteArray("SkyLight"), 7);
        anvilConverterData.blockLight = new NibbleArrayReader(nBTTagCompound.getByteArray("BlockLight"), 7);
        anvilConverterData.heightmap = nBTTagCompound.getByteArray("HeightMap");
        anvilConverterData.terrainPopulated = nBTTagCompound.getBoolean("TerrainPopulated");
        anvilConverterData.entities = nBTTagCompound.getTagList("Entities", 10);
        anvilConverterData.tileEntities = nBTTagCompound.getTagList("TileEntities", 10);
        anvilConverterData.tileTicks = nBTTagCompound.getTagList("TileTicks", 10);
        try {
            anvilConverterData.lastUpdated = nBTTagCompound.getLong("LastUpdate");
        }
        catch (ClassCastException classCastException) {
            anvilConverterData.lastUpdated = nBTTagCompound.getInteger("LastUpdate");
        }
        return anvilConverterData;
    }

    public static class AnvilConverterData {
        public byte[] blocks;
        public final int z;
        public NibbleArrayReader data;
        public NibbleArrayReader blockLight;
        public byte[] heightmap;
        public NBTTagList tileEntities;
        public NBTTagList entities;
        public boolean terrainPopulated;
        public final int x;
        public NibbleArrayReader skyLight;
        public long lastUpdated;
        public NBTTagList tileTicks;

        public AnvilConverterData(int n, int n2) {
            this.x = n;
            this.z = n2;
        }
    }
}

