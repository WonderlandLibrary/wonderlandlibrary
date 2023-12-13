// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
    private Block field_174853_f;
    protected boolean field_174854_a;
    public int throwableShake;
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    private static final String __OBFID = "CL_00001723";
    
    public EntityThrowable(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25f, 0.25f);
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
    
    public EntityThrowable(final World worldIn, final EntityLivingBase p_i1777_2_) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.thrower = p_i1777_2_;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(p_i1777_2_.posX, p_i1777_2_.posY + p_i1777_2_.getEyeHeight(), p_i1777_2_.posZ, p_i1777_2_.rotationYaw, p_i1777_2_.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        final float var3 = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var3;
        this.motionY = -MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0f * 3.1415927f) * var3;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0f);
    }
    
    public EntityThrowable(final World worldIn, final double p_i1778_2_, final double p_i1778_4_, final double p_i1778_6_) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
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
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, final float p_70186_7_, final float p_70186_8_) {
        final float var9 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= var9;
        p_70186_3_ /= var9;
        p_70186_5_ /= var9;
        p_70186_1_ += this.rand.nextGaussian() * 0.007499999832361937 * p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * 0.007499999832361937 * p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * 0.007499999832361937 * p_70186_8_;
        p_70186_1_ *= p_70186_7_;
        p_70186_3_ *= p_70186_7_;
        p_70186_5_ *= p_70186_7_;
        this.motionX = p_70186_1_;
        this.motionY = p_70186_3_;
        this.motionZ = p_70186_5_;
        final float var10 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        final float n = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(Math.atan2(p_70186_3_, var10) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float var7 = MathHelper.sqrt_double(x * x + z * z);
            final float n = (float)(Math.atan2(x, z) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(Math.atan2(y, var7) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
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
            this.motionX *= this.rand.nextFloat() * 0.2f;
            this.motionY *= this.rand.nextFloat() * 0.2f;
            this.motionZ *= this.rand.nextFloat() * 0.2f;
            this.ticksInGround = 0;
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
        if (!this.worldObj.isRemote) {
            Entity var4 = null;
            final List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var6 = 0.0;
            final EntityLivingBase var7 = this.getThrower();
            for (int var8 = 0; var8 < var5.size(); ++var8) {
                final Entity var9 = var5.get(var8);
                if (var9.canBeCollidedWith() && (var9 != var7 || this.ticksInAir >= 5)) {
                    final float var10 = 0.3f;
                    final AxisAlignedBB var11 = var9.getEntityBoundingBox().expand(var10, var10, var10);
                    final MovingObjectPosition var12 = var11.calculateIntercept(var1, var2);
                    if (var12 != null) {
                        final double var13 = var1.distanceTo(var12.hitVec);
                        if (var13 < var6 || var6 == 0.0) {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }
            if (var4 != null) {
                var3 = new MovingObjectPosition(var4);
            }
        }
        if (var3 != null) {
            if (var3.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(var3.getBlockPos()).getBlock() == Blocks.portal) {
                this.setInPortal();
            }
            else {
                this.onImpact(var3);
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        final float var14 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
        this.rotationPitch = (float)(Math.atan2(this.motionY, var14) * 180.0 / 3.141592653589793);
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
        float var15 = 0.99f;
        final float var16 = this.getGravityVelocity();
        if (this.isInWater()) {
            for (int var17 = 0; var17 < 4; ++var17) {
                final float var18 = 0.25f;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * var18, this.posY - this.motionY * var18, this.posZ - this.motionZ * var18, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
            var15 = 0.8f;
        }
        this.motionX *= var15;
        this.motionY *= var15;
        this.motionZ *= var15;
        this.motionY -= var16;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    protected float getGravityVelocity() {
        return 0.03f;
    }
    
    protected abstract void onImpact(final MovingObjectPosition p0);
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        final ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.field_174853_f);
        tagCompound.setString("inTile", (var2 == null) ? "" : var2.toString());
        tagCompound.setByte("shake", (byte)this.throwableShake);
        tagCompound.setByte("inGround", (byte)(this.field_174854_a ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }
        tagCompound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        if (tagCompund.hasKey("inTile", 8)) {
            this.field_174853_f = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.field_174853_f = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.throwableShake = (tagCompund.getByte("shake") & 0xFF);
        this.field_174854_a = (tagCompund.getByte("inGround") == 1);
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
