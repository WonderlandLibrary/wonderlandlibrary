// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk.storage;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTBase;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.nbt.NBTTagCompound;

public class ChunkLoader
{
    private static final String __OBFID = "CL_00000379";
    
    public static AnvilConverterData load(final NBTTagCompound nbt) {
        final int var1 = nbt.getInteger("xPos");
        final int var2 = nbt.getInteger("zPos");
        final AnvilConverterData var3 = new AnvilConverterData(var1, var2);
        var3.blocks = nbt.getByteArray("Blocks");
        var3.data = new NibbleArrayReader(nbt.getByteArray("Data"), 7);
        var3.skyLight = new NibbleArrayReader(nbt.getByteArray("SkyLight"), 7);
        var3.blockLight = new NibbleArrayReader(nbt.getByteArray("BlockLight"), 7);
        var3.heightmap = nbt.getByteArray("HeightMap");
        var3.terrainPopulated = nbt.getBoolean("TerrainPopulated");
        var3.entities = nbt.getTagList("Entities", 10);
        var3.tileEntities = nbt.getTagList("TileEntities", 10);
        var3.tileTicks = nbt.getTagList("TileTicks", 10);
        try {
            var3.lastUpdated = nbt.getLong("LastUpdate");
        }
        catch (ClassCastException var4) {
            var3.lastUpdated = nbt.getInteger("LastUpdate");
        }
        return var3;
    }
    
    public static void convertToAnvilFormat(final AnvilConverterData p_76690_0_, final NBTTagCompound p_76690_1_, final WorldChunkManager p_76690_2_) {
        p_76690_1_.setInteger("xPos", p_76690_0_.x);
        p_76690_1_.setInteger("zPos", p_76690_0_.z);
        p_76690_1_.setLong("LastUpdate", p_76690_0_.lastUpdated);
        final int[] var3 = new int[p_76690_0_.heightmap.length];
        for (int var4 = 0; var4 < p_76690_0_.heightmap.length; ++var4) {
            var3[var4] = p_76690_0_.heightmap[var4];
        }
        p_76690_1_.setIntArray("HeightMap", var3);
        p_76690_1_.setBoolean("TerrainPopulated", p_76690_0_.terrainPopulated);
        final NBTTagList var5 = new NBTTagList();
        for (int var6 = 0; var6 < 8; ++var6) {
            boolean var7 = true;
            for (int var8 = 0; var8 < 16 && var7; ++var8) {
                for (int var9 = 0; var9 < 16 && var7; ++var9) {
                    for (int var10 = 0; var10 < 16; ++var10) {
                        final int var11 = var8 << 11 | var10 << 7 | var9 + (var6 << 4);
                        final byte var12 = p_76690_0_.blocks[var11];
                        if (var12 != 0) {
                            var7 = false;
                            break;
                        }
                    }
                }
            }
            if (!var7) {
                final byte[] var13 = new byte[4096];
                final NibbleArray var14 = new NibbleArray();
                final NibbleArray var15 = new NibbleArray();
                final NibbleArray var16 = new NibbleArray();
                for (int var17 = 0; var17 < 16; ++var17) {
                    for (int var18 = 0; var18 < 16; ++var18) {
                        for (int var19 = 0; var19 < 16; ++var19) {
                            final int var20 = var17 << 11 | var19 << 7 | var18 + (var6 << 4);
                            final byte var21 = p_76690_0_.blocks[var20];
                            var13[var18 << 8 | var19 << 4 | var17] = (byte)(var21 & 0xFF);
                            var14.set(var17, var18, var19, p_76690_0_.data.get(var17, var18 + (var6 << 4), var19));
                            var15.set(var17, var18, var19, p_76690_0_.skyLight.get(var17, var18 + (var6 << 4), var19));
                            var16.set(var17, var18, var19, p_76690_0_.blockLight.get(var17, var18 + (var6 << 4), var19));
                        }
                    }
                }
                final NBTTagCompound var22 = new NBTTagCompound();
                var22.setByte("Y", (byte)(var6 & 0xFF));
                var22.setByteArray("Blocks", var13);
                var22.setByteArray("Data", var14.getData());
                var22.setByteArray("SkyLight", var15.getData());
                var22.setByteArray("BlockLight", var16.getData());
                var5.appendTag(var22);
            }
        }
        p_76690_1_.setTag("Sections", var5);
        final byte[] var23 = new byte[256];
        for (int var24 = 0; var24 < 16; ++var24) {
            for (int var8 = 0; var8 < 16; ++var8) {
                var23[var8 << 4 | var24] = (byte)(p_76690_2_.func_180300_a(new BlockPos(p_76690_0_.x << 4 | var24, 0, p_76690_0_.z << 4 | var8), BiomeGenBase.field_180279_ad).biomeID & 0xFF);
            }
        }
        p_76690_1_.setByteArray("Biomes", var23);
        p_76690_1_.setTag("Entities", p_76690_0_.entities);
        p_76690_1_.setTag("TileEntities", p_76690_0_.tileEntities);
        if (p_76690_0_.tileTicks != null) {
            p_76690_1_.setTag("TileTicks", p_76690_0_.tileTicks);
        }
    }
    
    public static class AnvilConverterData
    {
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
        private static final String __OBFID = "CL_00000380";
        
        public AnvilConverterData(final int p_i1999_1_, final int p_i1999_2_) {
            this.x = p_i1999_1_;
            this.z = p_i1999_2_;
        }
    }
}
