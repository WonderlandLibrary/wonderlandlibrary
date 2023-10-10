package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class EntityEnderCrystal extends Entity {
   public int innerRotation;
   public int health;

   public EntityEnderCrystal(World var1, double var2, double var4, double var6) {
      this(var1);
      this.setPosition(var2, var4, var6);
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         if (!this.isDead && !this.worldObj.isRemote) {
            this.health = 0;
            if (this.health <= 0) {
               this.setDead();
               if (!this.worldObj.isRemote) {
                  this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F, true);
               }
            }
         }

         return true;
      }
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ++this.innerRotation;
      this.dataWatcher.updateObject(8, this.health);
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.posY);
      int var3 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.provider instanceof WorldProviderEnd && this.worldObj.getBlockState(new BlockPos(var1, var2, var3)).getBlock() != Blocks.fire) {
         this.worldObj.setBlockState(new BlockPos(var1, var2, var3), Blocks.fire.getDefaultState());
      }

   }

   public EntityEnderCrystal(World var1) {
      super(var1);
      this.preventEntitySpawning = true;
      this.setSize(2.0F, 2.0F);
      this.health = 5;
      this.innerRotation = this.rand.nextInt(100000);
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
   }

   protected void entityInit() {
      this.dataWatcher.addObject(8, this.health);
   }
}
