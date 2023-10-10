/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntityEnchantmentTableParticleFX
extends EntityFX {
    private float field_70565_a;
    private double coordX;
    private double coordY;
    private double coordZ;

    protected EntityEnchantmentTableParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        this.posX = this.prevPosX = xCoordIn + xSpeedIn;
        this.posY = this.prevPosY = yCoordIn + ySpeedIn;
        this.posZ = this.prevPosZ = zCoordIn + zSpeedIn;
        float f2 = this.rand.nextFloat() * 0.6f + 0.4f;
        this.field_70565_a = this.particleScale = this.rand.nextFloat() * 0.5f + 0.2f;
        this.particleGreen = this.particleBlue = 1.0f * f2;
        this.particleRed = this.particleBlue;
        this.particleGreen *= 0.9f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 26.0 + 1.0 + 224.0));
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        int i2 = super.getBrightnessForRender(partialTicks);
        float f2 = (float)this.particleAge / (float)this.particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        int j2 = i2 & 0xFF;
        int k2 = i2 >> 16 & 0xFF;
        if ((k2 += (int)(f2 * 15.0f * 16.0f)) > 240) {
            k2 = 240;
        }
        return j2 | k2 << 16;
    }

    @Override
    public float getBrightness(float partialTicks) {
        float f2 = super.getBrightness(partialTicks);
        float f1 = (float)this.particleAge / (float)this.particleMaxAge;
        f1 *= f1;
        f1 *= f1;
        return f2 * (1.0f - f1) + f1;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f2 = (float)this.particleAge / (float)this.particleMaxAge;
        f2 = 1.0f - f2;
        float f1 = 1.0f - f2;
        f1 *= f1;
        f1 *= f1;
        this.posX = this.coordX + this.motionX * (double)f2;
        this.posY = this.coordY + this.motionY * (double)f2 - (double)(f1 * 1.2f);
        this.posZ = this.coordZ + this.motionZ * (double)f2;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }

    public static class EnchantmentTable
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new EntityEnchantmentTableParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

