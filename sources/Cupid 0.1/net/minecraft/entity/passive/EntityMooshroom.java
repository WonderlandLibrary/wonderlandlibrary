package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMooshroom extends EntityCow {
  public EntityMooshroom(World worldIn) {
    super(worldIn);
    setSize(0.9F, 1.3F);
    this.spawnableBlock = (Block)Blocks.mycelium;
  }
  
  public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (itemstack != null && itemstack.getItem() == Items.bowl && getGrowingAge() >= 0) {
      if (itemstack.stackSize == 1) {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.mushroom_stew));
        return true;
      } 
      if (player.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !player.capabilities.isCreativeMode) {
        player.inventory.decrStackSize(player.inventory.currentItem, 1);
        return true;
      } 
    } 
    if (itemstack != null && itemstack.getItem() == Items.shears && getGrowingAge() >= 0) {
      setDead();
      this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      if (!this.worldObj.isRemote) {
        EntityCow entitycow = new EntityCow(this.worldObj);
        entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        entitycow.setHealth(getHealth());
        entitycow.renderYawOffset = this.renderYawOffset;
        if (hasCustomName())
          entitycow.setCustomNameTag(getCustomNameTag()); 
        this.worldObj.spawnEntityInWorld((Entity)entitycow);
        for (int i = 0; i < 5; i++)
          this.worldObj.spawnEntityInWorld((Entity)new EntityItem(this.worldObj, this.posX, this.posY + this.height, this.posZ, new ItemStack((Block)Blocks.red_mushroom))); 
        itemstack.damageItem(1, (EntityLivingBase)player);
        playSound("mob.sheep.shear", 1.0F, 1.0F);
      } 
      return true;
    } 
    return super.interact(player);
  }
  
  public EntityMooshroom createChild(EntityAgeable ageable) {
    return new EntityMooshroom(this.worldObj);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */