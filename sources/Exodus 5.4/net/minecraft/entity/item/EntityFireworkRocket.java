/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkRocket
extends Entity {
    private int fireworkAge;
    private int lifetime;

    public EntityFireworkRocket(World world, double d, double d2, double d3, ItemStack itemStack) {
        super(world);
        this.fireworkAge = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(d, d2, d3);
        int n = 1;
        if (itemStack != null && itemStack.hasTagCompound()) {
            this.dataWatcher.updateObject(8, itemStack);
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Fireworks");
            if (nBTTagCompound2 != null) {
                n += nBTTagCompound2.getByte("Flight");
            }
        }
        this.motionX = this.rand.nextGaussian() * 0.001;
        this.motionZ = this.rand.nextGaussian() * 0.001;
        this.motionY = 0.05;
        this.lifetime = 10 * n + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    @Override
    public int getBrightnessForRender(float f) {
        return super.getBrightnessForRender(f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        ItemStack itemStack;
        this.fireworkAge = nBTTagCompound.getInteger("Life");
        this.lifetime = nBTTagCompound.getInteger("LifeTime");
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("FireworksItem");
        if (nBTTagCompound2 != null && (itemStack = ItemStack.loadItemStackFromNBT(nBTTagCompound2)) != null) {
            this.dataWatcher.updateObject(8, itemStack);
        }
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObjectByDataType(8, 5);
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
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
        this.motionX *= 1.15;
        this.motionZ *= 1.15;
        this.motionY += 0.04;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
        if (this.fireworkAge == 0 && !this.isSilent()) {
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
    public float getBrightness(float f) {
        return super.getBrightness(f);
    }

    public EntityFireworkRocket(World world) {
        super(world);
        this.setSize(0.25f, 0.25f);
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        return d < 4096.0;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 17 && this.worldObj.isRemote) {
            ItemStack itemStack = this.dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound nBTTagCompound = null;
            if (itemStack != null && itemStack.hasTagCompound()) {
                nBTTagCompound = itemStack.getTagCompound().getCompoundTag("Fireworks");
            }
            this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nBTTagCompound);
        }
        super.handleStatusUpdate(by);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setInteger("Life", this.fireworkAge);
        nBTTagCompound.setInteger("LifeTime", this.lifetime);
        ItemStack itemStack = this.dataWatcher.getWatchableObjectItemStack(8);
        if (itemStack != null) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            itemStack.writeToNBT(nBTTagCompound2);
            nBTTagCompound.setTag("FireworksItem", nBTTagCompound2);
        }
    }
}

