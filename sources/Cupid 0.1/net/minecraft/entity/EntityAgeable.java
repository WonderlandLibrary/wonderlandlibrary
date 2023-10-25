package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
  protected int growingAge;
  
  protected int field_175502_b;
  
  protected int field_175503_c;
  
  private float ageWidth = -1.0F;
  
  private float ageHeight;
  
  public EntityAgeable(World worldIn) {
    super(worldIn);
  }
  
  public abstract EntityAgeable createChild(EntityAgeable paramEntityAgeable);
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (itemstack != null && itemstack.getItem() == Items.spawn_egg) {
      if (!this.worldObj.isRemote) {
        Class<? extends Entity> oclass = EntityList.getClassFromID(itemstack.getMetadata());
        if (oclass != null && getClass() == oclass) {
          EntityAgeable entityageable = createChild(this);
          if (entityageable != null) {
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.worldObj.spawnEntityInWorld(entityageable);
            if (itemstack.hasDisplayName())
              entityageable.setCustomNameTag(itemstack.getDisplayName()); 
            if (!player.capabilities.isCreativeMode) {
              itemstack.stackSize--;
              if (itemstack.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null); 
            } 
          } 
        } 
      } 
      return true;
    } 
    return false;
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(12, Byte.valueOf((byte)0));
  }
  
  public int getGrowingAge() {
    return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.growingAge;
  }
  
  public void func_175501_a(int p_175501_1_, boolean p_175501_2_) {
    int i = getGrowingAge();
    int j = i;
    i += p_175501_1_ * 20;
    if (i > 0) {
      i = 0;
      if (j < 0)
        onGrowingAdult(); 
    } 
    int k = i - j;
    setGrowingAge(i);
    if (p_175501_2_) {
      this.field_175502_b += k;
      if (this.field_175503_c == 0)
        this.field_175503_c = 40; 
    } 
    if (getGrowingAge() == 0)
      setGrowingAge(this.field_175502_b); 
  }
  
  public void addGrowth(int growth) {
    func_175501_a(growth, false);
  }
  
  public void setGrowingAge(int age) {
    this.dataWatcher.updateObject(12, Byte.valueOf((byte)MathHelper.clamp_int(age, -1, 1)));
    this.growingAge = age;
    setScaleForAge(isChild());
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("Age", getGrowingAge());
    tagCompound.setInteger("ForcedAge", this.field_175502_b);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    setGrowingAge(tagCompund.getInteger("Age"));
    this.field_175502_b = tagCompund.getInteger("ForcedAge");
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.worldObj.isRemote) {
      if (this.field_175503_c > 0) {
        if (this.field_175503_c % 4 == 0)
          this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, 0.0D, 0.0D, 0.0D, new int[0]); 
        this.field_175503_c--;
      } 
      setScaleForAge(isChild());
    } else {
      int i = getGrowingAge();
      if (i < 0) {
        i++;
        setGrowingAge(i);
        if (i == 0)
          onGrowingAdult(); 
      } else if (i > 0) {
        i--;
        setGrowingAge(i);
      } 
    } 
  }
  
  protected void onGrowingAdult() {}
  
  public boolean isChild() {
    return (getGrowingAge() < 0);
  }
  
  public void setScaleForAge(boolean p_98054_1_) {
    setScale(p_98054_1_ ? 0.5F : 1.0F);
  }
  
  protected final void setSize(float width, float height) {
    boolean flag = (this.ageWidth > 0.0F);
    this.ageWidth = width;
    this.ageHeight = height;
    if (!flag)
      setScale(1.0F); 
  }
  
  protected final void setScale(float scale) {
    super.setSize(this.ageWidth * scale, this.ageHeight * scale);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EntityAgeable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */