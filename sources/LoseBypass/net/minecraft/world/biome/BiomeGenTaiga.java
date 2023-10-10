/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenTaiga
extends BiomeGenBase {
    private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
    private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
    private int field_150644_aH;

    public BiomeGenTaiga(int p_i45385_1_, int p_i45385_2_) {
        super(p_i45385_1_);
        this.field_150644_aH = p_i45385_2_;
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 10;
        if (p_i45385_2_ != 1 && p_i45385_2_ != 2) {
            this.theBiomeDecorator.grassPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 1;
            return;
        }
        this.theBiomeDecorator.grassPerChunk = 7;
        this.theBiomeDecorator.deadBushPerChunk = 1;
        this.theBiomeDecorator.mushroomsPerChunk = 3;
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random rand) {
        WorldGenAbstractTree worldGenAbstractTree;
        if ((this.field_150644_aH == 1 || this.field_150644_aH == 2) && rand.nextInt(3) == 0) {
            if (this.field_150644_aH != 2 && rand.nextInt(13) != 0) {
                worldGenAbstractTree = field_150641_aE;
                return worldGenAbstractTree;
            }
            worldGenAbstractTree = field_150642_aF;
            return worldGenAbstractTree;
        }
        if (rand.nextInt(3) == 0) {
            worldGenAbstractTree = field_150639_aC;
            return worldGenAbstractTree;
        }
        worldGenAbstractTree = field_150640_aD;
        return worldGenAbstractTree;
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        WorldGenTallGrass worldGenTallGrass;
        if (rand.nextInt(5) > 0) {
            worldGenTallGrass = new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
            return worldGenTallGrass;
        }
        worldGenTallGrass = new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
        return worldGenTallGrass;
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            int i = rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                field_150643_aG.generate(worldIn, rand, blockpos);
            }
        }
        DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
        int i1 = 0;
        while (true) {
            if (i1 >= 7) {
                super.decorate(worldIn, rand, pos);
                return;
            }
            int j1 = rand.nextInt(16) + 8;
            int k1 = rand.nextInt(16) + 8;
            int l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
            DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
            ++i1;
        }
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_) {
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();
            if (p_180622_6_ > 1.75) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            } else if (p_180622_6_ > -0.95) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
        BiomeGenBase biomeGenBase;
        if (this.biomeID == BiomeGenBase.megaTaiga.biomeID) {
            biomeGenBase = new BiomeGenTaiga(p_180277_1_, 2).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight));
            return biomeGenBase;
        }
        biomeGenBase = super.createMutatedBiome(p_180277_1_);
        return biomeGenBase;
    }
}

