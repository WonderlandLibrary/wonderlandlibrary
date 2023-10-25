package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
  public static final PropertyBool EXPLODE = PropertyBool.create("explode");
  
  public BlockTNT() {
    super(Material.tnt);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)EXPLODE, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    if (worldIn.isBlockPowered(pos)) {
      onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)));
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (worldIn.isBlockPowered(pos)) {
      onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)));
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    if (!worldIn.isRemote) {
      EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
      entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
      worldIn.spawnEntityInWorld((Entity)entitytntprimed);
    } 
  }
  
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    explode(worldIn, pos, state, (EntityLivingBase)null);
  }
  
  public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
    if (!worldIn.isRemote)
      if (((Boolean)state.getValue((IProperty)EXPLODE)).booleanValue()) {
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), igniter);
        worldIn.spawnEntityInWorld((Entity)entitytntprimed);
        worldIn.playSoundAtEntity((Entity)entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
      }  
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (playerIn.getCurrentEquippedItem() != null) {
      Item item = playerIn.getCurrentEquippedItem().getItem();
      if (item == Items.flint_and_steel || item == Items.fire_charge) {
        explode(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)), (EntityLivingBase)playerIn);
        worldIn.setBlockToAir(pos);
        if (item == Items.flint_and_steel) {
          playerIn.getCurrentEquippedItem().damageItem(1, (EntityLivingBase)playerIn);
        } else if (!playerIn.capabilities.isCreativeMode) {
          (playerIn.getCurrentEquippedItem()).stackSize--;
        } 
        return true;
      } 
    } 
    return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
      EntityArrow entityarrow = (EntityArrow)entityIn;
      if (entityarrow.isBurning()) {
        explode(worldIn, pos, worldIn.getBlockState(pos).withProperty((IProperty)EXPLODE, Boolean.valueOf(true)), (entityarrow.shootingEntity instanceof EntityLivingBase) ? (EntityLivingBase)entityarrow.shootingEntity : null);
        worldIn.setBlockToAir(pos);
      } 
    } 
  }
  
  public boolean canDropFromExplosion(Explosion explosionIn) {
    return false;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)EXPLODE, Boolean.valueOf(((meta & 0x1) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)EXPLODE)).booleanValue() ? 1 : 0;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)EXPLODE });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockTNT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */