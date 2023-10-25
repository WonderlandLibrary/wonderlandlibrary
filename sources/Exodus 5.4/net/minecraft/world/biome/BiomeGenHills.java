/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenHills
extends BiomeGenBase {
    private int field_150635_aE = 0;
    private int field_150636_aF = 1;
    private int field_150637_aG = 2;
    private WorldGenTaiga2 field_150634_aD;
    private WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
    private int field_150638_aH;

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        if ((d < -1.0 || d > 2.0) && this.field_150638_aH == this.field_150637_aG) {
            this.topBlock = Blocks.gravel.getDefaultState();
            this.fillerBlock = Blocks.gravel.getDefaultState();
        } else if (d > 1.0 && this.field_150638_aH != this.field_150636_aF) {
            this.topBlock = Blocks.stone.getDefaultState();
            this.fillerBlock = Blocks.stone.getDefaultState();
        }
        this.generateBiomeTerrain(world, random, chunkPrimer, n, n2, d);
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        int n;
        int n2;
        super.decorate(world, random, blockPos);
        int n3 = 3 + random.nextInt(6);
        int n4 = 0;
        while (n4 < n3) {
            int n5;
            n2 = random.nextInt(16);
            BlockPos blockPos2 = blockPos.add(n2, n = random.nextInt(28) + 4, n5 = random.nextInt(16));
            if (world.getBlockState(blockPos2).getBlock() == Blocks.stone) {
                world.setBlockState(blockPos2, Blocks.emerald_ore.getDefaultState(), 2);
            }
            ++n4;
        }
        n3 = 0;
        while (n3 < 7) {
            n4 = random.nextInt(16);
            n2 = random.nextInt(64);
            n = random.nextInt(16);
            this.theWorldGenerator.generate(world, random, blockPos.add(n4, n2, n));
            ++n3;
        }
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return random.nextInt(3) > 0 ? this.field_150634_aD : super.genBigTreeChance(random);
    }

    protected BiomeGenHills(int n, boolean bl) {
        super(n);
        this.field_150634_aD = new WorldGenTaiga2(false);
        this.field_150638_aH = this.field_150635_aE;
        if (bl) {
            this.theBiomeDecorator.treesPerChunk = 3;
            this.field_150638_aH = this.field_150636_aF;
        }
    }

    private BiomeGenHills mutateHills(BiomeGenBase biomeGenBase) {
        this.field_150638_aH = this.field_150637_aG;
        this.func_150557_a(biomeGenBase.color, true);
        this.setBiomeName(String.valueOf(biomeGenBase.biomeName) + " M");
        this.setHeight(new BiomeGenBase.Height(biomeGenBase.minHeight, biomeGenBase.maxHeight));
        this.setTemperatureRainfall(biomeGenBase.temperature, biomeGenBase.rainfall);
        return this;
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        return new BiomeGenHills(n, false).mutateHills(this);
    }
}

