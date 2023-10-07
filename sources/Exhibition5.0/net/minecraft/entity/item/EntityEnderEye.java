// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityEnderEye extends Entity
{
    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;
    private static final String __OBFID = "CL_00001716";
    
    public EntityEnderEye(final World worldIn) {
        super(worldIn);
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
    
    public EntityEnderEye(final World worldIn, final double p_i1758_2_, final double p_i1758_4_, final double p_i1758_6_) {
        super(worldIn);
        this.despawnTimer = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
    }
    
    public void func_180465_a(final BlockPos p_180465_1_) {
        final double var2 = p_180465_1_.getX();
        final int var3 = p_180465_1_.getY();
        final double var4 = p_180465_1_.getZ();
        final double var5 = var2 - this.posX;
        final double var6 = var4 - this.posZ;
        final float var7 = MathHelper.sqrt_double(var5 * var5 + var6 * var6);
        if (var7 > 12.0f) {
            this.targetX = this.posX + var5 / var7 * 12.0;
            this.targetZ = this.posZ + var6 / var7 * 12.0;
            this.targetY = this.posY + 8.0;
        }
        else {
            this.targetX = var2;
            this.targetY = var3;
            this.targetZ = var4;
        }
        this.despawnTimer = 0;
        this.shatterOrDrop = (this.rand.nextInt(5) > 0);
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
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        final float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
        this.rotationPitch = (float)(Math.atan2(this.motionY, var1) * 180.0 / 3.141592653589793);
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
        if (!this.worldObj.isRemote) {
            final double var2 = this.targetX - this.posX;
            final double var3 = this.targetZ - this.posZ;
            final float var4 = (float)Math.sqrt(var2 * var2 + var3 * var3);
            final float var5 = (float)Math.atan2(var3, var2);
            double var6 = var1 + (var4 - var1) * 0.0025;
            if (var4 < 1.0f) {
                var6 *= 0.8;
                this.motionY *= 0.8;
            }
            this.motionX = Math.cos(var5) * var6;
            this.motionZ = Math.sin(var5) * var6;
            if (this.posY < this.targetY) {
                this.motionY += (1.0 - this.motionY) * 0.014999999664723873;
            }
            else {
                this.motionY += (-1.0 - this.motionY) * 0.014999999664723873;
            }
        }
        final float var7 = 0.25f;
        if (this.isInWater()) {
            for (int var8 = 0; var8 < 4; ++var8) {
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * var7, this.posY - this.motionY * var7, this.posZ - this.motionZ * var7, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
        }
        else {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * var7 + this.rand.nextDouble() * 0.6 - 0.3, this.posY - this.motionY * var7 - 0.5, this.posZ - this.motionZ * var7 + this.rand.nextDouble() * 0.6 - 0.3, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;
            if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
                this.setDead();
                if (this.shatterOrDrop) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
                }
                else {
                    this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
                }
            }
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        return 15728880;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
