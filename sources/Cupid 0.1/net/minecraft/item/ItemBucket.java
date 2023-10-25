package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBucket extends Item {
  private Block isFull;
  
  public ItemBucket(Block containedBlock) {
    this.maxStackSize = 1;
    this.isFull = containedBlock;
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    boolean flag = (this.isFull == Blocks.air);
    MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, flag);
    if (movingobjectposition == null)
      return itemStackIn; 
    if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      BlockPos blockpos = movingobjectposition.getBlockPos();
      if (!worldIn.isBlockModifiable(playerIn, blockpos))
        return itemStackIn; 
      if (flag) {
        if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
          return itemStackIn; 
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Material material = iblockstate.getBlock().getMaterial();
        if (material == Material.water && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
          worldIn.setBlockToAir(blockpos);
          playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
          return fillBucket(itemStackIn, playerIn, Items.water_bucket);
        } 
        if (material == Material.lava && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
          worldIn.setBlockToAir(blockpos);
          playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
          return fillBucket(itemStackIn, playerIn, Items.lava_bucket);
        } 
      } else {
        if (this.isFull == Blocks.air)
          return new ItemStack(Items.bucket); 
        BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
        if (!playerIn.canPlayerEdit(blockpos1, movingobjectposition.sideHit, itemStackIn))
          return itemStackIn; 
        if (tryPlaceContainedLiquid(worldIn, blockpos1) && !playerIn.capabilities.isCreativeMode) {
          playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
          return new ItemStack(Items.bucket);
        } 
      } 
    } 
    return itemStackIn;
  }
  
  private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
    if (player.capabilities.isCreativeMode)
      return emptyBuckets; 
    if (--emptyBuckets.stackSize <= 0)
      return new ItemStack(fullBucket); 
    if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
      player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false); 
    return emptyBuckets;
  }
  
  public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos) {
    if (this.isFull == Blocks.air)
      return false; 
    Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
    boolean flag = !material.isSolid();
    if (!worldIn.isAirBlock(pos) && !flag)
      return false; 
    if (worldIn.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water) {
      int i = pos.getX();
      int j = pos.getY();
      int k = pos.getZ();
      worldIn.playSoundEffect((i + 0.5F), (j + 0.5F), (k + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
      for (int l = 0; l < 8; l++)
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]); 
    } else {
      if (!worldIn.isRemote && flag && !material.isLiquid())
        worldIn.destroyBlock(pos, true); 
      worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemBucket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */