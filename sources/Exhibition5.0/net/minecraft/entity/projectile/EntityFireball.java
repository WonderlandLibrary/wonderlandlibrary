// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public abstract class EntityFireball extends Entity
{
    private int field_145795_e;
    private int field_145793_f;
    private int field_145794_g;
    private Block field_145796_h;
    private boolean inGround;
    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    private static final String __OBFID = "CL_00001717";
    
    public EntityFireball(final World worldIn) {
        super(worldIn);
        this.field_145795_e = -1;
        this.field_145793_f = -1;
        this.field_145794_g = -1;
        this.setSize(1.0f, 1.0f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        var3 *= 64.0;
        return distance < var3 * var3;
    }
    
    public EntityFireball(final World worldIn, final double p_i1760_2_, final double p_i1760_4_, final double p_i1760_6_, final double p_i1760_8_, final double p_i1760_10_, final double p_i1760_12_) {
        super(worldIn);
        this.field_145795_e = -1;
        this.field_145793_f = -1;
        this.field_145794_g = -1;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, this.rotationYaw, this.rotationPitch);
        this.setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
        final double var14 = MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
        this.accelerationX = p_i1760_8_ / var14 * 0.1;
        this.accelerationY = p_i1760_10_ / var14 * 0.1;
        this.accelerationZ = p_i1760_12_ / var14 * 0.1;
    }
    
    public EntityFireball(final World worldIn, final EntityLivingBase p_i1761_2_, double p_i1761_3_, double p_i1761_5_, double p_i1761_7_) {
        super(worldIn);
        this.field_145795_e = -1;
        this.field_145793_f = -1;
        this.field_145794_g = -1;
        this.shootingEntity = p_i1761_2_;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(p_i1761_2_.posX, p_i1761_2_.posY, p_i1761_2_.posZ, p_i1761_2_.rotationYaw, p_i1761_2_.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        p_i1761_3_ += this.rand.nextGaussian() * 0.4;
        p_i1761_5_ += this.rand.nextGaussian() * 0.4;
        p_i1761_7_ += this.rand.nextGaussian() * 0.4;
        final double var9 = MathHelper.sqrt_double(p_i1761_3_ * p_i1761_3_ + p_i1761_5_ * p_i1761_5_ + p_i1761_7_ * p_i1761_7_);
        this.accelerationX = p_i1761_3_ / var9 * 0.1;
        this.accelerationY = p_i1761_5_ / var9 * 0.1;
        this.accelerationZ = p_i1761_7_ / var9 * 0.1;
    }
    
    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && ((this.shootingEntity != null && this.shootingEntity.isDead) || !this.worldObj.isBlockLoaded(new BlockPos(this)))) {
            this.setDead();
        }
        else {
            super.onUpdate();
            this.setFire(1);
            if (this.inGround) {
                if (this.worldObj.getBlockState(new BlockPos(this.field_145795_e, this.field_145793_f, this.field_145794_g)).getBlock() == this.field_145796_h) {
                    ++this.ticksAlive;
                    if (this.ticksAlive == 600) {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            }
            else {
                ++this.ticksInAir;
            }
            Vec3 var1 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var1, var2);
            var1 = new Vec3(this.posX, this.posY, this.posZ);
            var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (var3 != null) {
                var2 = new Vec3(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }
            Entity var4 = null;
            final List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var6 = 0.0;
            for (int var7 = 0; var7 < var5.size(); ++var7) {
                final Entity var8 = var5.get(var7);
                if (var8.canBeCollidedWith() && (!var8.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
                    final float var9 = 0.3f;
                    final AxisAlignedBB var10 = var8.getEntityBoundingBox().expand(var9, var9, var9);
                    final MovingObjectPosition var11 = var10.calculateIntercept(var1, var2);
                    if (var11 != null) {
                        final double var12 = var1.distanceTo(var11.hitVec);
                        if (var12 < var6 || var6 == 0.0) {
                            var4 = var8;
                            var6 = var12;
                        }
                    }
                }
            }
            if (var4 != null) {
                var3 = new MovingObjectPosition(var4);
            }
            if (var3 != null) {
                this.onImpact(var3);
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float var13 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0 / 3.141592653589793) + 90.0f;
            this.rotationPitch = (float)(Math.atan2(var13, this.motionY) * 180.0 / 3.141592653589793) - 90.0f;
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
            float var14 = this.getMotionFactor();
            if (this.isInWater()) {
                for (int var15 = 0; var15 < 4; ++var15) {
                    final float var16 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * var16, this.posY - this.motionY * var16, this.posZ - this.motionZ * var16, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                var14 = 0.8f;
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= var14;
            this.motionY *= var14;
            this.motionZ *= var14;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }
    
    protected float getMotionFactor() {
        return 0.95f;
    }
    
    protected abstract void onImpact(final MovingObjectPosition p0);
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.field_145795_e);
        tagCompound.setShort("yTile", (short)this.field_145793_f);
        tagCompound.setShort("zTile", (short)this.field_145794_g);
        final ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.field_145796_h);
        tagCompound.setString("inTile", (var2 == null) ? "" : var2.toString());
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.field_145795_e = tagCompund.getShort("xTile");
        this.field_145793_f = tagCompund.getShort("yTile");
        this.field_145794_g = tagCompund.getShort("zTile");
        if (tagCompund.hasKey("inTile", 8)) {
            this.field_145796_h = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.field_145796_h = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.inGround = (tagCompund.getByte("inGround") == 1);
        if (tagCompund.hasKey("direction", 9)) {
            final NBTTagList var2 = tagCompund.getTagList("direction", 6);
            this.motionX = var2.getDouble(0);
            this.motionY = var2.getDouble(1);
            this.motionZ = var2.getDouble(2);
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
        if (this.func_180431_b(source)) {
            return false;
        }
        this.setBeenAttacked();
        if (source.getEntity() != null) {
            final Vec3 var3 = source.getEntity().getLookVec();
            if (var3 != null) {
                this.motionX = var3.xCoord;
                this.motionY = var3.yCoord;
                this.motionZ = var3.zCoord;
                this.accelerationX = this.motionX * 0.1;
                this.accelerationY = this.motionY * 0.1;
                this.accelerationZ = this.motionZ * 0.1;
            }
            if (source.getEntity() instanceof EntityLivingBase) {
                this.shootingEntity = (EntityLivingBase)source.getEntity();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        return 15728880;
    }
}
