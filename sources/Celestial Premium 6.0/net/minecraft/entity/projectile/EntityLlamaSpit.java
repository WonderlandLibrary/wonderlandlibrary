/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit
extends Entity
implements IProjectile {
    public EntityLlama field_190539_a;
    private NBTTagCompound field_190540_b;

    public EntityLlamaSpit(World p_i47272_1_) {
        super(p_i47272_1_);
    }

    public EntityLlamaSpit(World p_i47273_1_, EntityLlama p_i47273_2_) {
        super(p_i47273_1_);
        this.field_190539_a = p_i47273_2_;
        this.setPosition(p_i47273_2_.posX - (double)(p_i47273_2_.width + 1.0f) * 0.5 * (double)MathHelper.sin(p_i47273_2_.renderYawOffset * ((float)Math.PI / 180)), p_i47273_2_.posY + (double)p_i47273_2_.getEyeHeight() - (double)0.1f, p_i47273_2_.posZ + (double)(p_i47273_2_.width + 1.0f) * 0.5 * (double)MathHelper.cos(p_i47273_2_.renderYawOffset * ((float)Math.PI / 180)));
        this.setSize(0.25f, 0.25f);
    }

    public EntityLlamaSpit(World p_i47274_1_, double p_i47274_2_, double p_i47274_4_, double p_i47274_6_, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        super(p_i47274_1_);
        this.setPosition(p_i47274_2_, p_i47274_4_, p_i47274_6_);
        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4 + 0.1 * (double)i;
            p_i47274_1_.spawnParticle(EnumParticleTypes.SPIT, p_i47274_2_, p_i47274_4_, p_i47274_6_, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0, new int[0]);
        }
        this.motionX = p_i47274_8_;
        this.motionY = p_i47274_10_;
        this.motionZ = p_i47274_12_;
    }

    @Override
    public void onUpdate() {
        Entity entity;
        super.onUpdate();
        if (this.field_190540_b != null) {
            this.func_190537_j();
        }
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null) {
            vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }
        if ((entity = this.func_190538_a(vec3d, vec3d1)) != null) {
            raytraceresult = new RayTraceResult(entity);
        }
        if (raytraceresult != null) {
            this.func_190536_a(raytraceresult);
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
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
        float f1 = 0.99f;
        float f2 = 0.06f;
        if (!this.world.isMaterialInBB(this.getEntityBoundingBox(), Material.AIR)) {
            this.setDead();
        } else if (this.isInWater()) {
            this.setDead();
        } else {
            this.motionX *= (double)0.99f;
            this.motionY *= (double)0.99f;
            this.motionZ *= (double)0.99f;
            if (!this.hasNoGravity()) {
                this.motionY -= (double)0.06f;
            }
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    @Nullable
    private Entity func_190538_a(Vec3d p_190538_1_, Vec3d p_190538_2_) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0));
        double d0 = 0.0;
        for (Entity entity1 : list) {
            double d1;
            AxisAlignedBB axisalignedbb;
            RayTraceResult raytraceresult;
            if (entity1 == this.field_190539_a || (raytraceresult = (axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.3f)).calculateIntercept(p_190538_1_, p_190538_2_)) == null || !((d1 = p_190538_1_.squareDistanceTo(raytraceresult.hitVec)) < d0) && d0 != 0.0) continue;
            entity = entity1;
            d0 = d1;
        }
        return entity;
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x /= (double)f;
        y /= (double)f;
        z /= (double)f;
        x += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        y += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        z += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        this.motionX = x *= (double)velocity;
        this.motionY = y *= (double)velocity;
        this.motionZ = z *= (double)velocity;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(y, f1) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void func_190536_a(RayTraceResult p_190536_1_) {
        if (p_190536_1_.entityHit != null && this.field_190539_a != null) {
            p_190536_1_.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.field_190539_a).setProjectile(), 1.0f);
        }
        if (!this.world.isRemote) {
            this.setDead();
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("Owner", 10)) {
            this.field_190540_b = compound.getCompoundTag("Owner");
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (this.field_190539_a != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            UUID uuid = this.field_190539_a.getUniqueID();
            nbttagcompound.setUniqueId("OwnerUUID", uuid);
            compound.setTag("Owner", nbttagcompound);
        }
    }

    private void func_190537_j() {
        if (this.field_190540_b != null && this.field_190540_b.hasUniqueId("OwnerUUID")) {
            UUID uuid = this.field_190540_b.getUniqueId("OwnerUUID");
            for (EntityLlama entityllama : this.world.getEntitiesWithinAABB(EntityLlama.class, this.getEntityBoundingBox().expandXyz(15.0))) {
                if (!entityllama.getUniqueID().equals(uuid)) continue;
                this.field_190539_a = entityllama;
                break;
            }
        }
        this.field_190540_b = null;
    }
}

