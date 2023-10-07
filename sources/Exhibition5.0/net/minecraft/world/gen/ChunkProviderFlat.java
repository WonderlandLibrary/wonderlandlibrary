// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenVillage;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.world.gen.feature.WorldGenLakes;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderFlat implements IChunkProvider
{
    private World worldObj;
    private Random random;
    private final IBlockState[] cachedBlockIDs;
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final List structureGenerators;
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;
    private static final String __OBFID = "CL_00000391";
    
    public ChunkProviderFlat(final World worldIn, final long p_i2004_2_, final boolean p_i2004_4_, final String p_i2004_5_) {
        this.cachedBlockIDs = new IBlockState[256];
        this.structureGenerators = Lists.newArrayList();
        this.worldObj = worldIn;
        this.random = new Random(p_i2004_2_);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_i2004_5_);
        if (p_i2004_4_) {
            final Map var6 = this.flatWorldGenInfo.getWorldFeatures();
            if (var6.containsKey("village")) {
                final Map var7 = var6.get("village");
                if (!var7.containsKey("size")) {
                    var7.put("size", "1");
                }
                this.structureGenerators.add(new MapGenVillage(var7));
            }
            if (var6.containsKey("biome_1")) {
                this.structureGenerators.add(new MapGenScatteredFeature(var6.get("biome_1")));
            }
            if (var6.containsKey("mineshaft")) {
                this.structureGenerators.add(new MapGenMineshaft(var6.get("mineshaft")));
            }
            if (var6.containsKey("stronghold")) {
                this.structureGenerators.add(new MapGenStronghold(var6.get("stronghold")));
            }
            if (var6.containsKey("oceanmonument")) {
                this.structureGenerators.add(new StructureOceanMonument(var6.get("oceanmonument")));
            }
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
        }
        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        boolean var8 = true;
        for (final FlatLayerInfo var10 : this.flatWorldGenInfo.getFlatLayers()) {
            for (int var11 = var10.getMinY(); var11 < var10.getMinY() + var10.getLayerCount(); ++var11) {
                final IBlockState var12 = var10.func_175900_c();
                if (var12.getBlock() != Blocks.air) {
                    var8 = false;
                    this.cachedBlockIDs[var11] = var12;
                }
            }
        }
        this.hasDecoration = (!var8 && this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration"));
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        final ChunkPrimer var3 = new ChunkPrimer();
        for (int var4 = 0; var4 < this.cachedBlockIDs.length; ++var4) {
            final IBlockState var5 = this.cachedBlockIDs[var4];
            if (var5 != null) {
                for (int var6 = 0; var6 < 16; ++var6) {
                    for (int var7 = 0; var7 < 16; ++var7) {
                        var3.setBlockState(var6, var4, var7, var5);
                    }
                }
            }
        }
        for (final MapGenBase var9 : this.structureGenerators) {
            var9.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        final Chunk var10 = new Chunk(this.worldObj, var3, p_73154_1_, p_73154_2_);
        final BiomeGenBase[] var11 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        final byte[] var12 = var10.getBiomeArray();
        for (int var7 = 0; var7 < var12.length; ++var7) {
            var12[var7] = (byte)var11[var7].biomeID;
        }
        var10.generateSkylightMap();
        return var10;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        final int var4 = p_73153_2_ * 16;
        final int var5 = p_73153_3_ * 16;
        final BlockPos var6 = new BlockPos(var4, 0, var5);
        final BiomeGenBase var7 = this.worldObj.getBiomeGenForCoords(new BlockPos(var4 + 16, 0, var5 + 16));
        boolean var8 = false;
        this.random.setSeed(this.worldObj.getSeed());
        final long var9 = this.random.nextLong() / 2L * 2L + 1L;
        final long var10 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed(p_73153_2_ * var9 + p_73153_3_ * var10 ^ this.worldObj.getSeed());
        final ChunkCoordIntPair var11 = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        for (final MapGenStructure var13 : this.structureGenerators) {
            final boolean var14 = var13.func_175794_a(this.worldObj, this.random, var11);
            if (var13 instanceof MapGenVillage) {
                var8 |= var14;
            }
        }
        if (this.waterLakeGenerator != null && !var8 && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.worldObj, this.random, var6.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }
        if (this.lavaLakeGenerator != null && !var8 && this.random.nextInt(8) == 0) {
            final BlockPos var15 = var6.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
            if (var15.getY() < 63 || this.random.nextInt(10) == 0) {
                this.lavaLakeGenerator.generate(this.worldObj, this.random, var15);
            }
        }
        if (this.hasDungeons) {
            for (int var16 = 0; var16 < 8; ++var16) {
                new WorldGenDungeons().generate(this.worldObj, this.random, var6.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }
        if (this.hasDecoration) {
            var7.func_180624_a(this.worldObj, this.random, new BlockPos(var4, 0, var5));
        }
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "FlatLevelSource";
    }
    
    @Override
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        final BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        return var3.getSpawnableList(p_177458_1_);
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        if ("Stronghold".equals(p_180513_2_)) {
            for (final MapGenStructure var5 : this.structureGenerators) {
                if (var5 instanceof MapGenStronghold) {
                    return var5.func_180706_b(worldIn, p_180513_3_);
                }
            }
        }
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void func_180514_a(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
        for (final MapGenStructure var5 : this.structureGenerators) {
            var5.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
