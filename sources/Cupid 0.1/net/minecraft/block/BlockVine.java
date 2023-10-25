package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
  public static final PropertyBool UP = PropertyBool.create("up");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyBool[] ALL_FACES = new PropertyBool[] { UP, NORTH, SOUTH, WEST, EAST };
  
  public BlockVine() {
    super(Material.vine);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)UP, Boolean.valueOf(worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube()));
  }
  
  public void setBlockBoundsForItemRender() {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isReplaceable(World worldIn, BlockPos pos) {
    return true;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    float f = 0.0625F;
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = 1.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    boolean flag = false;
    if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)WEST)).booleanValue()) {
      f4 = Math.max(f4, 0.0625F);
      f1 = 0.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
      flag = true;
    } 
    if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)EAST)).booleanValue()) {
      f1 = Math.min(f1, 0.9375F);
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
      flag = true;
    } 
    if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)NORTH)).booleanValue()) {
      f6 = Math.max(f6, 0.0625F);
      f3 = 0.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      flag = true;
    } 
    if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)SOUTH)).booleanValue()) {
      f3 = Math.min(f3, 0.9375F);
      f6 = 1.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f2 = 0.0F;
      f5 = 1.0F;
      flag = true;
    } 
    if (!flag && canPlaceOn(worldIn.getBlockState(pos.up()).getBlock())) {
      f2 = Math.min(f2, 0.9375F);
      f5 = 1.0F;
      f1 = 0.0F;
      f4 = 1.0F;
      f3 = 0.0F;
      f6 = 1.0F;
    } 
    setBlockBounds(f1, f2, f3, f4, f5, f6);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    return null;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    switch (side) {
      case UP:
        return canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());
      case NORTH:
      case SOUTH:
      case EAST:
      case WEST:
        return canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    } 
    return false;
  }
  
  private boolean canPlaceOn(Block blockIn) {
    return (blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement());
  }
  
  private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
    IBlockState iblockstate = state;
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      PropertyBool propertybool = getPropertyFor((EnumFacing)enumfacing);
      if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue() && !canPlaceOn(worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock())) {
        IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
        if (iblockstate1.getBlock() != this || !((Boolean)iblockstate1.getValue((IProperty)propertybool)).booleanValue())
          state = state.withProperty((IProperty)propertybool, Boolean.valueOf(false)); 
      } 
    } 
    if (getNumGrownFaces(state) == 0)
      return false; 
    if (iblockstate != state)
      worldIn.setBlockState(pos, state, 2); 
    return true;
  }
  
  public int getBlockColor() {
    return ColorizerFoliage.getFoliageColorBasic();
  }
  
  public int getRenderColor(IBlockState state) {
    return ColorizerFoliage.getFoliageColorBasic();
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote && !recheckGrownSides(worldIn, pos, state)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (worldIn.rand.nextInt(4) == 0) {
        int i = 4;
        int j = 5;
        boolean flag = false;
        int k;
        label103: for (k = -i; k <= i; k++) {
          for (int l = -i; l <= i; l++) {
            for (int i1 = -1; i1 <= 1; i1++) {
              if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
                j--;
                if (j <= 0) {
                  flag = true;
                  break label103;
                } 
              } 
            } 
          } 
        } 
        EnumFacing enumfacing1 = EnumFacing.random(rand);
        BlockPos blockpos1 = pos.up();
        if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos1)) {
          if (!flag) {
            IBlockState iblockstate2 = state;
            for (Object enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
              if (rand.nextBoolean() || !canPlaceOn(worldIn.getBlockState(blockpos1.offset((EnumFacing)enumfacing3)).getBlock()))
                iblockstate2 = iblockstate2.withProperty((IProperty)getPropertyFor((EnumFacing)enumfacing3), Boolean.valueOf(false)); 
            } 
            if (((Boolean)iblockstate2.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)WEST)).booleanValue())
              worldIn.setBlockState(blockpos1, iblockstate2, 2); 
          } 
        } else if (enumfacing1.getAxis().isHorizontal() && !((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing1))).booleanValue()) {
          if (!flag) {
            BlockPos blockpos3 = pos.offset(enumfacing1);
            Block block1 = worldIn.getBlockState(blockpos3).getBlock();
            if (block1.blockMaterial == Material.air) {
              EnumFacing enumfacing2 = enumfacing1.rotateY();
              EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
              boolean flag1 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing2))).booleanValue();
              boolean flag2 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing4))).booleanValue();
              BlockPos blockpos4 = blockpos3.offset(enumfacing2);
              BlockPos blockpos = blockpos3.offset(enumfacing4);
              if (flag1 && canPlaceOn(worldIn.getBlockState(blockpos4).getBlock())) {
                worldIn.setBlockState(blockpos3, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing2), Boolean.valueOf(true)), 2);
              } else if (flag2 && canPlaceOn(worldIn.getBlockState(blockpos).getBlock())) {
                worldIn.setBlockState(blockpos3, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
              } else if (flag1 && worldIn.isAirBlock(blockpos4) && canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing2)).getBlock())) {
                worldIn.setBlockState(blockpos4, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
              } else if (flag2 && worldIn.isAirBlock(blockpos) && canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing4)).getBlock())) {
                worldIn.setBlockState(blockpos, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
              } else if (canPlaceOn(worldIn.getBlockState(blockpos3.up()).getBlock())) {
                worldIn.setBlockState(blockpos3, getDefaultState(), 2);
              } 
            } else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
              worldIn.setBlockState(pos, state.withProperty((IProperty)getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
            } 
          } 
        } else if (pos.getY() > 1) {
          BlockPos blockpos2 = pos.down();
          IBlockState iblockstate = worldIn.getBlockState(blockpos2);
          Block block = iblockstate.getBlock();
          if (block.blockMaterial == Material.air) {
            IBlockState iblockstate1 = state;
            for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
              if (rand.nextBoolean())
                iblockstate1 = iblockstate1.withProperty((IProperty)getPropertyFor((EnumFacing)enumfacing), Boolean.valueOf(false)); 
            } 
            if (((Boolean)iblockstate1.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)WEST)).booleanValue())
              worldIn.setBlockState(blockpos2, iblockstate1, 2); 
          } else if (block == this) {
            IBlockState iblockstate3 = iblockstate;
            for (Object enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
              PropertyBool propertybool = getPropertyFor((EnumFacing)enumfacing5);
              if (rand.nextBoolean() && ((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
                iblockstate3 = iblockstate3.withProperty((IProperty)propertybool, Boolean.valueOf(true)); 
            } 
            if (((Boolean)iblockstate3.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)WEST)).booleanValue())
              worldIn.setBlockState(blockpos2, iblockstate3, 2); 
          } 
        } 
      }  
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false));
    return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty)getPropertyFor(facing.getOpposite()), Boolean.valueOf(true)) : iblockstate;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return null;
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
    if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
      player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
      spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
    } else {
      super.harvestBlock(worldIn, player, pos, state, te);
    } 
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)SOUTH, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)WEST, Boolean.valueOf(((meta & 0x2) > 0))).withProperty((IProperty)NORTH, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)EAST, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
      i |= 0x1; 
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
      i |= 0x2; 
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
      i |= 0x4; 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)UP, (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST });
  }
  
  public static PropertyBool getPropertyFor(EnumFacing side) {
    switch (side) {
      case UP:
        return UP;
      case NORTH:
        return NORTH;
      case SOUTH:
        return SOUTH;
      case EAST:
        return EAST;
      case WEST:
        return WEST;
    } 
    throw new IllegalArgumentException(side + " is an invalid choice");
  }
  
  public static int getNumGrownFaces(IBlockState state) {
    int i = 0;
    for (PropertyBool propertybool : ALL_FACES) {
      if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
        i++; 
    } 
    return i;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockVine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */