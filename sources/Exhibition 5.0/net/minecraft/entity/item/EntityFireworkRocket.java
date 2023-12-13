// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityFireworkRocket extends Entity
{
    private int fireworkAge;
    private int lifetime;
    private static final String __OBFID = "CL_00001718";
    
    public EntityFireworkRocket(final World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObjectByDataType(8, 5);
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 4096.0;
    }
    
    public EntityFireworkRocket(final World worldIn, final double p_i1763_2_, final double p_i1763_4_, final double p_i1763_6_, final ItemStack p_i1763_8_) {
        super(worldIn);
        this.fireworkAge = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);
        int var9 = 1;
        if (p_i1763_8_ != null && p_i1763_8_.hasTagCompound()) {
            this.dataWatcher.updateObject(8, p_i1763_8_);
            final NBTTagCompound var10 = p_i1763_8_.getTagCompound();
            final NBTTagCompound var11 = var10.getCompoundTag("Fireworks");
            if (var11 != null) {
                var9 += var11.getByte("Flight");
            }
        }
        this.motionX = this.rand.nextGaussian() * 0.001;
        this.motionZ = this.rand.nextGaussian() * 0.001;
        this.motionY = 0.05;
        this.lifetime = 10 * var9 + this.rand.nextInt(6) + this.rand.nextInt(7);
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
        this.motionX *= 1.15;
        this.motionZ *= 1.15;
        this.motionY += 0.04;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
        if (this.fireworkAge == 0 && !this.isSlient()) {
            this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0f, 1.0f);
        }
        ++this.fireworkAge;
        if (this.worldObj.isRemote && this.fireworkAge % 2 < 2) {
            this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3, this.posZ, this.rand.nextGaussian() * 0.05, -this.motionY * 0.5, this.rand.nextGaussian() * 0.05, new int[0]);
        }
        if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
            this.worldObj.setEntityState(this, (byte)17);
            this.setDead();
        }
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 17 && this.worldObj.isRemote) {
            final ItemStack var2 = this.dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound var3 = null;
            if (var2 != null && var2.hasTagCompound()) {
                var3 = var2.getTagCompound().getCompoundTag("Fireworks");
            }
            this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, var3);
        }
        super.handleHealthUpdate(p_70103_1_);
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setInteger("Life", this.fireworkAge);
        tagCompound.setInteger("LifeTime", this.lifetime);
        final ItemStack var2 = this.dataWatcher.getWatchableObjectItemStack(8);
        if (var2 != null) {
            final NBTTagCompound var3 = new NBTTagCompound();
            var2.writeToNBT(var3);
            tagCompound.setTag("FireworksItem", var3);
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.fireworkAge = tagCompund.getInteger("Life");
        this.lifetime = tagCompund.getInteger("LifeTime");
        final NBTTagCompound var2 = tagCompund.getCompoundTag("FireworksItem");
        if (var2 != null) {
            final ItemStack var3 = ItemStack.loadItemStackFromNBT(var2);
            if (var3 != null) {
                this.dataWatcher.updateObject(8, var3);
            }
        }
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        return super.getBrightness(p_70013_1_);
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        return super.getBrightnessForRender(p_70070_1_);
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
