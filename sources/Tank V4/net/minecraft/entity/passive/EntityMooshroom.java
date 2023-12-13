package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMooshroom extends EntityCow {
   public EntityMooshroom createChild(EntityAgeable var1) {
      return new EntityMooshroom(this.worldObj);
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (var2 != null && var2.getItem() == Items.bowl && this.getGrowingAge() >= 0) {
         if (var2.stackSize == 1) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(Items.mushroom_stew));
            return true;
         }

         if (var1.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !var1.capabilities.isCreativeMode) {
            var1.inventory.decrStackSize(var1.inventory.currentItem, 1);
            return true;
         }
      }

      if (var2 != null && var2.getItem() == Items.shears && this.getGrowingAge() >= 0) {
         this.setDead();
         this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);
         if (!this.worldObj.isRemote) {
            EntityCow var3 = new EntityCow(this.worldObj);
            var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            var3.setHealth(this.getHealth());
            var3.renderYawOffset = this.renderYawOffset;
            if (this.hasCustomName()) {
               var3.setCustomNameTag(this.getCustomNameTag());
            }

            this.worldObj.spawnEntityInWorld(var3);

            for(int var4 = 0; var4 < 5; ++var4) {
               this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Blocks.red_mushroom)));
            }

            var2.damageItem(1, var1);
            this.playSound("mob.sheep.shear", 1.0F, 1.0F);
         }

         return true;
      } else {
         return super.interact(var1);
      }
   }

   public EntityMooshroom(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      this.spawnableBlock = Blocks.mycelium;
   }

   public EntityCow createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }
}
