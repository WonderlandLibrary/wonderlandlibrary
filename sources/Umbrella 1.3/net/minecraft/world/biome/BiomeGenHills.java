/*
 * Decompiled with CFR 0.150.
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
    private WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT_PROP, (Comparable)((Object)BlockSilverfish.EnumType.STONE)), 9);
    private WorldGenTaiga2 field_150634_aD = new WorldGenTaiga2(false);
    private int field_150635_aE = 0;
    private int field_150636_aF = 1;
    private int field_150637_aG = 2;
    private int field_150638_aH = this.field_150635_aE;
    private static final String __OBFID = "CL_00000168";

    protected BiomeGenHills(int p_i45373_1_, boolean p_i45373_2_) {
        super(p_i45373_1_);
        if (p_i45373_2_) {
            this.theBiomeDecorator.treesPerChunk = 3;
            this.field_150638_aH = this.field_150636_aF;
        }
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_) {
        return p_150567_1_.nextInt(3) > 0 ? this.field_150634_aD : super.genBigTreeChance(p_150567_1_);
    }

    @Override
    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_) {
        int var7;
        int var6;
        int var5;
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
        int var4 = 3 + p_180624_2_.nextInt(6);
        for (var5 = 0; var5 < var4; ++var5) {
            int var8;
            var6 = p_180624_2_.nextInt(16);
            BlockPos var9 = p_180624_3_.add(var6, var7 = p_180624_2_.nextInt(28) + 4, var8 = p_180624_2_.nextInt(16));
            if (worldIn.getBlockState(var9).getBlock() != Blocks.stone) continue;
            worldIn.setBlockState(var9, Blocks.emerald_ore.getDefaultState(), 2);
        }
        for (var4 = 0; var4 < 7; ++var4) {
            var5 = p_180624_2_.nextInt(16);
            var6 = p_180624_2_.nextInt(64);
            var7 = p_180624_2_.nextInt(16);
            this.theWorldGenerator.generate(worldIn, p_180624_2_, p_180624_3_.add(var5, var6, var7));
        }
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random p_180622_2_, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_) {
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        if ((p_180622_6_ < -1.0 || p_180622_6_ > 2.0) && this.field_150638_aH == this.field_150637_aG) {
            this.topBlock = Blocks.gravel.getDefaultState();
            this.fillerBlock = Blocks.gravel.getDefaultState();
        } else if (p_180622_6_ > 1.0 && this.field_150638_aH != this.field_150636_aF) {
            this.topBlock = Blocks.stone.getDefaultState();
            this.fillerBlock = Blocks.stone.getDefaultState();
        }
        this.func_180628_b(worldIn, p_180622_2_, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }

    private BiomeGenHills mutateHills(BiomeGenBase p_150633_1_) {
        this.field_150638_aH = this.field_150637_aG;
        this.func_150557_a(p_150633_1_.color, true);
        this.setBiomeName(String.valueOf(p_150633_1_.biomeName) + " M");
        this.setHeight(new BiomeGenBase.Height(p_150633_1_.minHeight, p_150633_1_.maxHeight));
        this.setTemperatureRainfall(p_150633_1_.temperature, p_150633_1_.rainfall);
        return this;
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
        return new BiomeGenHills(p_180277_1_, false).mutateHills(this);
    }
}

