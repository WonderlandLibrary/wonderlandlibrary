/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCrit2FX
extends EntityFX {
    float field_174839_a;
    private static final String __OBFID = "CL_00000899";

    protected EntityCrit2FX(World worldIn, double p_i46284_2_, double p_i46284_4_, double p_i46284_6_, double p_i46284_8_, double p_i46284_10_, double p_i46284_12_) {
        this(worldIn, p_i46284_2_, p_i46284_4_, p_i46284_6_, p_i46284_8_, p_i46284_10_, p_i46284_12_, 1.0f);
    }

    protected EntityCrit2FX(World worldIn, double p_i46285_2_, double p_i46285_4_, double p_i46285_6_, double p_i46285_8_, double p_i46285_10_, double p_i46285_12_, float p_i46285_14_) {
        super(worldIn, p_i46285_2_, p_i46285_4_, p_i46285_6_, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += p_i46285_8_ * 0.4;
        this.motionY += p_i46285_10_ * 0.4;
        this.motionZ += p_i46285_12_ * 0.4;
        this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.3f + (double)0.6f);
        this.particleRed = this.particleBlue;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i46285_14_;
        this.field_174839_a = this.particleScale;
        this.particleMaxAge = (int)(6.0 / (Math.random() * 0.8 + 0.6));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i46285_14_);
        this.noClip = false;
        this.setParticleTextureIndex(65);
        this.onUpdate();
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.field_174839_a * var9;
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.particleGreen = (float)((double)this.particleGreen * 0.96);
        this.particleBlue = (float)((double)this.particleBlue * 0.9);
        this.motionX *= (double)0.7f;
        this.motionY *= (double)0.7f;
        this.motionZ *= (double)0.7f;
        this.motionY -= (double)0.02f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002608";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityCrit2FX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }

    public static class MagicFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002609";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntityCrit2FX var16 = new EntityCrit2FX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setRBGColorF(var16.getRedColorF() * 0.3f, var16.getGreenColorF() * 0.8f, var16.getBlueColorF());
            var16.nextTextureIndexX();
            return var16;
        }
    }
}

