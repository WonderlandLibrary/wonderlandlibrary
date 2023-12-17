// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenVillage;
import java.util.HashMap;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenStructure;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.world.World;

public class ChunkGeneratorFlat implements IChunkGenerator
{
    private final World world;
    private final Random random;
    private final IBlockState[] cachedBlockIDs;
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final Map<String, MapGenStructure> structureGenerators;
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;
    
    public ChunkGeneratorFlat(final World worldIn, final long seed, final boolean generateStructures, final String flatGeneratorSettings) {
        this.cachedBlockIDs = new IBlockState[256];
        this.structureGenerators = new HashMap<String, MapGenStructure>();
        this.world = worldIn;
        this.random = new Random(seed);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);
        if (generateStructures) {
            final Map<String, Map<String, String>> map = this.flatWorldGenInfo.getWorldFeatures();
            if (map.containsKey("village")) {
                final Map<String, String> map2 = map.get("village");
                if (!map2.containsKey("size")) {
                    map2.put("size", "1");
                }
                this.structureGenerators.put("Village", new MapGenVillage(map2));
            }
            if (map.containsKey("biome_1")) {
                this.structureGenerators.put("Temple", new MapGenScatteredFeature(map.get("biome_1")));
            }
            if (map.containsKey("mineshaft")) {
                this.structureGenerators.put("Mineshaft", new MapGenMineshaft(map.get("mineshaft")));
            }
            if (map.containsKey("stronghold")) {
                this.structureGenerators.put("Stronghold", new MapGenStronghold(map.get("stronghold")));
            }
            if (map.containsKey("oceanmonument")) {
                this.structureGenerators.put("Monument", new StructureOceanMonument(map.get("oceanmonument")));
            }
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.WATER);
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);
        }
        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        int j = 0;
        int k = 0;
        boolean flag = true;
        for (final FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers()) {
            for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++i) {
                final IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
                if (iblockstate.getBlock() != Blocks.AIR) {
                    flag = false;
                    this.cachedBlockIDs[i] = iblockstate;
                }
            }
            if (flatlayerinfo.getLayerMaterial().getBlock() == Blocks.AIR) {
                k += flatlayerinfo.getLayerCount();
            }
            else {
                j += flatlayerinfo.getLayerCount() + k;
                k = 0;
            }
        }
        worldIn.setSeaLevel(j);
        this.hasDecoration = ((!flag || this.flatWorldGenInfo.getBiome() == Biome.getIdForBiome(Biomes.VOID)) && this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration"));
    }
    
    @Override
    public Chunk generateChunk(final int x, final int z) {
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        for (int i = 0; i < this.cachedBlockIDs.length; ++i) {
            final IBlockState iblockstate = this.cachedBlockIDs[i];
            if (iblockstate != null) {
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        chunkprimer.setBlockState(j, i, k, iblockstate);
                    }
                }
            }
        }
        for (final MapGenBase mapgenbase : this.structureGenerators.values()) {
            mapgenbase.generate(this.world, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        final Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int l = 0; l < abyte.length; ++l) {
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    @Override
    public void populate(final int x, final int z) {
        final int i = x * 16;
        final int j = z * 16;
        final BlockPos blockpos = new BlockPos(i, 0, j);
        final Biome biome = this.world.getBiome(new BlockPos(i + 16, 0, j + 16));
        boolean flag = false;
        this.random.setSeed(this.world.getSeed());
        final long k = this.random.nextLong() / 2L * 2L + 1L;
        final long l = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed(x * k + z * l ^ this.world.getSeed());
        final ChunkPos chunkpos = new ChunkPos(x, z);
        for (final MapGenStructure mapgenstructure : this.structureGenerators.values()) {
            final boolean flag2 = mapgenstructure.generateStructure(this.world, this.random, chunkpos);
            if (mapgenstructure instanceof MapGenVillage) {
                flag |= flag2;
            }
        }
        if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }
        if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0) {
            final BlockPos blockpos2 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
            if (blockpos2.getY() < this.world.getSeaLevel() || this.random.nextInt(10) == 0) {
                this.lavaLakeGenerator.generate(this.world, this.random, blockpos2);
            }
        }
        if (this.hasDungeons) {
            for (int i2 = 0; i2 < 8; ++i2) {
                new WorldGenDungeons().generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }
        if (this.hasDecoration) {
            biome.decorate(this.world, this.random, blockpos);
        }
    }
    
    @Override
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        final MapGenStructure mapgenstructure = this.structureGenerators.get(structureName);
        return (mapgenstructure != null) ? mapgenstructure.getNearestStructurePos(worldIn, position, findUnexplored) : null;
    }
    
    @Override
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        final MapGenStructure mapgenstructure = this.structureGenerators.get(structureName);
        return mapgenstructure != null && mapgenstructure.isInsideStructure(pos);
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
        for (final MapGenStructure mapgenstructure : this.structureGenerators.values()) {
            mapgenstructure.generate(this.world, x, z, null);
        }
    }
}
