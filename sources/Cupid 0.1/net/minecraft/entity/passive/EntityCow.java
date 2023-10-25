package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityCow extends EntityAnimal {
  public EntityCow(World worldIn) {
    super(worldIn);
    setSize(0.9F, 1.3F);
    ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 2.0D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.25D, Items.wheat, false));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.25D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
  }
  
  protected String getLivingSound() {
    return "mob.cow.say";
  }
  
  protected String getHurtSound() {
    return "mob.cow.hurt";
  }
  
  protected String getDeathSound() {
    return "mob.cow.hurt";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.cow.step", 0.15F, 1.0F);
  }
  
  protected float getSoundVolume() {
    return 0.4F;
  }
  
  protected Item getDropItem() {
    return Items.leather;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    int i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
    for (int j = 0; j < i; j++)
      dropItem(Items.leather, 1); 
    i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);
    for (int k = 0; k < i; k++) {
      if (isBurning()) {
        dropItem(Items.cooked_beef, 1);
      } else {
        dropItem(Items.beef, 1);
      } 
    } 
  }
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (itemstack != null && itemstack.getItem() == Items.bucket && !player.capabilities.isCreativeMode && !isChild()) {
      if (itemstack.stackSize-- == 1) {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.milk_bucket));
      } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
        player.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
      } 
      return true;
    } 
    return super.interact(player);
  }
  
  public EntityCow createChild(EntityAgeable ageable) {
    return new EntityCow(this.worldObj);
  }
  
  public float getEyeHeight() {
    return this.height;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */