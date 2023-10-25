package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase {
  public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);
  
  protected BlockRail() {
    super(false);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
  }
  
  protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (neighborBlock.canProvidePower() && (new BlockRailBase.Rail(this, worldIn, pos, state)).countAdjacentRails() == 3)
      func_176564_a(worldIn, pos, state, false); 
  }
  
  public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
    return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).getMetadata();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)SHAPE });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */