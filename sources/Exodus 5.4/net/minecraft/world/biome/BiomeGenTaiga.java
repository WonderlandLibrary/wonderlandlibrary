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
    private static final WorldGenTaiga2 field_150640_aD;
    private static final WorldGenTaiga1 field_150639_aC;
    private static final WorldGenBlockBlob field_150643_aG;
    private static final WorldGenMegaPineTree field_150642_aF;
    private int field_150644_aH;
    private static final WorldGenMegaPineTree field_150641_aE;

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            n4 = random.nextInt(3);
            n3 = 0;
            while (n3 < n4) {
                n2 = random.nextInt(16) + 8;
                n = random.nextInt(16) + 8;
                BlockPos blockPos2 = world.getHeight(blockPos.add(n2, 0, n));
                field_150643_aG.generate(world, random, blockPos2);
                ++n3;
            }
        }
        DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
        n4 = 0;
        while (n4 < 7) {
            n3 = random.nextInt(16) + 8;
            n2 = random.nextInt(16) + 8;
            n = random.nextInt(world.getHeight(blockPos.add(n3, 0, n2)).getY() + 32);
            DOUBLE_PLANT_GENERATOR.generate(world, random, blockPos.add(n3, n, n2));
            ++n4;
        }
        super.decorate(world, random, blockPos);
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();
            if (d > 1.75) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            } else if (d > -0.95) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }
        this.generateBiomeTerrain(world, random, chunkPrimer, n, n2, d);
    }

    static {
        field_150639_aC = new WorldGenTaiga1();
        field_150640_aD = new WorldGenTaiga2(false);
        field_150641_aE = new WorldGenMegaPineTree(false, false);
        field_150642_aF = new WorldGenMegaPineTree(false, true);
        field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return (this.field_150644_aH == 1 || this.field_150644_aH == 2) && random.nextInt(3) == 0 ? (this.field_150644_aH != 2 && random.nextInt(13) != 0 ? field_150641_aE : field_150642_aF) : (random.nextInt(3) == 0 ? field_150639_aC : field_150640_aD);
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        return this.biomeID == BiomeGenBase.megaTaiga.biomeID ? new BiomeGenTaiga(n, 2).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight)) : super.createMutatedBiome(n);
    }

    public BiomeGenTaiga(int n, int n2) {
        super(n);
        this.field_150644_aH = n2;
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 10;
        if (n2 != 1 && n2 != 2) {
            this.theBiomeDecorator.grassPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 1;
        } else {
            this.theBiomeDecorator.grassPerChunk = 7;
            this.theBiomeDecorator.deadBushPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 3;
        }
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
}

