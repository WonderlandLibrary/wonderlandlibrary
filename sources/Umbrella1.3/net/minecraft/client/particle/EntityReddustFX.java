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

public class EntityReddustFX
extends EntityFX {
    float reddustParticleScale;
    private static final String __OBFID = "CL_00000923";

    protected EntityReddustFX(World worldIn, double p_i46349_2_, double p_i46349_4_, double p_i46349_6_, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        this(worldIn, p_i46349_2_, p_i46349_4_, p_i46349_6_, 1.0f, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected EntityReddustFX(World worldIn, double p_i46350_2_, double p_i46350_4_, double p_i46350_6_, float p_i46350_8_, float p_i46350_9_, float p_i46350_10_, float p_i46350_11_) {
        super(worldIn, p_i46350_2_, p_i46350_4_, p_i46350_6_, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        if (p_i46350_9_ == 0.0f) {
            p_i46350_9_ = 1.0f;
        }
        float var12 = (float)Math.random() * 0.4f + 0.6f;
        this.particleRed = ((float)(Math.random() * (double)0.2f) + 0.8f) * p_i46350_9_ * var12;
        this.particleGreen = ((float)(Math.random() * (double)0.2f) + 0.8f) * p_i46350_10_ * var12;
        this.particleBlue = ((float)(Math.random() * (double)0.2f) + 0.8f) * p_i46350_11_ * var12;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i46350_8_;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i46350_8_);
        this.noClip = false;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0f;
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
        this.motionX *= (double)0.96f;
        this.motionY *= (double)0.96f;
        this.motionZ *= (double)0.96f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002589";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityReddustFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, (float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
        }
    }
}

