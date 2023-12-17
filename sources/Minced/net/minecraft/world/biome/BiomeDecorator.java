// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStone;
import net.minecraft.world.gen.feature.WorldGenMinable;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.util.math.BlockPos;

public class BiomeDecorator
{
    protected boolean decorating;
    protected BlockPos chunkPos;
    protected ChunkGeneratorSettings chunkProviderSettings;
    protected WorldGenerator clayGen;
    protected WorldGenerator sandGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelOreGen;
    protected WorldGenerator graniteGen;
    protected WorldGenerator dioriteGen;
    protected WorldGenerator andesiteGen;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;
    protected WorldGenerator goldGen;
    protected WorldGenerator redstoneGen;
    protected WorldGenerator diamondGen;
    protected WorldGenerator lapisGen;
    protected WorldGenFlowers flowerGen;
    protected WorldGenerator mushroomBrownGen;
    protected WorldGenerator mushroomRedGen;
    protected WorldGenerator bigMushroomGen;
    protected WorldGenerator reedGen;
    protected WorldGenerator cactusGen;
    protected WorldGenerator waterlilyGen;
    protected int waterlilyPerChunk;
    protected int treesPerChunk;
    protected float extraTreeChance;
    protected int flowersPerChunk;
    protected int grassPerChunk;
    protected int deadBushPerChunk;
    protected int mushroomsPerChunk;
    protected int reedsPerChunk;
    protected int cactiPerChunk;
    protected int gravelPatchesPerChunk;
    protected int sandPatchesPerChunk;
    protected int clayPerChunk;
    protected int bigMushroomsPerChunk;
    public boolean generateFalls;
    
    public BiomeDecorator() {
        this.clayGen = new WorldGenClay(4);
        this.sandGen = new WorldGenSand(Blocks.SAND, 7);
        this.gravelGen = new WorldGenSand(Blocks.GRAVEL, 6);
        this.flowerGen = new WorldGenFlowers(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new WorldGenBush(Blocks.BROWN_MUSHROOM);
        this.mushroomRedGen = new WorldGenBush(Blocks.RED_MUSHROOM);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.extraTreeChance = 0.1f;
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.gravelPatchesPerChunk = 1;
        this.sandPatchesPerChunk = 3;
        this.clayPerChunk = 1;
        this.generateFalls = true;
    }
    
    public void decorate(final World worldIn, final Random random, final Biome biome, final BlockPos pos) {
        if (this.decorating) {
            throw new RuntimeException("Already decorating");
        }
        this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
        this.chunkPos = pos;
        this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
        this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
        this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
        this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
        this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
        this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
        this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
        this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
        this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
        this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
        this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
        this.genDecorations(biome, worldIn, random);
        this.decorating = false;
    }
    
    protected void genDecorations(final Biome biomeIn, final World worldIn, final Random random) {
        this.generateOres(worldIn, random);
        for (int i = 0; i < this.sandPatchesPerChunk; ++i) {
            final int j = random.nextInt(16) + 8;
            final int k = random.nextInt(16) + 8;
            this.sandGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }
        for (int i2 = 0; i2 < this.clayPerChunk; ++i2) {
            final int l1 = random.nextInt(16) + 8;
            final int i3 = random.nextInt(16) + 8;
            this.clayGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l1, 0, i3)));
        }
        for (int j2 = 0; j2 < this.gravelPatchesPerChunk; ++j2) {
            final int i4 = random.nextInt(16) + 8;
            final int j3 = random.nextInt(16) + 8;
            this.gravelGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(i4, 0, j3)));
        }
        int k2 = this.treesPerChunk;
        if (random.nextFloat() < this.extraTreeChance) {
            ++k2;
        }
        for (int j4 = 0; j4 < k2; ++j4) {
            final int k3 = random.nextInt(16) + 8;
            final int m = random.nextInt(16) + 8;
            final WorldGenAbstractTree worldgenabstracttree = biomeIn.getRandomTreeFeature(random);
            worldgenabstracttree.setDecorationDefaults();
            final BlockPos blockpos = worldIn.getHeight(this.chunkPos.add(k3, 0, m));
            if (worldgenabstracttree.generate(worldIn, random, blockpos)) {
                worldgenabstracttree.generateSaplings(worldIn, random, blockpos);
            }
        }
        for (int k4 = 0; k4 < this.bigMushroomsPerChunk; ++k4) {
            final int l2 = random.nextInt(16) + 8;
            final int k5 = random.nextInt(16) + 8;
            this.bigMushroomGen.generate(worldIn, random, worldIn.getHeight(this.chunkPos.add(l2, 0, k5)));
        }
        for (int l3 = 0; l3 < this.flowersPerChunk; ++l3) {
            final int i5 = random.nextInt(16) + 8;
            final int l4 = random.nextInt(16) + 8;
            final int j5 = worldIn.getHeight(this.chunkPos.add(i5, 0, l4)).getY() + 32;
            if (j5 > 0) {
                final int k6 = random.nextInt(j5);
                final BlockPos blockpos2 = this.chunkPos.add(i5, k6, l4);
                final BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeIn.pickRandomFlower(random, blockpos2);
                final BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
                if (blockflower.getDefaultState().getMaterial() != Material.AIR) {
                    this.flowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
                    this.flowerGen.generate(worldIn, random, blockpos2);
                }
            }
        }
        for (int i6 = 0; i6 < this.grassPerChunk; ++i6) {
            final int j6 = random.nextInt(16) + 8;
            final int i7 = random.nextInt(16) + 8;
            final int k7 = worldIn.getHeight(this.chunkPos.add(j6, 0, i7)).getY() * 2;
            if (k7 > 0) {
                final int l5 = random.nextInt(k7);
                biomeIn.getRandomWorldGenForGrass(random).generate(worldIn, random, this.chunkPos.add(j6, l5, i7));
            }
        }
        for (int j7 = 0; j7 < this.deadBushPerChunk; ++j7) {
            final int k8 = random.nextInt(16) + 8;
            final int j8 = random.nextInt(16) + 8;
            final int l6 = worldIn.getHeight(this.chunkPos.add(k8, 0, j8)).getY() * 2;
            if (l6 > 0) {
                final int i8 = random.nextInt(l6);
                new WorldGenDeadBush().generate(worldIn, random, this.chunkPos.add(k8, i8, j8));
            }
        }
        for (int k9 = 0; k9 < this.waterlilyPerChunk; ++k9) {
            final int l7 = random.nextInt(16) + 8;
            final int k10 = random.nextInt(16) + 8;
            final int i9 = worldIn.getHeight(this.chunkPos.add(l7, 0, k10)).getY() * 2;
            if (i9 > 0) {
                final int j9 = random.nextInt(i9);
                BlockPos blockpos3;
                BlockPos blockpos4;
                for (blockpos3 = this.chunkPos.add(l7, j9, k10); blockpos3.getY() > 0; blockpos3 = blockpos4) {
                    blockpos4 = blockpos3.down();
                    if (!worldIn.isAirBlock(blockpos4)) {
                        break;
                    }
                }
                this.waterlilyGen.generate(worldIn, random, blockpos3);
            }
        }
        for (int l8 = 0; l8 < this.mushroomsPerChunk; ++l8) {
            if (random.nextInt(4) == 0) {
                final int i10 = random.nextInt(16) + 8;
                final int l9 = random.nextInt(16) + 8;
                final BlockPos blockpos5 = worldIn.getHeight(this.chunkPos.add(i10, 0, l9));
                this.mushroomBrownGen.generate(worldIn, random, blockpos5);
            }
            if (random.nextInt(8) == 0) {
                final int j10 = random.nextInt(16) + 8;
                final int i11 = random.nextInt(16) + 8;
                final int j11 = worldIn.getHeight(this.chunkPos.add(j10, 0, i11)).getY() * 2;
                if (j11 > 0) {
                    final int k11 = random.nextInt(j11);
                    final BlockPos blockpos6 = this.chunkPos.add(j10, k11, i11);
                    this.mushroomRedGen.generate(worldIn, random, blockpos6);
                }
            }
        }
        if (random.nextInt(4) == 0) {
            final int i12 = random.nextInt(16) + 8;
            final int k12 = random.nextInt(16) + 8;
            final int j12 = worldIn.getHeight(this.chunkPos.add(i12, 0, k12)).getY() * 2;
            if (j12 > 0) {
                final int k13 = random.nextInt(j12);
                this.mushroomBrownGen.generate(worldIn, random, this.chunkPos.add(i12, k13, k12));
            }
        }
        if (random.nextInt(8) == 0) {
            final int j13 = random.nextInt(16) + 8;
            final int l10 = random.nextInt(16) + 8;
            final int k14 = worldIn.getHeight(this.chunkPos.add(j13, 0, l10)).getY() * 2;
            if (k14 > 0) {
                final int l11 = random.nextInt(k14);
                this.mushroomRedGen.generate(worldIn, random, this.chunkPos.add(j13, l11, l10));
            }
        }
        for (int k15 = 0; k15 < this.reedsPerChunk; ++k15) {
            final int i13 = random.nextInt(16) + 8;
            final int l12 = random.nextInt(16) + 8;
            final int i14 = worldIn.getHeight(this.chunkPos.add(i13, 0, l12)).getY() * 2;
            if (i14 > 0) {
                final int l13 = random.nextInt(i14);
                this.reedGen.generate(worldIn, random, this.chunkPos.add(i13, l13, l12));
            }
        }
        for (int l14 = 0; l14 < 10; ++l14) {
            final int j14 = random.nextInt(16) + 8;
            final int i15 = random.nextInt(16) + 8;
            final int j15 = worldIn.getHeight(this.chunkPos.add(j14, 0, i15)).getY() * 2;
            if (j15 > 0) {
                final int i16 = random.nextInt(j15);
                this.reedGen.generate(worldIn, random, this.chunkPos.add(j14, i16, i15));
            }
        }
        if (random.nextInt(32) == 0) {
            final int i17 = random.nextInt(16) + 8;
            final int k16 = random.nextInt(16) + 8;
            final int j16 = worldIn.getHeight(this.chunkPos.add(i17, 0, k16)).getY() * 2;
            if (j16 > 0) {
                final int k17 = random.nextInt(j16);
                new WorldGenPumpkin().generate(worldIn, random, this.chunkPos.add(i17, k17, k16));
            }
        }
        for (int j17 = 0; j17 < this.cactiPerChunk; ++j17) {
            final int l15 = random.nextInt(16) + 8;
            final int k18 = random.nextInt(16) + 8;
            final int l16 = worldIn.getHeight(this.chunkPos.add(l15, 0, k18)).getY() * 2;
            if (l16 > 0) {
                final int j18 = random.nextInt(l16);
                this.cactusGen.generate(worldIn, random, this.chunkPos.add(l15, j18, k18));
            }
        }
        if (this.generateFalls) {
            for (int k19 = 0; k19 < 50; ++k19) {
                final int i18 = random.nextInt(16) + 8;
                final int l17 = random.nextInt(16) + 8;
                final int i19 = random.nextInt(248) + 8;
                if (i19 > 0) {
                    final int k20 = random.nextInt(i19);
                    final BlockPos blockpos7 = this.chunkPos.add(i18, k20, l17);
                    new WorldGenLiquids(Blocks.FLOWING_WATER).generate(worldIn, random, blockpos7);
                }
            }
            for (int l18 = 0; l18 < 20; ++l18) {
                final int j19 = random.nextInt(16) + 8;
                final int i20 = random.nextInt(16) + 8;
                final int j20 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
                final BlockPos blockpos8 = this.chunkPos.add(j19, j20, i20);
                new WorldGenLiquids(Blocks.FLOWING_LAVA).generate(worldIn, random, blockpos8);
            }
        }
    }
    
    protected void generateOres(final World worldIn, final Random random) {
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.gravelCount, this.gravelOreGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
        this.genStandardOre1(worldIn, random, this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
        this.genStandardOre2(worldIn, random, this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
    }
    
    protected void genStandardOre1(final World worldIn, final Random random, final int blockCount, final WorldGenerator generator, int minHeight, int maxHeight) {
        if (maxHeight < minHeight) {
            final int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        }
        else if (maxHeight == minHeight) {
            if (minHeight < 255) {
                ++maxHeight;
            }
            else {
                --minHeight;
            }
        }
        for (int j = 0; j < blockCount; ++j) {
            final BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
            generator.generate(worldIn, random, blockpos);
        }
    }
    
    protected void genStandardOre2(final World worldIn, final Random random, final int blockCount, final WorldGenerator generator, final int centerHeight, final int spread) {
        for (int i = 0; i < blockCount; ++i) {
            final BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(spread) + random.nextInt(spread) + centerHeight - spread, random.nextInt(16));
            generator.generate(worldIn, random, blockpos);
        }
    }
}
