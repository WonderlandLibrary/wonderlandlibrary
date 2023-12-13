// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityReddustFX extends EntityFX
{
    float reddustParticleScale;
    private static final String __OBFID = "CL_00000923";
    
    protected EntityReddustFX(final World worldIn, final double p_i46349_2_, final double p_i46349_4_, final double p_i46349_6_, final float p_i46349_8_, final float p_i46349_9_, final float p_i46349_10_) {
        this(worldIn, p_i46349_2_, p_i46349_4_, p_i46349_6_, 1.0f, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }
    
    protected EntityReddustFX(final World worldIn, final double p_i46350_2_, final double p_i46350_4_, final double p_i46350_6_, final float p_i46350_8_, float p_i46350_9_, final float p_i46350_10_, final float p_i46350_11_) {
        super(worldIn, p_i46350_2_, p_i46350_4_, p_i46350_6_, 0.0, 0.0, 0.0);
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        if (p_i46350_9_ == 0.0f) {
            p_i46350_9_ = 1.0f;
        }
        final float var12 = (float)Math.random() * 0.4f + 0.6f;
        this.particleRed = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * p_i46350_9_ * var12;
        this.particleGreen = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * p_i46350_10_ * var12;
        this.particleBlue = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * p_i46350_11_ * var12;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i46350_8_;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge *= (int)p_i46350_8_;
        this.noClip = false;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.reddustParticleScale * var9;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
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
        private static final String __OBFID = "CL_00002589";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityReddustFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, (float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
        }
    }
}
