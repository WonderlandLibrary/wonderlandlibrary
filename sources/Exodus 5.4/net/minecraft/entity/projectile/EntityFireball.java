/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityFireball
extends Entity {
    public EntityLivingBase shootingEntity;
    private int xTile = -1;
    public double accelerationY;
    public double accelerationZ;
    private boolean inGround;
    private int zTile = -1;
    public double accelerationX;
    private int ticksAlive;
    private int yTile = -1;
    private int ticksInAir;
    private Block inTile;

    protected abstract void onImpact(MovingObjectPosition var1);

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        this.setBeenAttacked();
        if (damageSource.getEntity() != null) {
            Vec3 vec3 = damageSource.getEntity().getLookVec();
            if (vec3 != null) {
                this.motionX = vec3.xCoord;
                this.motionY = vec3.yCoord;
                this.motionZ = vec3.zCoord;
                this.accelerationX = this.motionX * 0.1;
                this.accelerationY = this.motionY * 0.1;
                this.accelerationZ = this.motionZ * 0.1;
            }
            if (damageSource.getEntity() instanceof EntityLivingBase) {
                this.shootingEntity = (EntityLivingBase)damageSource.getEntity();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getBrightnessForRender(float f) {
        return 0xF000F0;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0f;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public EntityFireball(World world, EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        super(world);
        this.shootingEntity = entityLivingBase;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        double d4 = MathHelper.sqrt_double((d += this.rand.nextGaussian() * 0.4) * d + (d2 += this.rand.nextGaussian() * 0.4) * d2 + (d3 += this.rand.nextGaussian() * 0.4) * d3);
        this.accelerationX = d / d4 * 0.1;
        this.accelerationY = d2 / d4 * 0.1;
        this.accelerationZ = d3 / d4 * 0.1;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("xTile", (short)this.xTile);
        nBTTagCompound.setShort("yTile", (short)this.yTile);
        nBTTagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        nBTTagCompound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        nBTTagCompound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
    }

    protected float getMotionFactor() {
        return 0.95f;
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d2 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d2)) {
            d2 = 4.0;
        }
        return d < (d2 *= 64.0) * d2;
    }

    public EntityFireball(World world) {
        super(world);
        this.setSize(1.0f, 1.0f);
    }

    public EntityFireball(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world);
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(d, d2, d3, this.rotationYaw, this.rotationPitch);
        this.setPosition(d, d2, d3);
        double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
        this.accelerationX = d4 / d7 * 0.1;
        this.accelerationY = d5 / d7 * 0.1;
        this.accelerationZ = d6 / d7 * 0.1;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.xTile = nBTTagCompound.getShort("xTile");
        this.yTile = nBTTagCompound.getShort("yTile");
        this.zTile = nBTTagCompound.getShort("zTile");
        this.inTile = nBTTagCompound.hasKey("inTile", 8) ? Block.getBlockFromName(nBTTagCompound.getString("inTile")) : Block.getBlockById(nBTTagCompound.getByte("inTile") & 0xFF);
        boolean bl = this.inGround = nBTTagCompound.getByte("inGround") == 1;
        if (nBTTagCompound.hasKey("direction", 9)) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("direction", 6);
            this.motionX = nBTTagList.getDoubleAt(0);
            this.motionY = nBTTagList.getDoubleAt(1);
            this.motionZ = nBTTagList.getDoubleAt(2);
        } else {
            this.setDead();
        }
    }

    @Override
    public float getBrightness(float f) {
        return 1.0f;
    }

    @Override
    public void onUpdate() {
        if (this.worldObj.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.worldObj.isBlockLoaded(new BlockPos(this))) {
            super.onUpdate();
            this.setFire(1);
            if (this.inGround) {
                if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                    ++this.ticksAlive;
                    if (this.ticksAlive == 600) {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInAir;
            }
            Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingObjectPosition = this.worldObj.rayTraceBlocks(vec3, vec32);
            vec3 = new Vec3(this.posX, this.posY, this.posZ);
            vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingObjectPosition != null) {
                vec32 = new Vec3(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
            }
            Entity entity = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d = 0.0;
            int n = 0;
            while (n < list.size()) {
                Entity entity2 = list.get(n);
                if (entity2.canBeCollidedWith() && (!entity2.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
                    double d2;
                    float f = 0.3f;
                    AxisAlignedBB axisAlignedBB = entity2.getEntityBoundingBox().expand(f, f, f);
                    MovingObjectPosition movingObjectPosition2 = axisAlignedBB.calculateIntercept(vec3, vec32);
                    if (movingObjectPosition2 != null && ((d2 = vec3.squareDistanceTo(movingObjectPosition2.hitVec)) < d || d == 0.0)) {
                        entity = entity2;
                        d = d2;
                    }
                }
                ++n;
            }
            if (entity != null) {
                movingObjectPosition = new MovingObjectPosition(entity);
            }
            if (movingObjectPosition != null) {
                this.onImpact(movingObjectPosition);
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0 / Math.PI) + 90.0f;
            this.rotationPitch = (float)(MathHelper.func_181159_b(f, this.motionY) * 180.0 / Math.PI) - 90.0f;
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
            float f2 = this.getMotionFactor();
            if (this.isInWater()) {
                int n2 = 0;
                while (n2 < 4) {
                    float f3 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                    ++n2;
                }
                f2 = 0.8f;
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            this.setDead();
        }
    }
}

