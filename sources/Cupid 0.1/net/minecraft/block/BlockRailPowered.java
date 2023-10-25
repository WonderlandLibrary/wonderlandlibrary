package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase {
  public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>() {
        public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_) {
          return (p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST);
        }
      });
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  protected BlockRailPowered() {
    super(true);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
  }
  
  protected boolean func_176566_a(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_) {
    if (p_176566_5_ >= 8)
      return false; 
    int i = pos.getX();
    int j = pos.getY();
    int k = pos.getZ();
    boolean flag = true;
    BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE);
    switch (blockrailbase$enumraildirection) {
      case NORTH_SOUTH:
        if (p_176566_4_) {
          k++;
          break;
        } 
        k--;
        break;
      case EAST_WEST:
        if (p_176566_4_) {
          i--;
          break;
        } 
        i++;
        break;
      case ASCENDING_EAST:
        if (p_176566_4_) {
          i--;
        } else {
          i++;
          j++;
          flag = false;
        } 
        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
        break;
      case ASCENDING_WEST:
        if (p_176566_4_) {
          i--;
          j++;
          flag = false;
        } else {
          i++;
        } 
        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
        break;
      case ASCENDING_NORTH:
        if (p_176566_4_) {
          k++;
        } else {
          k--;
          j++;
          flag = false;
        } 
        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        break;
      case ASCENDING_SOUTH:
        if (p_176566_4_) {
          k++;
          j++;
          flag = false;
        } else {
          k--;
        } 
        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        break;
    } 
    return func_176567_a(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection) ? true : ((flag && func_176567_a(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection)));
  }
  
  protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_) {
    IBlockState iblockstate = worldIn.getBlockState(p_176567_2_);
    if (iblockstate.getBlock() != this)
      return false; 
    BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue((IProperty)SHAPE);
    return (p_176567_5_ != BlockRailBase.EnumRailDirection.EAST_WEST || (blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH)) ? ((p_176567_5_ != BlockRailBase.EnumRailDirection.NORTH_SOUTH || (blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_WEST)) ? (((Boolean)iblockstate.getValue((IProperty)POWERED)).booleanValue() ? (worldIn.isBlockPowered(p_176567_2_) ? true : func_176566_a(worldIn, p_176567_2_, iblockstate, p_176567_3_, distance + 1)) : false) : false) : false;
  }
  
  protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    boolean flag = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
    boolean flag1 = (worldIn.isBlockPowered(pos) || func_176566_a(worldIn, pos, state, true, 0) || func_176566_a(worldIn, pos, state, false, 0));
    if (flag1 != flag) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(flag1)), 3);
      worldIn.notifyNeighborsOfStateChange(pos.down(), this);
      if (((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).isAscending())
        worldIn.notifyNeighborsOfStateChange(pos.up(), this); 
    } 
  }
  
  public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
    return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).getMetadata();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)SHAPE, (IProperty)POWERED });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRailPowered.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */