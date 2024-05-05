package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2
  extends WorldGenAbstractTree
{
  private static final String __OBFID = "CL_00000435";
  
  public WorldGenTaiga2(boolean p_i2025_1_)
  {
    super(p_i2025_1_);
  }
  
  public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
  {
    int var4 = p_180709_2_.nextInt(4) + 6;
    int var5 = 1 + p_180709_2_.nextInt(2);
    int var6 = var4 - var5;
    int var7 = 2 + p_180709_2_.nextInt(2);
    boolean var8 = true;
    if ((p_180709_3_.getY() >= 1) && (p_180709_3_.getY() + var4 + 1 <= 256))
    {
      for (int var9 = p_180709_3_.getY(); (var9 <= p_180709_3_.getY() + 1 + var4) && (var8); var9++)
      {
        boolean var10 = true;
        int var21;
        int var21;
        if (var9 - p_180709_3_.getY() < var5) {
          var21 = 0;
        } else {
          var21 = var7;
        }
        for (int var11 = p_180709_3_.getX() - var21; (var11 <= p_180709_3_.getX() + var21) && (var8); var11++) {
          for (int var12 = p_180709_3_.getZ() - var21; (var12 <= p_180709_3_.getZ() + var21) && (var8); var12++) {
            if ((var9 >= 0) && (var9 < 256))
            {
              Block var13 = worldIn.getBlockState(new BlockPos(var11, var9, var12)).getBlock();
              if ((var13.getMaterial() != Material.air) && (var13.getMaterial() != Material.leaves)) {
                var8 = false;
              }
            }
            else
            {
              var8 = false;
            }
          }
        }
      }
      if (!var8) {
        return false;
      }
      Block var20 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
      if (((var20 == Blocks.grass) || (var20 == Blocks.dirt) || (var20 == Blocks.farmland)) && (p_180709_3_.getY() < 256 - var4 - 1))
      {
        func_175921_a(worldIn, p_180709_3_.offsetDown());
        int var21 = p_180709_2_.nextInt(2);
        int var11 = 1;
        byte var22 = 0;
        for (int var23 = 0; var23 <= var6; var23++)
        {
          int var14 = p_180709_3_.getY() + var4 - var23;
          for (int var15 = p_180709_3_.getX() - var21; var15 <= p_180709_3_.getX() + var21; var15++)
          {
            int var16 = var15 - p_180709_3_.getX();
            for (int var17 = p_180709_3_.getZ() - var21; var17 <= p_180709_3_.getZ() + var21; var17++)
            {
              int var18 = var17 - p_180709_3_.getZ();
              if ((Math.abs(var16) != var21) || (Math.abs(var18) != var21) || (var21 <= 0))
              {
                BlockPos var19 = new BlockPos(var15, var14, var17);
                if (!worldIn.getBlockState(var19).getBlock().isFullBlock()) {
                  func_175905_a(worldIn, var19, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.func_176839_a());
                }
              }
            }
          }
          if (var21 >= var11)
          {
            var21 = var22;
            var22 = 1;
            var11++;
            if (var11 > var7) {
              var11 = var7;
            }
          }
          else
          {
            var21++;
          }
        }
        var23 = p_180709_2_.nextInt(3);
        for (int var14 = 0; var14 < var4 - var23; var14++)
        {
          Block var24 = worldIn.getBlockState(p_180709_3_.offsetUp(var14)).getBlock();
          if ((var24.getMaterial() == Material.air) || (var24.getMaterial() == Material.leaves)) {
            func_175905_a(worldIn, p_180709_3_.offsetUp(var14), Blocks.log, BlockPlanks.EnumType.SPRUCE.func_176839_a());
          }
        }
        return true;
      }
      return false;
    }
    return false;
  }
}
