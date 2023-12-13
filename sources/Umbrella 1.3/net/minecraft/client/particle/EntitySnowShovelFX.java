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

public class EntitySnowShovelFX
extends EntityFX {
    float snowDigParticleScale;
    private static final String __OBFID = "CL_00000925";

    protected EntitySnowShovelFX(World worldIn, double p_i1227_2_, double p_i1227_4_, double p_i1227_6_, double p_i1227_8_, double p_i1227_10_, double p_i1227_12_) {
        this(worldIn, p_i1227_2_, p_i1227_4_, p_i1227_6_, p_i1227_8_, p_i1227_10_, p_i1227_12_, 1.0f);
    }

    protected EntitySnowShovelFX(World worldIn, double p_i1228_2_, double p_i1228_4_, double p_i1228_6_, double p_i1228_8_, double p_i1228_10_, double p_i1228_12_, float p_i1228_14_) {
        super(worldIn, p_i1228_2_, p_i1228_4_, p_i1228_6_, p_i1228_8_, p_i1228_10_, p_i1228_12_);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += p_i1228_8_;
        this.motionY += p_i1228_10_;
        this.motionZ += p_i1228_12_;
        this.particleGreen = this.particleBlue = 1.0f - (float)(Math.random() * (double)0.3f);
        this.particleRed = this.particleBlue;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i1228_14_;
        this.snowDigParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i1228_14_);
        this.noClip = false;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.snowDigParticleScale * var9;
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
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.99f;
        this.motionY *= (double)0.99f;
        this.motionZ *= (double)0.99f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002586";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntitySnowShovelFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}

