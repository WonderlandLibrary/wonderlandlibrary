package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTNTPrimed extends Entity {
   public int fuse;
   private EntityLivingBase tntPlacedBy;

   protected void entityInit() {
   }

   public EntityTNTPrimed(World var1) {
      super(var1);
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.98F);
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      var1.setByte("Fuse", (byte)this.fuse);
   }

   private void explode() {
      float var1 = 4.0F;
      this.worldObj.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, var1, true);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public float getEyeHeight() {
      return 0.0F;
   }

   public EntityTNTPrimed(World var1, double var2, double var4, double var6, EntityLivingBase var8) {
      this(var1);
      this.setPosition(var2, var4, var6);
      float var9 = (float)(Math.random() * 3.141592653589793D * 2.0D);
      this.motionX = (double)(-((float)Math.sin((double)var9)) * 0.02F);
      this.motionY = 0.20000000298023224D;
      this.motionZ = (double)(-((float)Math.cos((double)var9)) * 0.02F);
      this.fuse = 80;
      this.prevPosX = var2;
      this.prevPosY = var4;
      this.prevPosZ = var6;
      this.tntPlacedBy = var8;
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.fuse = var1.getByte("Fuse");
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.03999999910593033D;
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;
      if (this.onGround) {
         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
         this.motionY *= -0.5D;
      }

      if (this.fuse-- <= 0) {
         this.setDead();
         if (!this.worldObj.isRemote) {
            this.explode();
         }
      } else {
         this.handleWaterMovement();
         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
      }

   }

   public EntityLivingBase getTntPlacedBy() {
      return this.tntPlacedBy;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }
}
