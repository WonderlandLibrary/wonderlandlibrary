package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCauldron extends Block {
  public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
  
  public BlockCauldron() {
    super(Material.iron, MapColor.stoneColor);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LEVEL, Integer.valueOf(0)));
  }
  
  public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    float f = 0.125F;
    setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    setBlockBoundsForItemRender();
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
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
    float f = pos.getY() + (6.0F + (3 * i)) / 16.0F;
    if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && (entityIn.getEntityBoundingBox()).minY <= f) {
      entityIn.extinguish();
      setWaterLevel(worldIn, pos, state, i - 1);
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (worldIn.isRemote)
      return true; 
    ItemStack itemstack = playerIn.inventory.getCurrentItem();
    if (itemstack == null)
      return true; 
    int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
    Item item = itemstack.getItem();
    if (item == Items.water_bucket) {
      if (i < 3) {
        if (!playerIn.capabilities.isCreativeMode)
          playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket)); 
        playerIn.triggerAchievement(StatList.field_181725_I);
        setWaterLevel(worldIn, pos, state, 3);
      } 
      return true;
    } 
    if (item == Items.glass_bottle) {
      if (i > 0) {
        if (!playerIn.capabilities.isCreativeMode) {
          ItemStack itemstack2 = new ItemStack((Item)Items.potionitem, 1, 0);
          if (!playerIn.inventory.addItemStackToInventory(itemstack2)) {
            worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack2));
          } else if (playerIn instanceof EntityPlayerMP) {
            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
          } 
          playerIn.triggerAchievement(StatList.field_181726_J);
          itemstack.stackSize--;
          if (itemstack.stackSize <= 0)
            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null); 
        } 
        setWaterLevel(worldIn, pos, state, i - 1);
      } 
      return true;
    } 
    if (i > 0 && item instanceof ItemArmor) {
      ItemArmor itemarmor = (ItemArmor)item;
      if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack)) {
        itemarmor.removeColor(itemstack);
        setWaterLevel(worldIn, pos, state, i - 1);
        playerIn.triggerAchievement(StatList.field_181727_K);
        return true;
      } 
    } 
    if (i > 0 && item instanceof net.minecraft.item.ItemBanner && TileEntityBanner.getPatterns(itemstack) > 0) {
      ItemStack itemstack1 = itemstack.copy();
      itemstack1.stackSize = 1;
      TileEntityBanner.removeBannerData(itemstack1);
      if (itemstack.stackSize <= 1 && !playerIn.capabilities.isCreativeMode) {
        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemstack1);
      } else {
        if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
          worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack1));
        } else if (playerIn instanceof EntityPlayerMP) {
          ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
        } 
        playerIn.triggerAchievement(StatList.field_181728_L);
        if (!playerIn.capabilities.isCreativeMode)
          itemstack.stackSize--; 
      } 
      if (!playerIn.capabilities.isCreativeMode)
        setWaterLevel(worldIn, pos, state, i - 1); 
      return true;
    } 
    return false;
  }
  
  public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
    worldIn.setBlockState(pos, state.withProperty((IProperty)LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
    worldIn.updateComparatorOutputLevel(pos, this);
  }
  
  public void fillWithRain(World worldIn, BlockPos pos) {
    if (worldIn.rand.nextInt(20) == 1) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if (((Integer)iblockstate.getValue((IProperty)LEVEL)).intValue() < 3)
        worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty)LEVEL), 2); 
    } 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.cauldron;
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.cauldron;
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World worldIn, BlockPos pos) {
    return ((Integer)worldIn.getBlockState(pos).getValue((IProperty)LEVEL)).intValue();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)LEVEL, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)LEVEL)).intValue();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)LEVEL });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */