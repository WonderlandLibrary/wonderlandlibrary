/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
    protected int sandPerChunk2 = 3;
    protected int bigMushroomsPerChunk;
    protected WorldGenerator diamondGen;
    protected WorldGenerator mushroomRedGen;
    protected int treesPerChunk;
    protected WorldGenerator waterlilyGen;
    protected WorldGenerator sandGen;
    protected int flowersPerChunk = 2;
    protected WorldGenerator coalGen;
    protected WorldGenerator bigMushroomGen;
    protected int clayPerChunk = 1;
    protected WorldGenerator cactusGen;
    protected World currentWorld;
    protected WorldGenerator ironGen;
    protected int deadBushPerChunk;
    protected WorldGenerator goldGen;
    protected int waterlilyPerChunk;
    protected WorldGenerator reedGen;
    protected WorldGenerator gravelAsSandGen;
    protected int cactiPerChunk;
    protected WorldGenerator redstoneGen;
    protected WorldGenFlowers yellowFlowerGen;
    protected WorldGenerator clayGen = new WorldGenClay(4);
    protected WorldGenerator lapisGen;
    public boolean generateLakes = true;
    protected int grassPerChunk = 1;
    protected WorldGenerator andesiteGen;
    protected BlockPos field_180294_c;
    protected WorldGenerator dirtGen;
    protected WorldGenerator graniteGen;
    protected int mushroomsPerChunk;
    protected Random randomGenerator;
    protected WorldGenerator dioriteGen;
    protected WorldGenerator mushroomBrownGen;
    protected int reedsPerChunk;
    protected WorldGenerator gravelGen;
    protected int sandPerChunk = 1;
    protected ChunkProviderSettings chunkProviderSettings;

    public void decorate(World world, Random random, BiomeGenBase biomeGenBase, BlockPos blockPos) {
        if (this.currentWorld != null) {
            throw new RuntimeException("Already decorating");
        }
        this.currentWorld = world;
        String string = world.getWorldInfo().getGeneratorOptions();
        this.chunkProviderSettings = string != null ? ChunkProviderSettings.Factory.jsonToFactory(string).func_177864_b() : ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
        this.randomGenerator = random;
        this.field_180294_c = blockPos;
        this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
        this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
        this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
        this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
        this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
        this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
        this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
        this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
        this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
        this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
        this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
        this.genDecorations(biomeGenBase);
        this.currentWorld = null;
        this.randomGenerator = null;
    }

    public BiomeDecorator() {
        this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
        this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
    }

    protected void generateOres() {
        this.genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
        this.genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
        this.genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
    }

    protected void genStandardOre1(int n, WorldGenerator worldGenerator, int n2, int n3) {
        int n4;
        if (n3 < n2) {
            n4 = n2;
            n2 = n3;
            n3 = n4;
        } else if (n3 == n2) {
            if (n2 < 255) {
                ++n3;
            } else {
                --n2;
            }
        }
        n4 = 0;
        while (n4 < n) {
            BlockPos blockPos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(n3 - n2) + n2, this.randomGenerator.nextInt(16));
            worldGenerator.generate(this.currentWorld, this.randomGenerator, blockPos);
            ++n4;
        }
    }

    protected void genDecorations(BiomeGenBase biomeGenBase) {
        Object object;
        Object object2;
        BlockPos blockPos;
        Object object3;
        int n;
        int n2;
        int n3;
        this.generateOres();
        int n4 = 0;
        while (n4 < this.sandPerChunk2) {
            n3 = this.randomGenerator.nextInt(16) + 8;
            n2 = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(n3, 0, n2)));
            ++n4;
        }
        n4 = 0;
        while (n4 < this.clayPerChunk) {
            n3 = this.randomGenerator.nextInt(16) + 8;
            n2 = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(n3, 0, n2)));
            ++n4;
        }
        n4 = 0;
        while (n4 < this.sandPerChunk) {
            n3 = this.randomGenerator.nextInt(16) + 8;
            n2 = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(n3, 0, n2)));
            ++n4;
        }
        n4 = this.treesPerChunk;
        if (this.randomGenerator.nextInt(10) == 0) {
            ++n4;
        }
        n3 = 0;
        while (n3 < n4) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            n = this.randomGenerator.nextInt(16) + 8;
            object3 = biomeGenBase.genBigTreeChance(this.randomGenerator);
            ((WorldGenerator)object3).func_175904_e();
            blockPos = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n));
            if (((WorldGenerator)object3).generate(this.currentWorld, this.randomGenerator, blockPos)) {
                ((WorldGenAbstractTree)object3).func_180711_a(this.currentWorld, this.randomGenerator, blockPos);
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.bigMushroomsPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            n = this.randomGenerator.nextInt(16) + 8;
            this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n)));
            ++n3;
        }
        n3 = 0;
        while (n3 < this.flowersPerChunk) {
            int n5;
            BlockFlower blockFlower;
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n6 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() + 32;
            if (n6 > 0 && (blockFlower = ((BlockFlower.EnumFlowerType)(object2 = biomeGenBase.pickRandomFlower(this.randomGenerator, (BlockPos)(object = this.field_180294_c.add(n2, n5 = this.randomGenerator.nextInt(n6), n))))).getBlockType().getBlock()).getMaterial() != Material.air) {
                this.yellowFlowerGen.setGeneratedBlock(blockFlower, (BlockFlower.EnumFlowerType)object2);
                this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, (BlockPos)object);
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.grassPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n7 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n7 > 0) {
                int n8 = this.randomGenerator.nextInt(n7);
                biomeGenBase.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n2, n8, n));
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.deadBushPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n9 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n9 > 0) {
                int n10 = this.randomGenerator.nextInt(n9);
                new WorldGenDeadBush().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n2, n10, n));
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.waterlilyPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n11 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n11 > 0) {
                int n12 = this.randomGenerator.nextInt(n11);
                object = this.field_180294_c.add(n2, n12, n);
                while (((Vec3i)object).getY() > 0) {
                    object2 = ((BlockPos)object).down();
                    if (!this.currentWorld.isAirBlock((BlockPos)object2)) break;
                    object = object2;
                }
                this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, (BlockPos)object);
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.mushroomsPerChunk) {
            int n13;
            if (this.randomGenerator.nextInt(4) == 0) {
                n2 = this.randomGenerator.nextInt(16) + 8;
                n = this.randomGenerator.nextInt(16) + 8;
                object3 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n));
                this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, (BlockPos)object3);
            }
            if (this.randomGenerator.nextInt(8) == 0 && (n13 = this.currentWorld.getHeight(this.field_180294_c.add(n2 = this.randomGenerator.nextInt(16) + 8, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2) > 0) {
                int n14 = this.randomGenerator.nextInt(n13);
                object = this.field_180294_c.add(n2, n14, n);
                this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, (BlockPos)object);
            }
            ++n3;
        }
        if (this.randomGenerator.nextInt(4) == 0 && (n = this.currentWorld.getHeight(this.field_180294_c.add(n3 = this.randomGenerator.nextInt(16) + 8, 0, n2 = this.randomGenerator.nextInt(16) + 8)).getY() * 2) > 0) {
            int n15 = this.randomGenerator.nextInt(n);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n3, n15, n2));
        }
        if (this.randomGenerator.nextInt(8) == 0 && (n = this.currentWorld.getHeight(this.field_180294_c.add(n3 = this.randomGenerator.nextInt(16) + 8, 0, n2 = this.randomGenerator.nextInt(16) + 8)).getY() * 2) > 0) {
            int n16 = this.randomGenerator.nextInt(n);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n3, n16, n2));
        }
        n3 = 0;
        while (n3 < this.reedsPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n17 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n17 > 0) {
                int n18 = this.randomGenerator.nextInt(n17);
                this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n2, n18, n));
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 10) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n19 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n19 > 0) {
                int n20 = this.randomGenerator.nextInt(n19);
                this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n2, n20, n));
            }
            ++n3;
        }
        if (this.randomGenerator.nextInt(32) == 0 && (n = this.currentWorld.getHeight(this.field_180294_c.add(n3 = this.randomGenerator.nextInt(16) + 8, 0, n2 = this.randomGenerator.nextInt(16) + 8)).getY() * 2) > 0) {
            int n21 = this.randomGenerator.nextInt(n);
            new WorldGenPumpkin().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n3, n21, n2));
        }
        n3 = 0;
        while (n3 < this.cactiPerChunk) {
            n2 = this.randomGenerator.nextInt(16) + 8;
            int n22 = this.currentWorld.getHeight(this.field_180294_c.add(n2, 0, n = this.randomGenerator.nextInt(16) + 8)).getY() * 2;
            if (n22 > 0) {
                int n23 = this.randomGenerator.nextInt(n22);
                this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(n2, n23, n));
            }
            ++n3;
        }
        if (this.generateLakes) {
            n3 = 0;
            while (n3 < 50) {
                n2 = this.randomGenerator.nextInt(16) + 8;
                n = this.randomGenerator.nextInt(16) + 8;
                int n24 = this.randomGenerator.nextInt(248) + 8;
                if (n24 > 0) {
                    int n25 = this.randomGenerator.nextInt(n24);
                    object = this.field_180294_c.add(n2, n25, n);
                    new WorldGenLiquids(Blocks.flowing_water).generate(this.currentWorld, this.randomGenerator, (BlockPos)object);
                }
                ++n3;
            }
            n3 = 0;
            while (n3 < 20) {
                n2 = this.randomGenerator.nextInt(16) + 8;
                n = this.randomGenerator.nextInt(16) + 8;
                int n26 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
                blockPos = this.field_180294_c.add(n2, n26, n);
                new WorldGenLiquids(Blocks.flowing_lava).generate(this.currentWorld, this.randomGenerator, blockPos);
                ++n3;
            }
        }
    }

    protected void genStandardOre2(int n, WorldGenerator worldGenerator, int n2, int n3) {
        int n4 = 0;
        while (n4 < n) {
            BlockPos blockPos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(n3) + this.randomGenerator.nextInt(n3) + n2 - n3, this.randomGenerator.nextInt(16));
            worldGenerator.generate(this.currentWorld, this.randomGenerator, blockPos);
            ++n4;
        }
    }
}

