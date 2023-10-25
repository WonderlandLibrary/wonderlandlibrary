package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees {
  private static final IBlockState field_181633_e = Blocks.log.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, (Comparable)BlockPlanks.EnumType.SPRUCE);
  
  private static final IBlockState field_181634_f = Blocks.leaves.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, (Comparable)BlockPlanks.EnumType.SPRUCE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  private static final IBlockState field_181635_g = Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.PODZOL);
  
  private boolean useBaseHeight;
  
  public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_) {
    super(p_i45457_1_, 13, 15, field_181633_e, field_181634_f);
    this.useBaseHeight = p_i45457_2_;
  }
  
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    int i = func_150533_a(rand);
    if (!func_175929_a(worldIn, rand, position, i))
      return false; 
    func_150541_c(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);
    for (int j = 0; j < i; j++) {
      Block block = worldIn.getBlockState(position.up(j)).getBlock();
      if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves)
        setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata); 
      if (j < i - 1) {
        block = worldIn.getBlockState(position.add(1, j, 0)).getBlock();
        if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves)
          setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata); 
        block = worldIn.getBlockState(position.add(1, j, 1)).getBlock();
        if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves)
          setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata); 
        block = worldIn.getBlockState(position.add(0, j, 1)).getBlock();
        if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves)
          setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata); 
      } 
    } 
    return true;
  }
  
  private void func_150541_c(World worldIn, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_) {
    int i = p_150541_6_.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
    int j = 0;
    for (int k = p_150541_4_ - i; k <= p_150541_4_; k++) {
      int l = p_150541_4_ - k;
      int i1 = p_150541_5_ + MathHelper.floor_float(l / i * 3.5F);
      func_175925_a(worldIn, new BlockPos(p_150541_2_, k, p_150541_3_), i1 + ((l > 0 && i1 == j && (k & 0x1) == 0) ? 1 : 0));
      j = i1;
    } 
  }
  
  public void func_180711_a(World worldIn, Random p_180711_2_, BlockPos p_180711_3_) {
    func_175933_b(worldIn, p_180711_3_.west().north());
    func_175933_b(worldIn, p_180711_3_.east(2).north());
    func_175933_b(worldIn, p_180711_3_.west().south(2));
    func_175933_b(worldIn, p_180711_3_.east(2).south(2));
    for (int i = 0; i < 5; i++) {
      int j = p_180711_2_.nextInt(64);
      int k = j % 8;
      int l = j / 8;
      if (k == 0 || k == 7 || l == 0 || l == 7)
        func_175933_b(worldIn, p_180711_3_.add(-3 + k, 0, -3 + l)); 
    } 
  }
  
  private void func_175933_b(World worldIn, BlockPos p_175933_2_) {
    for (int i = -2; i <= 2; i++) {
      for (int j = -2; j <= 2; j++) {
        if (Math.abs(i) != 2 || Math.abs(j) != 2)
          func_175934_c(worldIn, p_175933_2_.add(i, 0, j)); 
      } 
    } 
  }
  
  private void func_175934_c(World worldIn, BlockPos p_175934_2_) {
    for (int i = 2; i >= -3; i--) {
      BlockPos blockpos = p_175934_2_.up(i);
      Block block = worldIn.getBlockState(blockpos).getBlock();
      if (block == Blocks.grass || block == Blocks.dirt) {
        setBlockAndNotifyAdequately(worldIn, blockpos, field_181635_g);
        break;
      } 
      if (block.getMaterial() != Material.air && i < 0)
        break; 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\feature\WorldGenMegaPineTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */