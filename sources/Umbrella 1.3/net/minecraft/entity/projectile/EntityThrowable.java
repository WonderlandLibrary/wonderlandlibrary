/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import java.util.List;
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

public abstract class EntityThrowable
extends Entity
implements IProjectile {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block field_174853_f;
    protected boolean field_174854_a;
    public int throwableShake;
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    private static final String __OBFID = "CL_00001723";

    public EntityThrowable(World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        return distance < (var3 *= 64.0) * var3;
    }

    public EntityThrowable(World worldIn, EntityLivingBase p_i1777_2_) {
        super(worldIn);
        this.thrower = p_i1777_2_;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(p_i1777_2_.posX, p_i1777_2_.posY + (double)p_i1777_2_.getEyeHeight(), p_i1777_2_.posZ, p_i1777_2_.rotationYaw, p_i1777_2_.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.posY -= (double)0.1f;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        float var3 = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * var3;
        this.motionY = -MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0f * (float)Math.PI) * var3;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0f);
    }

    public EntityThrowable(World worldIn, double p_i1778_2_, double p_i1778_4_, double p_i1778_6_) {
        super(worldIn);
        this.ticksInGround = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(p_i1778_2_, p_i1778_4_, p_i1778_6_);
    }

    protected float func_70182_d() {
        return 1.5f;
    }

    protected float func_70183_g() {
        return 0.0f;
    }

    @Override
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        float var9 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= (double)var9;
        p_70186_3_ /= (double)var9;
        p_70186_5_ /= (double)var9;
        p_70186_1_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_70186_8_;
        this.motionX = p_70186_1_ *= (double)p_70186_7_;
        this.motionY = p_70186_3_ *= (double)p_70186_7_;
        this.motionZ = p_70186_5_ *= (double)p_70186_7_;
        float var10 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70186_3_, var10) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float var7 = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0 / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, var7) * 180.0 / Math.PI);
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
        if (this.field_174854_a) {
            if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.field_174853_f) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
                return;
            }
            this.field_174854_a = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
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
        if (!this.worldObj.isRemote) {
            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var6 = 0.0;
            EntityLivingBase var8 = this.getThrower();
            for (int var9 = 0; var9 < var5.size(); ++var9) {
                double var14;
                Entity var10 = (Entity)var5.get(var9);
                if (!var10.canBeCollidedWith() || var10 == var8 && this.ticksInAir < 5) continue;
                float var11 = 0.3f;
                AxisAlignedBB var12 = var10.getEntityBoundingBox().expand(var11, var11, var11);
                MovingObjectPosition var13 = var12.calculateIntercept(var1, var2);
                if (var13 == null || !((var14 = var1.distanceTo(var13.hitVec)) < var6) && var6 != 0.0) continue;
                var4 = var10;
                var6 = var14;
            }
            if (var4 != null) {
                var3 = new MovingObjectPosition(var4);
            }
        }
        if (var3 != null) {
            if (var3.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(var3.func_178782_a()).getBlock() == Blocks.portal) {
                this.setInPortal();
            } else {
                this.onImpact(var3);
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
        this.rotationPitch = (float)(Math.atan2(this.motionY, var16) * 180.0 / Math.PI);
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
        float var17 = 0.99f;
        float var18 = this.getGravityVelocity();
        if (this.isInWater()) {
            for (int var7 = 0; var7 < 4; ++var7) {
                float var19 = 0.25f;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)var19, this.posY - this.motionY * (double)var19, this.posZ - this.motionZ * (double)var19, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
            var17 = 0.8f;
        }
        this.motionX *= (double)var17;
        this.motionY *= (double)var17;
        this.motionZ *= (double)var17;
        this.motionY -= (double)var18;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected float getGravityVelocity() {
        return 0.03f;
    }

    protected abstract void onImpact(MovingObjectPosition var1);

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.field_174853_f);
        tagCompound.setString("inTile", var2 == null ? "" : var2.toString());
        tagCompound.setByte("shake", (byte)this.throwableShake);
        tagCompound.setByte("inGround", (byte)(this.field_174854_a ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }
        tagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.field_174853_f = tagCompund.hasKey("inTile", 8) ? Block.getBlockFromName(tagCompund.getString("inTile")) : Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        this.throwableShake = tagCompund.getByte("shake") & 0xFF;
        this.field_174854_a = tagCompund.getByte("inGround") == 1;
        this.throwerName = tagCompund.getString("ownerName");
        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }
    }

    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
        }
        return this.thrower;
    }
}

