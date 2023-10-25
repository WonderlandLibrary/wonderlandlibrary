package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BetterSnow {
  private static IBakedModel modelSnowLayer = null;
  
  public static void update() {
    modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.snow_layer.getDefaultState());
  }
  
  public static IBakedModel getModelSnowLayer() {
    return modelSnowLayer;
  }
  
  public static IBlockState getStateSnowLayer() {
    return Blocks.snow_layer.getDefaultState();
  }
  
  public static boolean shouldRender(IBlockAccess p_shouldRender_0_, Block p_shouldRender_1_, IBlockState p_shouldRender_2_, BlockPos p_shouldRender_3_) {
    return !checkBlock(p_shouldRender_1_, p_shouldRender_2_) ? false : hasSnowNeighbours(p_shouldRender_0_, p_shouldRender_3_);
  }
  
  private static boolean hasSnowNeighbours(IBlockAccess p_hasSnowNeighbours_0_, BlockPos p_hasSnowNeighbours_1_) {
    Block block = Blocks.snow_layer;
    return (p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.north()).getBlock() != block && p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.south()).getBlock() != block && p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.west()).getBlock() != block && p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.east()).getBlock() != block) ? false : p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.down()).getBlock().isOpaqueCube();
  }
  
  private static boolean checkBlock(Block p_checkBlock_0_, IBlockState p_checkBlock_1_) {
    if (p_checkBlock_0_.isFullCube())
      return false; 
    if (p_checkBlock_0_.isOpaqueCube())
      return false; 
    if (p_checkBlock_0_ instanceof net.minecraft.block.BlockSnow)
      return false; 
    if (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockBush) || (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockDoublePlant) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockFlower) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockMushroom) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockSapling) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockTallGrass))) {
      if (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockFence) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockFenceGate) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockFlowerPot) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockPane) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockReed) && !(p_checkBlock_0_ instanceof net.minecraft.block.BlockWall)) {
        if (p_checkBlock_0_ instanceof net.minecraft.block.BlockRedstoneTorch && p_checkBlock_1_.getValue((IProperty)BlockTorch.FACING) == EnumFacing.UP)
          return true; 
        if (p_checkBlock_0_ instanceof BlockLever) {
          Object object = p_checkBlock_1_.getValue((IProperty)BlockLever.FACING);
          if (object == BlockLever.EnumOrientation.UP_X || object == BlockLever.EnumOrientation.UP_Z)
            return true; 
        } 
        return false;
      } 
      return true;
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\BetterSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */