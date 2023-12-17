// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.Entity;

public class EntityTNTPrimed extends Entity
{
    private static final DataParameter<Integer> FUSE;
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;
    
    public EntityTNTPrimed(final World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98f, 0.98f);
    }
    
    public EntityTNTPrimed(final World worldIn, final double x, final double y, final double z, final EntityLivingBase igniter) {
        this(worldIn);
        this.setPosition(x, y, z);
        final float f = (float)(Math.random() * 6.283185307179586);
        this.motionX = -(float)Math.sin(f) * 0.02f;
        this.motionY = 0.20000000298023224;
        this.motionZ = -(float)Math.cos(f) * 0.02f;
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityTNTPrimed.FUSE, 80);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033;
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
            this.motionY *= -0.5;
        }
        --this.fuse;
        if (this.fuse <= 0) {
            this.setDead();
            if (!this.world.isRemote) {
                this.explode();
            }
        }
        else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    private void explode() {
        final float f = 4.0f;
        this.world.createExplosion(this, this.posX, this.posY + this.height / 16.0f, this.posZ, 4.0f, true);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setShort("Fuse", (short)this.getFuse());
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
    }
    
    @Nullable
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.0f;
    }
    
    public void setFuse(final int fuseIn) {
        this.dataManager.set(EntityTNTPrimed.FUSE, fuseIn);
        this.fuse = fuseIn;
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityTNTPrimed.FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }
    }
    
    public int getFuseDataManager() {
        return this.dataManager.get(EntityTNTPrimed.FUSE);
    }
    
    public int getFuse() {
        return this.fuse;
    }
    
    static {
        FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    }
}
