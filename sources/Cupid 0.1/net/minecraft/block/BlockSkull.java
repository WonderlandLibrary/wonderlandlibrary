package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer {
  public static final PropertyDirection FACING = PropertyDirection.create("facing");
  
  public static final PropertyBool NODROP = PropertyBool.create("nodrop");
  
  private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
      public boolean apply(BlockWorldState p_apply_1_) {
        return (p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.skull && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1);
      }
    };
  
  private BlockPattern witherBasePattern;
  
  private BlockPattern witherPattern;
  
  protected BlockSkull() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)NODROP, Boolean.valueOf(false)));
    setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal("tile.skull.skeleton.name");
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    switch ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)) {
      default:
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
        return;
      case NORTH:
        setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
        return;
      case SOUTH:
        setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
        return;
      case WEST:
        setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
        return;
      case EAST:
        break;
    } 
    setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)NODROP, Boolean.valueOf(false));
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntitySkull();
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.skull;
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return (tileentity instanceof TileEntitySkull) ? ((TileEntitySkull)tileentity).getSkullType() : super.getDamageValue(worldIn, pos);
  }
  
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (player.capabilities.isCreativeMode) {
      state = state.withProperty((IProperty)NODROP, Boolean.valueOf(true));
      worldIn.setBlockState(pos, state, 4);
    } 
    super.onBlockHarvested(worldIn, pos, state, player);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote) {
      if (!((Boolean)state.getValue((IProperty)NODROP)).booleanValue()) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySkull) {
          TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
          ItemStack itemstack = new ItemStack(Items.skull, 1, getDamageValue(worldIn, pos));
          if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
            itemstack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
            itemstack.getTagCompound().setTag("SkullOwner", (NBTBase)nbttagcompound);
          } 
          spawnAsEntity(worldIn, pos, itemstack);
        } 
      } 
      super.breakBlock(worldIn, pos, state);
    } 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.skull;
  }
  
  public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack) {
    return (stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) ? ((getWitherBasePattern().match(worldIn, pos) != null)) : false;
  }
  
  public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te) {
    if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
      BlockPattern blockpattern = getWitherPattern();
      BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
      if (blockpattern$patternhelper != null) {
        for (int i = 0; i < 3; i++) {
          BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
          worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty((IProperty)NODROP, Boolean.valueOf(true)), 2);
        } 
        for (int j = 0; j < blockpattern.getPalmLength(); j++) {
          for (int k = 0; k < blockpattern.getThumbLength(); k++) {
            BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
            worldIn.setBlockState(blockworldstate1.getPos(), Blocks.air.getDefaultState(), 2);
          } 
        } 
        BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
        EntityWither entitywither = new EntityWither(worldIn);
        BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
        entitywither.setLocationAndAngles(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.55D, blockpos1.getZ() + 0.5D, (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? 0.0F : 90.0F, 0.0F);
        entitywither.renderYawOffset = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? 0.0F : 90.0F;
        entitywither.func_82206_m();
        for (EntityPlayer entityplayer : worldIn.getEntitiesWithinAABB(EntityPlayer.class, entitywither.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D)))
          entityplayer.triggerAchievement((StatBase)AchievementList.spawnWither); 
        worldIn.spawnEntityInWorld((Entity)entitywither);
        for (int l = 0; l < 120; l++)
          worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos.getX() + worldIn.rand.nextDouble(), (blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D, blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]); 
        for (int i1 = 0; i1 < blockpattern.getPalmLength(); i1++) {
          for (int j1 = 0; j1 < blockpattern.getThumbLength(); j1++) {
            BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
            worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
          } 
        } 
      } 
    } 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getFront(meta & 0x7)).withProperty((IProperty)NODROP, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
    if (((Boolean)state.getValue((IProperty)NODROP)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)NODROP });
  }
  
  protected BlockPattern getWitherBasePattern() {
    if (this.witherBasePattern == null)
      this.witherBasePattern = FactoryBlockPattern.start().aisle(new String[] { "   ", "###", "~#~" }).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.air))).build(); 
    return this.witherBasePattern;
  }
  
  protected BlockPattern getWitherPattern() {
    if (this.witherPattern == null)
      this.witherPattern = FactoryBlockPattern.start().aisle(new String[] { "^^^", "###", "~#~" }).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.air))).build(); 
    return this.witherPattern;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */