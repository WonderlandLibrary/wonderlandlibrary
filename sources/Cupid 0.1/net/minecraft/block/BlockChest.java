package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  public final int chestType;
  
  protected BlockChest(int type) {
    super(Material.wood);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
    this.chestType = type;
    setCreativeTab(CreativeTabs.tabDecorations);
    setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public int getRenderType() {
    return 2;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos.north()).getBlock() == this) {
      setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
    } else if (worldIn.getBlockState(pos.south()).getBlock() == this) {
      setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
    } else if (worldIn.getBlockState(pos.west()).getBlock() == this) {
      setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    } else if (worldIn.getBlockState(pos.east()).getBlock() == this) {
      setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
    } else {
      setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    } 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    checkForSurroundingChests(worldIn, pos, state);
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      if (iblockstate.getBlock() == this)
        checkForSurroundingChests(worldIn, blockpos, iblockstate); 
    } 
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing());
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3).getOpposite();
    state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
    BlockPos blockpos = pos.north();
    BlockPos blockpos1 = pos.south();
    BlockPos blockpos2 = pos.west();
    BlockPos blockpos3 = pos.east();
    boolean flag = (this == worldIn.getBlockState(blockpos).getBlock());
    boolean flag1 = (this == worldIn.getBlockState(blockpos1).getBlock());
    boolean flag2 = (this == worldIn.getBlockState(blockpos2).getBlock());
    boolean flag3 = (this == worldIn.getBlockState(blockpos3).getBlock());
    if (!flag && !flag1 && !flag2 && !flag3) {
      worldIn.setBlockState(pos, state, 3);
    } else if (enumfacing.getAxis() != EnumFacing.Axis.X || (!flag && !flag1)) {
      if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3)) {
        if (flag2) {
          worldIn.setBlockState(blockpos2, state, 3);
        } else {
          worldIn.setBlockState(blockpos3, state, 3);
        } 
        worldIn.setBlockState(pos, state, 3);
      } 
    } else {
      if (flag) {
        worldIn.setBlockState(blockpos, state, 3);
      } else {
        worldIn.setBlockState(blockpos1, state, 3);
      } 
      worldIn.setBlockState(pos, state, 3);
    } 
    if (stack.hasDisplayName()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntityChest)
        ((TileEntityChest)tileentity).setCustomName(stack.getDisplayName()); 
    } 
  }
  
  public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state) {
    if (worldIn.isRemote)
      return state; 
    IBlockState iblockstate = worldIn.getBlockState(pos.north());
    IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
    IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
    IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    Block block = iblockstate.getBlock();
    Block block1 = iblockstate1.getBlock();
    Block block2 = iblockstate2.getBlock();
    Block block3 = iblockstate3.getBlock();
    if (block != this && block1 != this) {
      boolean flag = block.isFullBlock();
      boolean flag1 = block1.isFullBlock();
      if (block2 == this || block3 == this) {
        EnumFacing enumfacing2;
        BlockPos blockpos1 = (block2 == this) ? pos.west() : pos.east();
        IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.north());
        IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.south());
        enumfacing = EnumFacing.SOUTH;
        if (block2 == this) {
          enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
        } else {
          enumfacing2 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
        } 
        if (enumfacing2 == EnumFacing.NORTH)
          enumfacing = EnumFacing.NORTH; 
        Block block6 = iblockstate6.getBlock();
        Block block7 = iblockstate7.getBlock();
        if ((flag || block6.isFullBlock()) && !flag1 && !block7.isFullBlock())
          enumfacing = EnumFacing.SOUTH; 
        if ((flag1 || block7.isFullBlock()) && !flag && !block6.isFullBlock())
          enumfacing = EnumFacing.NORTH; 
      } 
    } else {
      EnumFacing enumfacing1;
      BlockPos blockpos = (block == this) ? pos.north() : pos.south();
      IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
      IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
      enumfacing = EnumFacing.EAST;
      if (block == this) {
        enumfacing1 = (EnumFacing)iblockstate.getValue((IProperty)FACING);
      } else {
        enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
      } 
      if (enumfacing1 == EnumFacing.WEST)
        enumfacing = EnumFacing.WEST; 
      Block block4 = iblockstate4.getBlock();
      Block block5 = iblockstate5.getBlock();
      if ((block2.isFullBlock() || block4.isFullBlock()) && !block3.isFullBlock() && !block5.isFullBlock())
        enumfacing = EnumFacing.EAST; 
      if ((block3.isFullBlock() || block5.isFullBlock()) && !block2.isFullBlock() && !block4.isFullBlock())
        enumfacing = EnumFacing.WEST; 
    } 
    state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
    worldIn.setBlockState(pos, state, 3);
    return state;
  }
  
  public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = null;
    for (Object enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
      IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing)enumfacing1));
      if (iblockstate.getBlock() == this)
        return state; 
      if (iblockstate.getBlock().isFullBlock()) {
        if (enumfacing != null) {
          enumfacing = null;
          break;
        } 
        enumfacing = (EnumFacing)enumfacing1;
      } 
    } 
    if (enumfacing != null)
      return state.withProperty((IProperty)FACING, (Comparable)enumfacing.getOpposite()); 
    EnumFacing enumfacing2 = (EnumFacing)state.getValue((IProperty)FACING);
    if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
      enumfacing2 = enumfacing2.getOpposite(); 
    if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
      enumfacing2 = enumfacing2.rotateY(); 
    if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
      enumfacing2 = enumfacing2.getOpposite(); 
    return state.withProperty((IProperty)FACING, (Comparable)enumfacing2);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    int i = 0;
    BlockPos blockpos = pos.west();
    BlockPos blockpos1 = pos.east();
    BlockPos blockpos2 = pos.north();
    BlockPos blockpos3 = pos.south();
    if (worldIn.getBlockState(blockpos).getBlock() == this) {
      if (isDoubleChest(worldIn, blockpos))
        return false; 
      i++;
    } 
    if (worldIn.getBlockState(blockpos1).getBlock() == this) {
      if (isDoubleChest(worldIn, blockpos1))
        return false; 
      i++;
    } 
    if (worldIn.getBlockState(blockpos2).getBlock() == this) {
      if (isDoubleChest(worldIn, blockpos2))
        return false; 
      i++;
    } 
    if (worldIn.getBlockState(blockpos3).getBlock() == this) {
      if (isDoubleChest(worldIn, blockpos3))
        return false; 
      i++;
    } 
    return (i <= 1);
  }
  
  private boolean isDoubleChest(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos).getBlock() != this)
      return false; 
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock() == this)
        return true; 
    } 
    return false;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityChest)
      tileentity.updateContainingBlockInfo(); 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof IInventory) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
      worldIn.updateComparatorOutputLevel(pos, this);
    } 
    super.breakBlock(worldIn, pos, state);
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (worldIn.isRemote)
      return true; 
    ILockableContainer ilockablecontainer = getLockableContainer(worldIn, pos);
    if (ilockablecontainer != null) {
      playerIn.displayGUIChest((IInventory)ilockablecontainer);
      if (this.chestType == 0) {
        playerIn.triggerAchievement(StatList.field_181723_aa);
      } else if (this.chestType == 1) {
        playerIn.triggerAchievement(StatList.field_181737_U);
      } 
    } 
    return true;
  }
  
  public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
    InventoryLargeChest inventoryLargeChest;
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (!(tileentity instanceof TileEntityChest))
      return null; 
    TileEntityChest tileEntityChest = (TileEntityChest)tileentity;
    if (isBlocked(worldIn, pos))
      return null; 
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
      Block block = worldIn.getBlockState(blockpos).getBlock();
      if (block == this) {
        if (isBlocked(worldIn, blockpos))
          return null; 
        TileEntity tileentity1 = worldIn.getTileEntity(blockpos);
        if (tileentity1 instanceof TileEntityChest) {
          if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
            inventoryLargeChest = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileEntityChest, (ILockableContainer)tileentity1);
            continue;
          } 
          inventoryLargeChest = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileentity1, (ILockableContainer)inventoryLargeChest);
        } 
      } 
    } 
    return (ILockableContainer)inventoryLargeChest;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntityChest();
  }
  
  public boolean canProvidePower() {
    return (this.chestType == 1);
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    if (!canProvidePower())
      return 0; 
    int i = 0;
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityChest)
      i = ((TileEntityChest)tileentity).numPlayersUsing; 
    return MathHelper.clamp_int(i, 0, 15);
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return (side == EnumFacing.UP) ? getWeakPower(worldIn, pos, state, side) : 0;
  }
  
  private boolean isBlocked(World worldIn, BlockPos pos) {
    return (isBelowSolidBlock(worldIn, pos) || isOcelotSittingOnChest(worldIn, pos));
  }
  
  private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
  }
  
  private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
    for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)))) {
      EntityOcelot entityocelot = (EntityOcelot)entity;
      if (entityocelot.isSitting())
        return true; 
    } 
    return false;
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World worldIn, BlockPos pos) {
    return Container.calcRedstoneFromInventory((IInventory)getLockableContainer(worldIn, pos));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing = EnumFacing.getFront(meta);
    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
      enumfacing = EnumFacing.NORTH; 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */