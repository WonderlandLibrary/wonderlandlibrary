package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTNTPrimed
extends Entity {
    public int fuse;
    private EntityLivingBase tntPlacedBy;

    public EntityTNTPrimed(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
    }

    public EntityTNTPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * Math.PI * 2.0);
        this.motionX = -((float)Math.sin((double)f)) * 0.02f;
        this.motionY = 0.2f;
        this.motionZ = -((float)Math.cos((double)f)) * 0.02f;
        this.fuse = 80;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    protected void entityInit() {
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= (double)0.04f;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98f;
        this.motionY *= (double)0.98f;
        this.motionZ *= (double)0.98f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
            this.motionY *= -0.5;
        }
        if (this.fuse-- <= 0) {
            this.setDead();
            if (!this.worldObj.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    private void explode() {
        float f = 4.0f;
        this.worldObj.createExplosion((Entity)this, this.posX, this.posY + (double)(this.height / 16.0f), this.posZ, f, true);
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("Fuse", (byte)this.fuse);
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.fuse = tagCompund.getByte("Fuse");
    }

    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    public float getEyeHeight() {
        return 0.0f;
    }
}
