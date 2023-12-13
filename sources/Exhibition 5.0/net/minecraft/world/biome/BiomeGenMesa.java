// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import java.util.Arrays;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.block.state.IBlockState;

public class BiomeGenMesa extends BiomeGenBase
{
    private IBlockState[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private boolean field_150626_aH;
    private boolean field_150620_aI;
    private static final String __OBFID = "CL_00000176";
    
    public BiomeGenMesa(final int p_i45380_1_, final boolean p_i45380_2_, final boolean p_i45380_3_) {
        super(p_i45380_1_);
        this.field_150626_aH = p_i45380_2_;
        this.field_150620_aI = p_i45380_3_;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0f, 0.0f);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT_PROP, BlockSand.EnumType.RED_SAND);
        this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (p_i45380_3_) {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_) {
        return this.worldGeneratorTrees;
    }
    
    @Override
    public int func_180625_c(final BlockPos p_180625_1_) {
        return 10387789;
    }
    
    @Override
    public int func_180627_b(final BlockPos p_180627_1_) {
        return 9470285;
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random p_180622_2_, final ChunkPrimer p_180622_3_, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        if (this.field_150621_aC == null || this.field_150622_aD != worldIn.getSeed()) {
            this.func_150619_a(worldIn.getSeed());
        }
        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != worldIn.getSeed()) {
            final Random var8 = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(var8, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(var8, 1);
        }
        this.field_150622_aD = worldIn.getSeed();
        double var9 = 0.0;
        if (this.field_150626_aH) {
            final int var10 = (p_180622_4_ & 0xFFFFFFF0) + (p_180622_5_ & 0xF);
            final int var11 = (p_180622_5_ & 0xFFFFFFF0) + (p_180622_4_ & 0xF);
            final double var12 = Math.min(Math.abs(p_180622_6_), this.field_150623_aE.func_151601_a(var10 * 0.25, var11 * 0.25));
            if (var12 > 0.0) {
                final double var13 = 0.001953125;
                final double var14 = Math.abs(this.field_150624_aF.func_151601_a(var10 * var13, var11 * var13));
                var9 = var12 * var12 * 2.5;
                final double var15 = Math.ceil(var14 * 50.0) + 14.0;
                if (var9 > var15) {
                    var9 = var15;
                }
                var9 += 64.0;
            }
        }
        final int var10 = p_180622_4_ & 0xF;
        final int var11 = p_180622_5_ & 0xF;
        final boolean var16 = true;
        IBlockState var17 = Blocks.stained_hardened_clay.getDefaultState();
        IBlockState var18 = this.fillerBlock;
        final int var19 = (int)(p_180622_6_ / 3.0 + 3.0 + p_180622_2_.nextDouble() * 0.25);
        final boolean var20 = Math.cos(p_180622_6_ / 3.0 * 3.141592653589793) > 0.0;
        int var21 = -1;
        boolean var22 = false;
        for (int var23 = 255; var23 >= 0; --var23) {
            if (p_180622_3_.getBlockState(var11, var23, var10).getBlock().getMaterial() == Material.air && var23 < (int)var9) {
                p_180622_3_.setBlockState(var11, var23, var10, Blocks.stone.getDefaultState());
            }
            if (var23 <= p_180622_2_.nextInt(5)) {
                p_180622_3_.setBlockState(var11, var23, var10, Blocks.bedrock.getDefaultState());
            }
            else {
                final IBlockState var24 = p_180622_3_.getBlockState(var11, var23, var10);
                if (var24.getBlock().getMaterial() == Material.air) {
                    var21 = -1;
                }
                else if (var24.getBlock() == Blocks.stone) {
                    if (var21 == -1) {
                        var22 = false;
                        if (var19 <= 0) {
                            var17 = null;
                            var18 = Blocks.stone.getDefaultState();
                        }
                        else if (var23 >= 59 && var23 <= 64) {
                            var17 = Blocks.stained_hardened_clay.getDefaultState();
                            var18 = this.fillerBlock;
                        }
                        if (var23 < 63 && (var17 == null || var17.getBlock().getMaterial() == Material.air)) {
                            var17 = Blocks.water.getDefaultState();
                        }
                        var21 = var19 + Math.max(0, var23 - 63);
                        if (var23 >= 62) {
                            if (this.field_150620_aI && var23 > 86 + var19 * 2) {
                                if (var20) {
                                    p_180622_3_.setBlockState(var11, var23, var10, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                                }
                                else {
                                    p_180622_3_.setBlockState(var11, var23, var10, Blocks.grass.getDefaultState());
                                }
                            }
                            else if (var23 > 66 + var19) {
                                IBlockState var25;
                                if (var23 >= 64 && var23 <= 127) {
                                    if (var20) {
                                        var25 = Blocks.hardened_clay.getDefaultState();
                                    }
                                    else {
                                        var25 = this.func_180629_a(p_180622_4_, var23, p_180622_5_);
                                    }
                                }
                                else {
                                    var25 = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
                                }
                                p_180622_3_.setBlockState(var11, var23, var10, var25);
                            }
                            else {
                                p_180622_3_.setBlockState(var11, var23, var10, this.topBlock);
                                var22 = true;
                            }
                        }
                        else {
                            p_180622_3_.setBlockState(var11, var23, var10, var18);
                            if (var18.getBlock() == Blocks.stained_hardened_clay) {
                                p_180622_3_.setBlockState(var11, var23, var10, var18.getBlock().getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                            }
                        }
                    }
                    else if (var21 > 0) {
                        --var21;
                        if (var22) {
                            p_180622_3_.setBlockState(var11, var23, var10, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                        }
                        else {
                            final IBlockState var25 = this.func_180629_a(p_180622_4_, var23, p_180622_5_);
                            p_180622_3_.setBlockState(var11, var23, var10, var25);
                        }
                    }
                }
            }
        }
    }
    
    private void func_150619_a(final long p_150619_1_) {
        Arrays.fill(this.field_150621_aC = new IBlockState[64], Blocks.hardened_clay.getDefaultState());
        final Random var3 = new Random(p_150619_1_);
        this.field_150625_aG = new NoiseGeneratorPerlin(var3, 1);
        for (int var4 = 0; var4 < 64; ++var4) {
            var4 += var3.nextInt(5) + 1;
            if (var4 < 64) {
                this.field_150621_aC[var4] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
            }
        }
        for (int var4 = var3.nextInt(4) + 2, var5 = 0; var5 < var4; ++var5) {
            for (int var6 = var3.nextInt(3) + 1, var7 = var3.nextInt(64), var8 = 0; var7 + var8 < 64 && var8 < var6; ++var8) {
                this.field_150621_aC[var7 + var8] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }
        for (int var5 = var3.nextInt(4) + 2, var6 = 0; var6 < var5; ++var6) {
            for (int var7 = var3.nextInt(3) + 2, var8 = var3.nextInt(64), var9 = 0; var8 + var9 < 64 && var9 < var7; ++var9) {
                this.field_150621_aC[var8 + var9] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }
        for (int var6 = var3.nextInt(4) + 2, var7 = 0; var7 < var6; ++var7) {
            for (int var8 = var3.nextInt(3) + 1, var9 = var3.nextInt(64), var10 = 0; var9 + var10 < 64 && var10 < var8; ++var10) {
                this.field_150621_aC[var9 + var10] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }
        int var7 = var3.nextInt(3) + 3;
        int var8 = 0;
        for (int var9 = 0; var9 < var7; ++var9) {
            final byte var11 = 1;
            var8 += var3.nextInt(16) + 4;
            for (int var12 = 0; var8 + var12 < 64 && var12 < var11; ++var12) {
                this.field_150621_aC[var8 + var12] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (var8 + var12 > 1 && var3.nextBoolean()) {
                    this.field_150621_aC[var8 + var12 - 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (var8 + var12 < 63 && var3.nextBoolean()) {
                    this.field_150621_aC[var8 + var12 + 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }
    }
    
    private IBlockState func_180629_a(final int p_180629_1_, final int p_180629_2_, final int p_180629_3_) {
        final int var4 = (int)Math.round(this.field_150625_aG.func_151601_a(p_180629_1_ * 1.0 / 512.0, p_180629_1_ * 1.0 / 512.0) * 2.0);
        return this.field_150621_aC[(p_180629_2_ + var4 + 64) % 64];
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final boolean var2 = this.biomeID == BiomeGenBase.mesa.biomeID;
        final BiomeGenMesa var3 = new BiomeGenMesa(p_180277_1_, var2, this.field_150620_aI);
        if (!var2) {
            var3.setHeight(BiomeGenMesa.height_LowHills);
            var3.setBiomeName(this.biomeName + " M");
        }
        else {
            var3.setBiomeName(this.biomeName + " (Bryce)");
        }
        var3.func_150557_a(this.color, true);
        return var3;
    }
}
