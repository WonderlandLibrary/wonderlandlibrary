/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkProviderFlat
implements IChunkProvider {
    private final boolean hasDungeons;
    private Random random;
    private final IBlockState[] cachedBlockIDs = new IBlockState[256];
    private World worldObj;
    private final FlatGeneratorInfo flatWorldGenInfo;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;
    private final List<MapGenStructure> structureGenerators = Lists.newArrayList();
    private final boolean hasDecoration;

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        BlockPos n5;
        int n3 = n * 16;
        int n4 = n2 * 16;
        BlockPos blockPos = new BlockPos(n3, 0, n4);
        BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(new BlockPos(n3 + 16, 0, n4 + 16));
        boolean bl = false;
        this.random.setSeed(this.worldObj.getSeed());
        long l = this.random.nextLong() / 2L * 2L + 1L;
        long l2 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)n * l + (long)n2 * l2 ^ this.worldObj.getSeed());
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n, n2);
        for (MapGenStructure object2 : this.structureGenerators) {
            boolean bl2 = object2.generateStructure(this.worldObj, this.random, chunkCoordIntPair);
            if (!(object2 instanceof MapGenVillage)) continue;
            bl |= bl2;
        }
        if (this.waterLakeGenerator != null && !bl && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.worldObj, this.random, blockPos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }
        if (!(this.lavaLakeGenerator == null || bl || this.random.nextInt(8) != 0 || (n5 = blockPos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8)).getY() >= this.worldObj.func_181545_F() && this.random.nextInt(10) != 0)) {
            this.lavaLakeGenerator.generate(this.worldObj, this.random, n5);
        }
        if (this.hasDungeons) {
            void var14_17;
            boolean bl3 = false;
            while (var14_17 < 8) {
                new WorldGenDungeons().generate(this.worldObj, this.random, blockPos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
                ++var14_17;
            }
        }
        if (this.hasDecoration) {
            biomeGenBase.decorate(this.worldObj, this.random, blockPos);
        }
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        if ("Stronghold".equals(string)) {
            for (MapGenStructure mapGenStructure : this.structureGenerators) {
                if (!(mapGenStructure instanceof MapGenStronghold)) continue;
                return mapGenStructure.getClosestStrongholdPos(world, blockPos);
            }
        }
        return null;
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        return false;
    }

    public ChunkProviderFlat(World world, long l, boolean bl, String string) {
        this.worldObj = world;
        this.random = new Random(l);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(string);
        if (bl) {
            Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();
            if (map.containsKey("village")) {
                Map<String, String> map2 = map.get("village");
                if (!map2.containsKey("size")) {
                    map2.put("size", "1");
                }
                this.structureGenerators.add(new MapGenVillage(map2));
            }
            if (map.containsKey("biome_1")) {
                this.structureGenerators.add(new MapGenScatteredFeature(map.get("biome_1")));
            }
            if (map.containsKey("mineshaft")) {
                this.structureGenerators.add(new MapGenMineshaft(map.get("mineshaft")));
            }
            if (map.containsKey("stronghold")) {
                this.structureGenerators.add(new MapGenStronghold(map.get("stronghold")));
            }
            if (map.containsKey("oceanmonument")) {
                this.structureGenerators.add(new StructureOceanMonument(map.get("oceanmonument")));
            }
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
        }
        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        int n = 0;
        int n2 = 0;
        boolean bl2 = true;
        for (FlatLayerInfo flatLayerInfo : this.flatWorldGenInfo.getFlatLayers()) {
            int n3 = flatLayerInfo.getMinY();
            while (n3 < flatLayerInfo.getMinY() + flatLayerInfo.getLayerCount()) {
                IBlockState iBlockState = flatLayerInfo.func_175900_c();
                if (iBlockState.getBlock() != Blocks.air) {
                    bl2 = false;
                    this.cachedBlockIDs[n3] = iBlockState;
                }
                ++n3;
            }
            if (flatLayerInfo.func_175900_c().getBlock() == Blocks.air) {
                n2 += flatLayerInfo.getLayerCount();
                continue;
            }
            n += flatLayerInfo.getLayerCount() + n2;
            n2 = 0;
        }
        world.func_181544_b(n);
        this.hasDecoration = bl2 ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
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
    public void saveExtraData() {
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return true;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
        for (MapGenStructure mapGenStructure : this.structureGenerators) {
            mapGenStructure.generate(this, this.worldObj, n, n2, null);
        }
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public Chunk provideChunk(int n, int n2) {
        int n3;
        Object object;
        void mapGenBase;
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        boolean n4 = false;
        while (mapGenBase < this.cachedBlockIDs.length) {
            object = this.cachedBlockIDs[mapGenBase];
            if (object != null) {
                int byArray = 0;
                while (byArray < 16) {
                    n3 = 0;
                    while (n3 < 16) {
                        chunkPrimer.setBlockState(byArray, (int)mapGenBase, n3, (IBlockState)object);
                        ++n3;
                    }
                    ++byArray;
                }
            }
            ++mapGenBase;
        }
        for (MapGenBase chunk : this.structureGenerators) {
            chunk.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        Chunk chunk = new Chunk(this.worldObj, chunkPrimer, n, n2);
        object = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, n * 16, n2 * 16, 16, 16);
        byte[] byArray = chunk.getBiomeArray();
        n3 = 0;
        while (n3 < byArray.length) {
            byArray[n3] = (byte)object[n3].biomeID;
            ++n3;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(blockPos);
        return biomeGenBase.getSpawnableList(enumCreatureType);
    }
}

