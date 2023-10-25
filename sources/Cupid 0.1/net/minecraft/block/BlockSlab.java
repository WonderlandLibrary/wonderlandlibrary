package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab extends Block {
  public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
  
  public BlockSlab(Material materialIn) {
    super(materialIn);
    if (isDouble()) {
      this.fullBlock = true;
    } else {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    } 
    setLightOpacity(255);
  }
  
  protected boolean canSilkHarvest() {
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    if (isDouble()) {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if (iblockstate.getBlock() == this)
        if (iblockstate.getValue((IProperty)HALF) == EnumBlockHalf.TOP) {
          setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
          setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }  
    } 
  }
  
  public void setBlockBoundsForItemRender() {
    if (isDouble()) {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    } 
  }
  
  public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
  }
  
  public boolean isOpaqueCube() {
    return isDouble();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)HALF, EnumBlockHalf.BOTTOM);
    return isDouble() ? iblockstate : ((facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)) ? iblockstate : iblockstate.withProperty((IProperty)HALF, EnumBlockHalf.TOP));
  }
  
  public int quantityDropped(Random random) {
    return isDouble() ? 2 : 1;
  }
  
  public boolean isFullCube() {
    return isDouble();
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    if (isDouble())
      return super.shouldSideBeRendered(worldIn, pos, side); 
    if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side))
      return false; 
    BlockPos blockpos = pos.offset(side.getOpposite());
    IBlockState iblockstate = worldIn.getBlockState(pos);
    IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
    boolean flag = (isSlab(iblockstate.getBlock()) && iblockstate.getValue((IProperty)HALF) == EnumBlockHalf.TOP);
    boolean flag1 = (isSlab(iblockstate1.getBlock()) && iblockstate1.getValue((IProperty)HALF) == EnumBlockHalf.TOP);
    return flag1 ? ((side == EnumFacing.DOWN) ? true : ((side == EnumFacing.UP && super.shouldSideBeRendered(worldIn, pos, side)) ? true : ((!isSlab(iblockstate.getBlock()) || !flag)))) : ((side == EnumFacing.UP) ? true : ((side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side)) ? true : ((!isSlab(iblockstate.getBlock()) || flag))));
  }
  
  protected static boolean isSlab(Block blockIn) {
    return (blockIn == Blocks.stone_slab || blockIn == Blocks.wooden_slab || blockIn == Blocks.stone_slab2);
  }
  
  public abstract String getUnlocalizedName(int paramInt);
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    return super.getDamageValue(worldIn, pos) & 0x7;
  }
  
  public abstract boolean isDouble();
  
  public abstract IProperty<?> getVariantProperty();
  
  public abstract Object getVariant(ItemStack paramItemStack);
  
  public enum EnumBlockHalf implements IStringSerializable {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String name;
    
    EnumBlockHalf(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */