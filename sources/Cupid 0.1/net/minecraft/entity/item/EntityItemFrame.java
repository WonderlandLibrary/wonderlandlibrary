package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging {
  private float itemDropChance = 1.0F;
  
  public EntityItemFrame(World worldIn) {
    super(worldIn);
  }
  
  public EntityItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_) {
    super(worldIn, p_i45852_2_);
    updateFacingWithBoundingBox(p_i45852_3_);
  }
  
  protected void entityInit() {
    getDataWatcher().addObjectByDataType(8, 5);
    getDataWatcher().addObject(9, Byte.valueOf((byte)0));
  }
  
  public float getCollisionBorderSize() {
    return 0.0F;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!source.isExplosion() && getDisplayedItem() != null) {
      if (!this.worldObj.isRemote) {
        dropItemOrSelf(source.getEntity(), false);
        setDisplayedItem((ItemStack)null);
      } 
      return true;
    } 
    return super.attackEntityFrom(source, amount);
  }
  
  public int getWidthPixels() {
    return 12;
  }
  
  public int getHeightPixels() {
    return 12;
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = 16.0D;
    d0 = d0 * 64.0D * this.renderDistanceWeight;
    return (distance < d0 * d0);
  }
  
  public void onBroken(Entity brokenEntity) {
    dropItemOrSelf(brokenEntity, true);
  }
  
  public void dropItemOrSelf(Entity p_146065_1_, boolean p_146065_2_) {
    if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = getDisplayedItem();
      if (p_146065_1_ instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer)p_146065_1_;
        if (entityplayer.capabilities.isCreativeMode) {
          removeFrameFromMap(itemstack);
          return;
        } 
      } 
      if (p_146065_2_)
        entityDropItem(new ItemStack(Items.item_frame), 0.0F); 
      if (itemstack != null && this.rand.nextFloat() < this.itemDropChance) {
        itemstack = itemstack.copy();
        removeFrameFromMap(itemstack);
        entityDropItem(itemstack, 0.0F);
      } 
    } 
  }
  
  private void removeFrameFromMap(ItemStack p_110131_1_) {
    if (p_110131_1_ != null) {
      if (p_110131_1_.getItem() == Items.filled_map) {
        MapData mapdata = ((ItemMap)p_110131_1_.getItem()).getMapData(p_110131_1_, this.worldObj);
        mapdata.mapDecorations.remove("frame-" + getEntityId());
      } 
      p_110131_1_.setItemFrame((EntityItemFrame)null);
    } 
  }
  
  public ItemStack getDisplayedItem() {
    return getDataWatcher().getWatchableObjectItemStack(8);
  }
  
  public void setDisplayedItem(ItemStack p_82334_1_) {
    setDisplayedItemWithUpdate(p_82334_1_, true);
  }
  
  private void setDisplayedItemWithUpdate(ItemStack p_174864_1_, boolean p_174864_2_) {
    if (p_174864_1_ != null) {
      p_174864_1_ = p_174864_1_.copy();
      p_174864_1_.stackSize = 1;
      p_174864_1_.setItemFrame(this);
    } 
    getDataWatcher().updateObject(8, p_174864_1_);
    getDataWatcher().setObjectWatched(8);
    if (p_174864_2_ && this.hangingPosition != null)
      this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air); 
  }
  
  public int getRotation() {
    return getDataWatcher().getWatchableObjectByte(9);
  }
  
  public void setItemRotation(int p_82336_1_) {
    func_174865_a(p_82336_1_, true);
  }
  
  private void func_174865_a(int p_174865_1_, boolean p_174865_2_) {
    getDataWatcher().updateObject(9, Byte.valueOf((byte)(p_174865_1_ % 8)));
    if (p_174865_2_ && this.hangingPosition != null)
      this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air); 
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    if (getDisplayedItem() != null) {
      tagCompound.setTag("Item", (NBTBase)getDisplayedItem().writeToNBT(new NBTTagCompound()));
      tagCompound.setByte("ItemRotation", (byte)getRotation());
      tagCompound.setFloat("ItemDropChance", this.itemDropChance);
    } 
    super.writeEntityToNBT(tagCompound);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
    if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
      setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nbttagcompound), false);
      func_174865_a(tagCompund.getByte("ItemRotation"), false);
      if (tagCompund.hasKey("ItemDropChance", 99))
        this.itemDropChance = tagCompund.getFloat("ItemDropChance"); 
      if (tagCompund.hasKey("Direction"))
        func_174865_a(getRotation() * 2, false); 
    } 
    super.readEntityFromNBT(tagCompund);
  }
  
  public boolean interactFirst(EntityPlayer playerIn) {
    if (getDisplayedItem() == null) {
      ItemStack itemstack = playerIn.getHeldItem();
      if (itemstack != null && !this.worldObj.isRemote) {
        setDisplayedItem(itemstack);
        if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
          playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null); 
      } 
    } else if (!this.worldObj.isRemote) {
      setItemRotation(getRotation() + 1);
    } 
    return true;
  }
  
  public int func_174866_q() {
    return (getDisplayedItem() == null) ? 0 : (getRotation() % 8 + 1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\item\EntityItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */