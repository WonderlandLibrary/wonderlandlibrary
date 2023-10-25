package net.minecraft.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMonsterPlacer extends Item {
  public ItemMonsterPlacer() {
    setHasSubtypes(true);
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public String getItemStackDisplayName(ItemStack stack) {
    String s = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
    String s1 = EntityList.getStringFromID(stack.getMetadata());
    if (s1 != null)
      s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name"); 
    return s;
  }
  
  public int getColorFromItemStack(ItemStack stack, int renderPass) {
    EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(stack.getMetadata()));
    return (entitylist$entityegginfo != null) ? ((renderPass == 0) ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) : 16777215;
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (worldIn.isRemote)
      return true; 
    if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() == Blocks.mob_spawner) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntityMobSpawner) {
        MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
        mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
        tileentity.markDirty();
        worldIn.markBlockForUpdate(pos);
        if (!playerIn.capabilities.isCreativeMode)
          stack.stackSize--; 
        return true;
      } 
    } 
    pos = pos.offset(side);
    double d0 = 0.0D;
    if (side == EnumFacing.UP && iblockstate instanceof net.minecraft.block.BlockFence)
      d0 = 0.5D; 
    Entity entity = spawnCreature(worldIn, stack.getMetadata(), pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D);
    if (entity != null) {
      if (entity instanceof net.minecraft.entity.EntityLivingBase && stack.hasDisplayName())
        entity.setCustomNameTag(stack.getDisplayName()); 
      if (!playerIn.capabilities.isCreativeMode)
        stack.stackSize--; 
    } 
    return true;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (worldIn.isRemote)
      return itemStackIn; 
    MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
    if (movingobjectposition == null)
      return itemStackIn; 
    if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      BlockPos blockpos = movingobjectposition.getBlockPos();
      if (!worldIn.isBlockModifiable(playerIn, blockpos))
        return itemStackIn; 
      if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn))
        return itemStackIn; 
      if (worldIn.getBlockState(blockpos).getBlock() instanceof net.minecraft.block.BlockLiquid) {
        Entity entity = spawnCreature(worldIn, itemStackIn.getMetadata(), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
        if (entity != null) {
          if (entity instanceof net.minecraft.entity.EntityLivingBase && itemStackIn.hasDisplayName())
            ((EntityLiving)entity).setCustomNameTag(itemStackIn.getDisplayName()); 
          if (!playerIn.capabilities.isCreativeMode)
            itemStackIn.stackSize--; 
          playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        } 
      } 
    } 
    return itemStackIn;
  }
  
  public static Entity spawnCreature(World worldIn, int entityID, double x, double y, double z) {
    if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID)))
      return null; 
    Entity entity = null;
    for (int i = 0; i < 1; i++) {
      entity = EntityList.createEntityByID(entityID, worldIn);
      if (entity instanceof net.minecraft.entity.EntityLivingBase) {
        EntityLiving entityliving = (EntityLiving)entity;
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        entityliving.rotationYawHead = entityliving.rotationYaw;
        entityliving.renderYawOffset = entityliving.rotationYaw;
        entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), (IEntityLivingData)null);
        worldIn.spawnEntityInWorld(entity);
        entityliving.playLivingSound();
      } 
    } 
    return entity;
  }
  
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values())
      subItems.add(new ItemStack(itemIn, 1, entitylist$entityegginfo.spawnedID)); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemMonsterPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */