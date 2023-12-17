// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public abstract class EntityThrowable extends Entity implements IProjectile
{
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    protected EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    public Entity ignoreEntity;
    private int ignoreTime;
    
    public EntityThrowable(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25f, 0.25f);
    }
    
    public EntityThrowable(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }
    
    public EntityThrowable(final World worldIn, final EntityLivingBase throwerIn) {
        this(worldIn, throwerIn.posX, throwerIn.posY + throwerIn.getEyeHeight() - 0.10000000149011612, throwerIn.posZ);
        this.thrower = throwerIn;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    public void shoot(final Entity entityThrower, final float rotationPitchIn, final float rotationYawIn, final float pitchOffset, final float velocity, final float inaccuracy) {
        final float f = -MathHelper.sin(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f);
        final float f2 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292f);
        final float f3 = MathHelper.cos(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f);
        this.shoot(f, f2, f3, velocity, inaccuracy);
        this.motionX += entityThrower.motionX;
        this.motionZ += entityThrower.motionZ;
        if (!entityThrower.onGround) {
            this.motionY += entityThrower.motionY;
        }
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
        this.ticksInGround = 0;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
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
            if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
                return;
            }
            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2f;
            this.motionY *= this.rand.nextFloat() * 0.2f;
            this.motionZ *= this.rand.nextFloat() * 0.2f;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else {
            ++this.ticksInAir;
        }
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d2);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null) {
            vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }
        Entity entity = null;
        final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
        double d0 = 0.0;
        boolean flag = false;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity2 = list.get(i);
            if (entity2.canBeCollidedWith()) {
                if (entity2 == this.ignoreEntity) {
                    flag = true;
                }
                else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
                    this.ignoreEntity = entity2;
                    flag = true;
                }
                else {
                    flag = false;
                    final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(0.30000001192092896);
                    final RayTraceResult raytraceresult2 = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                    if (raytraceresult2 != null) {
                        final double d2 = vec3d.squareDistanceTo(raytraceresult2.hitVec);
                        if (d2 < d0 || d0 == 0.0) {
                            entity = entity2;
                            d0 = d2;
                        }
                    }
                }
            }
        }
        if (this.ignoreEntity != null) {
            if (flag) {
                this.ignoreTime = 2;
            }
            else if (this.ignoreTime-- <= 0) {
                this.ignoreEntity = null;
            }
        }
        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }
        if (raytraceresult != null) {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
                this.setPortal(raytraceresult.getBlockPos());
            }
            else {
                this.onImpact(raytraceresult);
            }
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
        float f2 = 0.99f;
        final float f3 = this.getGravityVelocity();
        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                final float f4 = 0.25f;
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25, this.posY - this.motionY * 0.25, this.posZ - this.motionZ * 0.25, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
            f2 = 0.8f;
        }
        this.motionX *= f2;
        this.motionY *= f2;
        this.motionZ *= f2;
        if (!this.hasNoGravity()) {
            this.motionY -= f3;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public float getGravityVelocity() {
        return 0.03f;
    }
    
    protected abstract void onImpact(final RayTraceResult p0);
    
    public static void registerFixesThrowable(final DataFixer fixer, final String name) {
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        final ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        compound.setByte("shake", (byte)this.throwableShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }
        compound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        if (compound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 0xFF);
        }
        this.throwableShake = (compound.getByte("shake") & 0xFF);
        this.inGround = (compound.getByte("inGround") == 1);
        this.thrower = null;
        this.throwerName = compound.getString("ownerName");
        if (this.throwerName != null && this.throwerName.isEmpty()) {
            this.throwerName = null;
        }
        this.thrower = this.getThrower();
    }
    
    @Nullable
    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
            this.thrower = this.world.getPlayerEntityByName(this.throwerName);
            if (this.thrower == null && this.world instanceof WorldServer) {
                try {
                    final Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.throwerName));
                    if (entity instanceof EntityLivingBase) {
                        this.thrower = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable var2) {
                    this.thrower = null;
                }
            }
        }
        return this.thrower;
    }
}
