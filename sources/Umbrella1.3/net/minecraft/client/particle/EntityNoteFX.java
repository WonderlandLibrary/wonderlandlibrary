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

public class EntityNoteFX
extends EntityFX {
    float noteParticleScale;
    private static final String __OBFID = "CL_00000913";

    protected EntityNoteFX(World worldIn, double p_i46353_2_, double p_i46353_4_, double p_i46353_6_, double p_i46353_8_, double p_i46353_10_, double p_i46353_12_) {
        this(worldIn, p_i46353_2_, p_i46353_4_, p_i46353_6_, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0f);
    }

    protected EntityNoteFX(World worldIn, double p_i1217_2_, double p_i1217_4_, double p_i1217_6_, double p_i1217_8_, double p_i1217_10_, double p_i1217_12_, float p_i1217_14_) {
        super(worldIn, p_i1217_2_, p_i1217_4_, p_i1217_6_, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.01f;
        this.motionY *= (double)0.01f;
        this.motionZ *= (double)0.01f;
        this.motionY += 0.2;
        this.particleRed = MathHelper.sin(((float)p_i1217_8_ + 0.0f) * (float)Math.PI * 2.0f) * 0.65f + 0.35f;
        this.particleGreen = MathHelper.sin(((float)p_i1217_8_ + 0.33333334f) * (float)Math.PI * 2.0f) * 0.65f + 0.35f;
        this.particleBlue = MathHelper.sin(((float)p_i1217_8_ + 0.6666667f) * (float)Math.PI * 2.0f) * 0.65f + 0.35f;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i1217_14_;
        this.noteParticleScale = this.particleScale;
        this.particleMaxAge = 6;
        this.noClip = false;
        this.setParticleTextureIndex(64);
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.noteParticleScale * var9;
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
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= (double)0.66f;
        this.motionY *= (double)0.66f;
        this.motionZ *= (double)0.66f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002592";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityNoteFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}

