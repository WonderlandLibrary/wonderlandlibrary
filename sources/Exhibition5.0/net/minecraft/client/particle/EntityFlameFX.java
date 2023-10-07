// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityFlameFX extends EntityFX
{
    private float flameScale;
    private static final String __OBFID = "CL_00000907";
    
    protected EntityFlameFX(final World worldIn, final double p_i1209_2_, final double p_i1209_4_, final double p_i1209_6_, final double p_i1209_8_, final double p_i1209_10_, final double p_i1209_12_) {
        super(worldIn, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_, p_i1209_10_, p_i1209_12_);
        this.motionX = this.motionX * 0.009999999776482582 + p_i1209_8_;
        this.motionY = this.motionY * 0.009999999776482582 + p_i1209_10_;
        this.motionZ = this.motionZ * 0.009999999776482582 + p_i1209_12_;
        double var10000 = p_i1209_2_ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        var10000 = p_i1209_4_ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        var10000 = p_i1209_6_ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.flameScale = this.particleScale;
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
        this.noClip = true;
        this.setParticleTextureIndex(48);
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0f - var9 * var9 * 0.5f);
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        float var2 = (this.particleAge + p_70070_1_) / this.particleMaxAge;
        var2 = MathHelper.clamp_float(var2, 0.0f, 1.0f);
        final int var3 = super.getBrightnessForRender(p_70070_1_);
        int var4 = var3 & 0xFF;
        final int var5 = var3 >> 16 & 0xFF;
        var4 += (int)(var2 * 15.0f * 16.0f);
        if (var4 > 240) {
            var4 = 240;
        }
        return var4 | var5 << 16;
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        float var2 = (this.particleAge + p_70013_1_) / this.particleMaxAge;
        var2 = MathHelper.clamp_float(var2, 0.0f, 1.0f);
        final float var3 = super.getBrightness(p_70013_1_);
        return var3 * var2 + (1.0f - var2);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002602";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityFlameFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
