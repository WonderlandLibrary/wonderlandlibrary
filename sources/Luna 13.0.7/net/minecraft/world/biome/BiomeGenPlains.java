package net.minecraft.world.biome;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;

public class BiomeGenPlains
  extends BiomeGenBase
{
  protected boolean field_150628_aC;
  private static final String __OBFID = "CL_00000180";
  
  protected BiomeGenPlains(int p_i1986_1_)
  {
    super(p_i1986_1_);
    setTemperatureRainfall(0.8F, 0.4F);
    setHeight(height_LowPlains);
    this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
    this.theBiomeDecorator.treesPerChunk = 64537;
    this.theBiomeDecorator.flowersPerChunk = 4;
    this.theBiomeDecorator.grassPerChunk = 10;
  }
  
  public BlockFlower.EnumFlowerType pickRandomFlower(Random p_180623_1_, BlockPos p_180623_2_)
  {
    double var3 = field_180281_af.func_151601_a(p_180623_2_.getX() / 200.0D, p_180623_2_.getZ() / 200.0D);
    if (var3 < -0.8D)
    {
      int var5 = p_180623_1_.nextInt(4);
      switch (var5)
      {
      case 0: 
        return BlockFlower.EnumFlowerType.ORANGE_TULIP;
      case 1: 
        return BlockFlower.EnumFlowerType.RED_TULIP;
      case 2: 
        return BlockFlower.EnumFlowerType.PINK_TULIP;
      }
      return BlockFlower.EnumFlowerType.WHITE_TULIP;
    }
    if (p_180623_1_.nextInt(3) > 0)
    {
      int var5 = p_180623_1_.nextInt(3);
      return var5 == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : var5 == 0 ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.OXEYE_DAISY;
    }
    return BlockFlower.EnumFlowerType.DANDELION;
  }
  
  public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
  {
    double var4 = field_180281_af.func_151601_a((p_180624_3_.getX() + 8) / 200.0D, (p_180624_3_.getZ() + 8) / 200.0D);
    if (var4 < -0.8D)
    {
      this.theBiomeDecorator.flowersPerChunk = 15;
      this.theBiomeDecorator.grassPerChunk = 5;
    }
    else
    {
      this.theBiomeDecorator.flowersPerChunk = 4;
      this.theBiomeDecorator.grassPerChunk = 10;
      field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);
      for (int var6 = 0; var6 < 7; var6++)
      {
        int var7 = p_180624_2_.nextInt(16) + 8;
        int var8 = p_180624_2_.nextInt(16) + 8;
        int var9 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var7, 0, var8)).getY() + 32);
        field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var7, var9, var8));
      }
    }
    if (this.field_150628_aC)
    {
      field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.SUNFLOWER);
      for (int var6 = 0; var6 < 10; var6++)
      {
        int var7 = p_180624_2_.nextInt(16) + 8;
        int var8 = p_180624_2_.nextInt(16) + 8;
        int var9 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var7, 0, var8)).getY() + 32);
        field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var7, var9, var8));
      }
    }
    super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
  }
  
  protected BiomeGenBase createMutatedBiome(int p_180277_1_)
  {
    BiomeGenPlains var2 = new BiomeGenPlains(p_180277_1_);
    var2.setBiomeName("Sunflower Plains");
    var2.field_150628_aC = true;
    var2.setColor(9286496);
    var2.field_150609_ah = 14273354;
    return var2;
  }
}
