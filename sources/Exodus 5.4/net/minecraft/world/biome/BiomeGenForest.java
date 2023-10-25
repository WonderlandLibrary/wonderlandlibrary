/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenForest
extends BiomeGenBase {
    private int field_150632_aF;
    protected static final WorldGenForest field_150629_aC = new WorldGenForest(false, true);
    protected static final WorldGenCanopyTree field_150631_aE;
    protected static final WorldGenForest field_150630_aD;

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.field_150632_aF == 3) {
            n4 = 0;
            while (n4 < 4) {
                n3 = 0;
                while (n3 < 4) {
                    WorldGenerator worldGenerator;
                    n2 = n4 * 4 + 1 + 8 + random.nextInt(3);
                    n = n3 * 4 + 1 + 8 + random.nextInt(3);
                    BlockPos blockPos2 = world.getHeight(blockPos.add(n2, 0, n));
                    if (random.nextInt(20) == 0) {
                        worldGenerator = new WorldGenBigMushroom();
                        ((WorldGenBigMushroom)worldGenerator).generate(world, random, blockPos2);
                    } else {
                        worldGenerator = this.genBigTreeChance(random);
                        worldGenerator.func_175904_e();
                        if (worldGenerator.generate(world, random, blockPos2)) {
                            ((WorldGenAbstractTree)worldGenerator).func_180711_a(world, random, blockPos2);
                        }
                    }
                    ++n3;
                }
                ++n4;
            }
        }
        n4 = random.nextInt(5) - 3;
        if (this.field_150632_aF == 1) {
            n4 += 2;
        }
        n3 = 0;
        while (n3 < n4) {
            n2 = random.nextInt(3);
            if (n2 == 0) {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
            } else if (n2 == 1) {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
            } else if (n2 == 2) {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
            }
            n = 0;
            while (n < 5) {
                int n5 = random.nextInt(16) + 8;
                int n6 = random.nextInt(16) + 8;
                int n7 = random.nextInt(world.getHeight(blockPos.add(n5, 0, n6)).getY() + 32);
                if (DOUBLE_PLANT_GENERATOR.generate(world, random, new BlockPos(blockPos.getX() + n5, n7, blockPos.getZ() + n6))) break;
                ++n;
            }
            ++n3;
        }
        super.decorate(world, random, blockPos);
    }

    @Override
    protected BiomeGenBase func_150557_a(int n, boolean bl) {
        if (this.field_150632_aF == 2) {
            this.field_150609_ah = 353825;
            this.color = n;
            if (bl) {
                this.field_150609_ah = (this.field_150609_ah & 0xFEFEFE) >> 1;
            }
            return this;
        }
        return super.func_150557_a(n, bl);
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        if (this.biomeID == BiomeGenBase.forest.biomeID) {
            BiomeGenForest biomeGenForest = new BiomeGenForest(n, 1);
            biomeGenForest.setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight + 0.2f));
            biomeGenForest.setBiomeName("Flower Forest");
            biomeGenForest.func_150557_a(6976549, true);
            biomeGenForest.setFillerBlockMetadata(8233509);
            return biomeGenForest;
        }
        return this.biomeID != BiomeGenBase.birchForest.biomeID && this.biomeID != BiomeGenBase.birchForestHills.biomeID ? new BiomeGenMutated(n, this){

            @Override
            public void decorate(World world, Random random, BlockPos blockPos) {
                this.baseBiome.decorate(world, random, blockPos);
            }
        } : new BiomeGenMutated(n, this){

            @Override
            public WorldGenAbstractTree genBigTreeChance(Random random) {
                return random.nextBoolean() ? field_150629_aC : field_150630_aD;
            }
        };
    }

    public BiomeGenForest(int n, int n2) {
        super(n);
        this.field_150632_aF = n2;
        this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;
        if (this.field_150632_aF == 1) {
            this.theBiomeDecorator.treesPerChunk = 6;
            this.theBiomeDecorator.flowersPerChunk = 100;
            this.theBiomeDecorator.grassPerChunk = 1;
        }
        this.setFillerBlockMetadata(5159473);
        this.setTemperatureRainfall(0.7f, 0.8f);
        if (this.field_150632_aF == 2) {
            this.field_150609_ah = 353825;
            this.color = 3175492;
            this.setTemperatureRainfall(0.6f, 0.6f);
        }
        if (this.field_150632_aF == 0) {
            this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }
        if (this.field_150632_aF == 3) {
            this.theBiomeDecorator.treesPerChunk = -999;
        }
    }

    static {
        field_150630_aD = new WorldGenForest(false, false);
        field_150631_aE = new WorldGenCanopyTree(false);
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockPos) {
        if (this.field_150632_aF == 1) {
            double d = MathHelper.clamp_double((1.0 + GRASS_COLOR_NOISE.func_151601_a((double)blockPos.getX() / 48.0, (double)blockPos.getZ() / 48.0)) / 2.0, 0.0, 0.9999);
            BlockFlower.EnumFlowerType enumFlowerType = BlockFlower.EnumFlowerType.values()[(int)(d * (double)BlockFlower.EnumFlowerType.values().length)];
            return enumFlowerType == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : enumFlowerType;
        }
        return super.pickRandomFlower(random, blockPos);
    }

    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        int n = super.getGrassColorAtPos(blockPos);
        return this.field_150632_aF == 3 ? (n & 0xFEFEFE) + 2634762 >> 1 : n;
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return this.field_150632_aF == 3 && random.nextInt(3) > 0 ? field_150631_aE : (this.field_150632_aF != 2 && random.nextInt(5) != 0 ? this.worldGeneratorTrees : field_150630_aD);
    }
}

