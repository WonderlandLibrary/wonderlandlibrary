// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public abstract class EntityFireball extends Entity
{
    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    
    public EntityFireball(final World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
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
    
    public EntityFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        final double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1;
        this.accelerationY = accelY / d0 * 0.1;
        this.accelerationZ = accelZ / d0 * 0.1;
    }
    
    public EntityFireball(final World worldIn, final EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn);
        this.shootingEntity = shooter;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        accelX += this.rand.nextGaussian() * 0.4;
        accelY += this.rand.nextGaussian() * 0.4;
        accelZ += this.rand.nextGaussian() * 0.4;
        final double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1;
        this.accelerationY = accelY / d0 * 0.1;
        this.accelerationZ = accelZ / d0 * 0.1;
    }
    
    @Override
    public void onUpdate() {
        if (this.world.isRemote || ((this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))) {
            super.onUpdate();
            if (this.isFireballFiery()) {
                this.setFire(1);
            }
            ++this.ticksInAir;
            final RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shootingEntity);
            if (raytraceresult != null) {
                this.onImpact(raytraceresult);
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2f);
            float f = this.getMotionFactor();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    final float f2 = 0.25f;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25, this.posY - this.motionY * 0.25, this.posZ - this.motionZ * 0.25, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                f = 0.8f;
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
            this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        else {
            this.setDead();
        }
    }
    
    protected boolean isFireballFiery() {
        return true;
    }
    
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SMOKE_NORMAL;
    }
    
    protected float getMotionFactor() {
        return 0.95f;
    }
    
    protected abstract void onImpact(final RayTraceResult p0);
    
    public static void registerFixesFireball(final DataFixer fixer, final String name) {
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
        compound.setTag("power", this.newDoubleNBTList(this.accelerationX, this.accelerationY, this.accelerationZ));
        compound.setInteger("life", this.ticksAlive);
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        if (compound.hasKey("power", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("power", 6);
            if (nbttaglist.tagCount() == 3) {
                this.accelerationX = nbttaglist.getDoubleAt(0);
                this.accelerationY = nbttaglist.getDoubleAt(1);
                this.accelerationZ = nbttaglist.getDoubleAt(2);
            }
        }
        this.ticksAlive = compound.getInteger("life");
        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3) {
            final NBTTagList nbttaglist2 = compound.getTagList("direction", 6);
            this.motionX = nbttaglist2.getDoubleAt(0);
            this.motionY = nbttaglist2.getDoubleAt(1);
            this.motionZ = nbttaglist2.getDoubleAt(2);
        }
        else {
            this.setDead();
        }
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 1.0f;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.markVelocityChanged();
        if (source.getTrueSource() != null) {
            final Vec3d vec3d = source.getTrueSource().getLookVec();
            if (vec3d != null) {
                this.motionX = vec3d.x;
                this.motionY = vec3d.y;
                this.motionZ = vec3d.z;
                this.accelerationX = this.motionX * 0.1;
                this.accelerationY = this.motionY * 0.1;
                this.accelerationZ = this.motionZ * 0.1;
            }
            if (source.getTrueSource() instanceof EntityLivingBase) {
                this.shootingEntity = (EntityLivingBase)source.getTrueSource();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
}
