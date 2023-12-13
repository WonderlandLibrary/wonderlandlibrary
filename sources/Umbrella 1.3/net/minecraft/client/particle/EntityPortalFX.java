/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityPortalFX
extends EntityFX {
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    private static final String __OBFID = "CL_00000921";

    protected EntityPortalFX(World worldIn, double p_i46351_2_, double p_i46351_4_, double p_i46351_6_, double p_i46351_8_, double p_i46351_10_, double p_i46351_12_) {
        super(worldIn, p_i46351_2_, p_i46351_4_, p_i46351_6_, p_i46351_8_, p_i46351_10_, p_i46351_12_);
        this.motionX = p_i46351_8_;
        this.motionY = p_i46351_10_;
        this.motionZ = p_i46351_12_;
        this.portalPosX = this.posX = p_i46351_2_;
        this.portalPosY = this.posY = p_i46351_4_;
        this.portalPosZ = this.posZ = p_i46351_6_;
        float var14 = this.rand.nextFloat() * 0.6f + 0.4f;
        this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2f + 0.5f;
        this.particleGreen = this.particleBlue = 1.0f * var14;
        this.particleRed = this.particleBlue;
        this.particleGreen *= 0.3f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 8.0));
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge;
        var9 = 1.0f - var9;
        var9 *= var9;
        var9 = 1.0f - var9;
        this.particleScale = this.portalParticleScale * var9;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        int var2 = super.getBrightnessForRender(p_70070_1_);
        float var3 = (float)this.particleAge / (float)this.particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        int var4 = var2 & 0xFF;
        int var5 = var2 >> 16 & 0xFF;
        if ((var5 += (int)(var3 * 15.0f * 16.0f)) > 240) {
            var5 = 240;
        }
        return var4 | var5 << 16;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        float var2 = super.getBrightness(p_70013_1_);
        float var3 = (float)this.particleAge / (float)this.particleMaxAge;
        var3 = var3 * var3 * var3 * var3;
        return var2 * (1.0f - var3) + var3;
    }

    @Override
    public void onUpdate() {
        float var1;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float var2 = var1 = (float)this.particleAge / (float)this.particleMaxAge;
        var1 = -var1 + var1 * var1 * 2.0f;
        var1 = 1.0f - var1;
        this.posX = this.portalPosX + this.motionX * (double)var1;
        this.posY = this.portalPosY + this.motionY * (double)var1 + (double)(1.0f - var2);
        this.posZ = this.portalPosZ + this.motionZ * (double)var1;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002590";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityPortalFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}

