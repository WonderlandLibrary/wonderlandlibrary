/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable
extends Entity
implements IProjectile {
    private int xTile = -1;
    private EntityLivingBase thrower;
    private String throwerName;
    private int zTile = -1;
    private int yTile = -1;
    private int ticksInAir;
    protected boolean inGround;
    private int ticksInGround;
    private Block inTile;
    public int throwableShake;

    public EntityThrowable(World world, EntityLivingBase entityLivingBase) {
        super(world);
        this.thrower = entityLivingBase;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY + (double)entityLivingBase.getEyeHeight(), entityLivingBase.posZ, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.posY -= (double)0.1f;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        float f = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
        this.motionY = -MathHelper.sin((this.rotationPitch + this.getInaccuracy()) / 180.0f * (float)Math.PI) * f;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getVelocity(), 1.0f);
    }

    protected float getInaccuracy() {
        return 0.0f;
    }

    @Override
    public void setVelocity(double d, double d2, double d3) {
        this.motionX = d;
        this.motionY = d2;
        this.motionZ = d3;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt_double(d * d + d3 * d3);
            this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(d, d3) * 180.0 / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(d2, f) * 180.0 / Math.PI);
        }
    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.throwableShake > 0) {
            --this.throwableShake;
        }
        if (this.inGround) {
            if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
                return;
            }
            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
            this.ticksInGround = 0;
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
        if (!this.worldObj.isRemote) {
            Entity entity = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d = 0.0;
            EntityLivingBase entityLivingBase = this.getThrower();
            int n = 0;
            while (n < list.size()) {
                Entity entity2 = list.get(n);
                if (entity2.canBeCollidedWith() && (entity2 != entityLivingBase || this.ticksInAir >= 5)) {
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
        }
        if (movingObjectPosition != null) {
            if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(movingObjectPosition.getBlockPos()).getBlock() == Blocks.portal) {
                this.func_181015_d(movingObjectPosition.getBlockPos());
            } else {
                this.onImpact(movingObjectPosition);
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / Math.PI);
        this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0 / Math.PI);
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
        float f2 = 0.99f;
        float f3 = this.getGravityVelocity();
        if (this.isInWater()) {
            int n = 0;
            while (n < 4) {
                float f4 = 0.25f;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ, new int[0]);
                ++n;
            }
            f2 = 0.8f;
        }
        this.motionX *= (double)f2;
        this.motionY *= (double)f2;
        this.motionZ *= (double)f2;
        this.motionY -= (double)f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected float getGravityVelocity() {
        return 0.03f;
    }

    public EntityThrowable(World world, double d, double d2, double d3) {
        super(world);
        this.ticksInGround = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(d, d2, d3);
    }

    @Override
    public void setThrowableHeading(double d, double d2, double d3, float f, float f2) {
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3);
        d /= (double)f3;
        d2 /= (double)f3;
        d3 /= (double)f3;
        d += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        d2 += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        d3 += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        this.motionX = d *= (double)f;
        this.motionY = d2 *= (double)f;
        this.motionZ = d3 *= (double)f;
        float f4 = MathHelper.sqrt_double(d * d + d3 * d3);
        this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(d, d3) * 180.0 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(d2, f4) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    protected abstract void onImpact(MovingObjectPosition var1);

    public EntityThrowable(World world) {
        super(world);
        this.setSize(0.25f, 0.25f);
    }

    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
            if (this.thrower == null && this.worldObj instanceof WorldServer) {
                try {
                    Entity entity = ((WorldServer)this.worldObj).getEntityFromUuid(UUID.fromString(this.throwerName));
                    if (entity instanceof EntityLivingBase) {
                        this.thrower = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable throwable) {
                    this.thrower = null;
                }
            }
        }
        return this.thrower;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("xTile", (short)this.xTile);
        nBTTagCompound.setShort("yTile", (short)this.yTile);
        nBTTagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        nBTTagCompound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setByte("shake", (byte)this.throwableShake);
        nBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }
        nBTTagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.xTile = nBTTagCompound.getShort("xTile");
        this.yTile = nBTTagCompound.getShort("yTile");
        this.zTile = nBTTagCompound.getShort("zTile");
        this.inTile = nBTTagCompound.hasKey("inTile", 8) ? Block.getBlockFromName(nBTTagCompound.getString("inTile")) : Block.getBlockById(nBTTagCompound.getByte("inTile") & 0xFF);
        this.throwableShake = nBTTagCompound.getByte("shake") & 0xFF;
        this.inGround = nBTTagCompound.getByte("inGround") == 1;
        this.thrower = null;
        this.throwerName = nBTTagCompound.getString("ownerName");
        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }
        this.thrower = this.getThrower();
    }

    protected float getVelocity() {
        return 1.5f;
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d2 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d2)) {
            d2 = 4.0;
        }
        return d < (d2 *= 64.0) * d2;
    }
}

