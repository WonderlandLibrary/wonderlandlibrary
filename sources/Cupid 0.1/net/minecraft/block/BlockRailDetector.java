package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase {
  public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>() {
        public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_) {
          return (p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST);
        }
      });
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public BlockRailDetector() {
    super(true);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
    setTickRandomly(true);
  }
  
  public int tickRate(World worldIn) {
    return 20;
  }
  
  public boolean canProvidePower() {
    return true;
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote)
      if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
        updatePoweredState(worldIn, pos, state);  
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      updatePoweredState(worldIn, pos, state); 
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((side == EnumFacing.UP) ? 15 : 0);
  }
  
  private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
    boolean flag = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
    boolean flag1 = false;
    List<EntityMinecart> list = findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[0]);
    if (!list.isEmpty())
      flag1 = true; 
    if (flag1 && !flag) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 3);
      worldIn.notifyNeighborsOfStateChange(pos, this);
      worldIn.notifyNeighborsOfStateChange(pos.down(), this);
      worldIn.markBlockRangeForRenderUpdate(pos, pos);
    } 
    if (!flag1 && flag) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)), 3);
      worldIn.notifyNeighborsOfStateChange(pos, this);
      worldIn.notifyNeighborsOfStateChange(pos.down(), this);
      worldIn.markBlockRangeForRenderUpdate(pos, pos);
    } 
    if (flag1)
      worldIn.scheduleUpdate(pos, this, tickRate(worldIn)); 
    worldIn.updateComparatorOutputLevel(pos, this);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    updatePoweredState(worldIn, pos, state);
  }
  
  public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
    return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World worldIn, BlockPos pos) {
    if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue()) {
      List<EntityMinecartCommandBlock> list = findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, (Predicate<Entity>[])new Predicate[0]);
      if (!list.isEmpty())
        return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount(); 
      List<EntityMinecart> list1 = findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[] { EntitySelectors.selectInventories });
      if (!list1.isEmpty())
        return Container.calcRedstoneFromInventory((IInventory)list1.get(0)); 
    } 
    return 0;
  }
  
  protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity>... filter) {
    AxisAlignedBB axisalignedbb = getDectectionBox(pos);
    return (filter.length != 1) ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
  }
  
  private AxisAlignedBB getDectectionBox(BlockPos pos) {
    float f = 0.2F;
    return new AxisAlignedBB((pos.getX() + 0.2F), pos.getY(), (pos.getZ() + 0.2F), ((pos.getX() + 1) - 0.2F), ((pos.getY() + 1) - 0.2F), ((pos.getZ() + 1) - 0.2F));
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRailDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */