// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityPortalFX extends EntityFX
{
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    private static final String __OBFID = "CL_00000921";
    
    protected EntityPortalFX(final World worldIn, final double p_i46351_2_, final double p_i46351_4_, final double p_i46351_6_, final double p_i46351_8_, final double p_i46351_10_, final double p_i46351_12_) {
        super(worldIn, p_i46351_2_, p_i46351_4_, p_i46351_6_, p_i46351_8_, p_i46351_10_, p_i46351_12_);
        this.motionX = p_i46351_8_;
        this.motionY = p_i46351_10_;
        this.motionZ = p_i46351_12_;
        this.posX = p_i46351_2_;
        this.portalPosX = p_i46351_2_;
        this.posY = p_i46351_4_;
        this.portalPosY = p_i46351_4_;
        this.posZ = p_i46351_6_;
        this.portalPosZ = p_i46351_6_;
        final float var14 = this.rand.nextFloat() * 0.6f + 0.4f;
        final float n = this.rand.nextFloat() * 0.2f + 0.5f;
        this.particleScale = n;
        this.portalParticleScale = n;
        final float particleRed = 1.0f * var14;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleGreen *= 0.3f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 8.0));
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge;
        var9 = 1.0f - var9;
        var9 *= var9;
        var9 = 1.0f - var9;
        this.particleScale = this.portalParticleScale * var9;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        final int var2 = super.getBrightnessForRender(p_70070_1_);
        float var3 = this.particleAge / this.particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        final int var4 = var2 & 0xFF;
        int var5 = var2 >> 16 & 0xFF;
        var5 += (int)(var3 * 15.0f * 16.0f);
        if (var5 > 240) {
            var5 = 240;
        }
        return var4 | var5 << 16;
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        final float var2 = super.getBrightness(p_70013_1_);
        float var3 = this.particleAge / this.particleMaxAge;
        var3 *= var3 * var3 * var3;
        return var2 * (1.0f - var3) + var3;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        final float var2;
        float var1 = var2 = this.particleAge / this.particleMaxAge;
        var1 = -var1 + var1 * var1 * 2.0f;
        var1 = 1.0f - var1;
        this.posX = this.portalPosX + this.motionX * var1;
        this.posY = this.portalPosY + this.motionY * var1 + (1.0f - var2);
        this.posZ = this.portalPosZ + this.motionZ * var1;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002590";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityPortalFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
