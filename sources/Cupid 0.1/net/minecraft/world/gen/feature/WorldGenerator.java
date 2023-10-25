package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator {
  private final boolean doBlockNotify;
  
  public WorldGenerator() {
    this(false);
  }
  
  public WorldGenerator(boolean notify) {
    this.doBlockNotify = notify;
  }
  
  public abstract boolean generate(World paramWorld, Random paramRandom, BlockPos paramBlockPos);
  
  public void func_175904_e() {}
  
  protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
    if (this.doBlockNotify) {
      worldIn.setBlockState(pos, state, 3);
    } else {
      worldIn.setBlockState(pos, state, 2);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\feature\WorldGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */