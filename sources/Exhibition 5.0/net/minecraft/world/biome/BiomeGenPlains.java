// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.entity.passive.EntityHorse;

public class BiomeGenPlains extends BiomeGenBase
{
    protected boolean field_150628_aC;
    private static final String __OBFID = "CL_00000180";
    
    protected BiomeGenPlains(final int p_i1986_1_) {
        super(p_i1986_1_);
        this.setTemperatureRainfall(0.8f, 0.4f);
        this.setHeight(BiomeGenPlains.height_LowPlains);
        this.spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 10;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random p_180623_1_, final BlockPos p_180623_2_) {
        final double var3 = BiomeGenPlains.field_180281_af.func_151601_a(p_180623_2_.getX() / 200.0, p_180623_2_.getZ() / 200.0);
        if (var3 < -0.8) {
            final int var4 = p_180623_1_.nextInt(4);
            switch (var4) {
                case 0: {
                    return BlockFlower.EnumFlowerType.ORANGE_TULIP;
                }
                case 1: {
                    return BlockFlower.EnumFlowerType.RED_TULIP;
                }
                case 2: {
                    return BlockFlower.EnumFlowerType.PINK_TULIP;
                }
                default: {
                    return BlockFlower.EnumFlowerType.WHITE_TULIP;
                }
            }
        }
        else {
            if (p_180623_1_.nextInt(3) > 0) {
                final int var4 = p_180623_1_.nextInt(3);
                return (var4 == 0) ? BlockFlower.EnumFlowerType.POPPY : ((var4 == 1) ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
            }
            return BlockFlower.EnumFlowerType.DANDELION;
        }
    }
    
    @Override
    public void func_180624_a(final World worldIn, final Random p_180624_2_, final BlockPos p_180624_3_) {
        final double var4 = BiomeGenPlains.field_180281_af.func_151601_a((p_180624_3_.getX() + 8) / 200.0, (p_180624_3_.getZ() + 8) / 200.0);
        if (var4 < -0.8) {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        else {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            BiomeGenPlains.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);
            for (int var5 = 0; var5 < 7; ++var5) {
                final int var6 = p_180624_2_.nextInt(16) + 8;
                final int var7 = p_180624_2_.nextInt(16) + 8;
                final int var8 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var6, 0, var7)).getY() + 32);
                BiomeGenPlains.field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var6, var8, var7));
            }
        }
        if (this.field_150628_aC) {
            BiomeGenPlains.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.SUNFLOWER);
            for (int var5 = 0; var5 < 10; ++var5) {
                final int var6 = p_180624_2_.nextInt(16) + 8;
                final int var7 = p_180624_2_.nextInt(16) + 8;
                final int var8 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var6, 0, var7)).getY() + 32);
                BiomeGenPlains.field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var6, var8, var7));
            }
        }
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final BiomeGenPlains var2 = new BiomeGenPlains(p_180277_1_);
        var2.setBiomeName("Sunflower Plains");
        var2.field_150628_aC = true;
        var2.setColor(9286496);
        var2.field_150609_ah = 14273354;
        return var2;
    }
}
