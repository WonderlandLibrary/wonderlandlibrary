// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import java.util.UUID;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public class EntityLlamaSpit extends Entity implements IProjectile
{
    public EntityLlama owner;
    private NBTTagCompound ownerNbt;
    
    public EntityLlamaSpit(final World worldIn) {
        super(worldIn);
    }
    
    public EntityLlamaSpit(final World worldIn, final EntityLlama p_i47273_2_) {
        super(worldIn);
        this.owner = p_i47273_2_;
        this.setPosition(p_i47273_2_.posX - (p_i47273_2_.width + 1.0f) * 0.5 * MathHelper.sin(p_i47273_2_.renderYawOffset * 0.017453292f), p_i47273_2_.posY + p_i47273_2_.getEyeHeight() - 0.10000000149011612, p_i47273_2_.posZ + (p_i47273_2_.width + 1.0f) * 0.5 * MathHelper.cos(p_i47273_2_.renderYawOffset * 0.017453292f));
        this.setSize(0.25f, 0.25f);
    }
    
    public EntityLlamaSpit(final World worldIn, final double x, final double y, final double z, final double p_i47274_8_, final double p_i47274_10_, final double p_i47274_12_) {
        super(worldIn);
        this.setPosition(x, y, z);
        for (int i = 0; i < 7; ++i) {
            final double d0 = 0.4 + 0.1 * i;
            worldIn.spawnParticle(EnumParticleTypes.SPIT, x, y, z, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0, new int[0]);
        }
        this.motionX = p_i47274_8_;
        this.motionY = p_i47274_10_;
        this.motionZ = p_i47274_12_;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ownerNbt != null) {
            this.restoreOwnerFromSave();
        }
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d2);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null) {
            vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }
        final Entity entity = this.getHitEntity(vec3d, vec3d2);
        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }
        if (raytraceresult != null) {
            this.onHit(raytraceresult);
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        final float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232);
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
        final float f2 = 0.99f;
        final float f3 = 0.06f;
        if (!this.world.isMaterialInBB(this.getEntityBoundingBox(), Material.AIR)) {
            this.setDead();
        }
        else if (this.isInWater()) {
            this.setDead();
        }
        else {
            this.motionX *= 0.9900000095367432;
            this.motionY *= 0.9900000095367432;
            this.motionZ *= 0.9900000095367432;
            if (!this.hasNoGravity()) {
                this.motionY -= 0.05999999865889549;
            }
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    @Nullable
    private Entity getHitEntity(final Vec3d p_190538_1_, final Vec3d p_190538_2_) {
        Entity entity = null;
        final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
        double d0 = 0.0;
        for (final Entity entity2 : list) {
            if (entity2 != this.owner) {
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(0.30000001192092896);
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(p_190538_1_, p_190538_2_);
                if (raytraceresult == null) {
                    continue;
                }
                final double d2 = p_190538_1_.squareDistanceTo(raytraceresult.hitVec);
                if (d2 >= d0 && d0 != 0.0) {
                    continue;
                }
                entity = entity2;
                d0 = d2;
            }
        }
        return entity;
    }
    
    @Override
    public void shoot(double x, double y, double z, final float velocity, final float inaccuracy) {
        final float f = MathHelper.sqrt(x * x + y * y + z * z);
        x /= f;
        y /= f;
        z /= f;
        x += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        y += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        z += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        final float f2 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(y, f2) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }
    
    public void onHit(final RayTraceResult p_190536_1_) {
        if (p_190536_1_.entityHit != null && this.owner != null) {
            p_190536_1_.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 1.0f);
        }
        if (!this.world.isRemote) {
            this.setDead();
        }
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        if (compound.hasKey("Owner", 10)) {
            this.ownerNbt = compound.getCompoundTag("Owner");
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        if (this.owner != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            final UUID uuid = this.owner.getUniqueID();
            nbttagcompound.setUniqueId("OwnerUUID", uuid);
            compound.setTag("Owner", nbttagcompound);
        }
    }
    
    private void restoreOwnerFromSave() {
        if (this.ownerNbt != null && this.ownerNbt.hasUniqueId("OwnerUUID")) {
            final UUID uuid = this.ownerNbt.getUniqueId("OwnerUUID");
            for (final EntityLlama entityllama : this.world.getEntitiesWithinAABB((Class<? extends EntityLlama>)EntityLlama.class, this.getEntityBoundingBox().grow(15.0))) {
                if (entityllama.getUniqueID().equals(uuid)) {
                    this.owner = entityllama;
                    break;
                }
            }
        }
        this.ownerNbt = null;
    }
}
