/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLavaFX
extends EntityFX {
    private float lavaParticleScale;
    private static final String __OBFID = "CL_00000912";

    protected EntityLavaFX(World worldIn, double p_i1215_2_, double p_i1215_4_, double p_i1215_6_) {
        super(worldIn, p_i1215_2_, p_i1215_4_, p_i1215_6_, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.8f;
        this.motionY *= (double)0.8f;
        this.motionZ *= (double)0.8f;
        this.motionY = this.rand.nextFloat() * 0.4f + 0.05f;
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.particleScale *= this.rand.nextFloat() * 2.0f + 0.2f;
        this.lavaParticleScale = this.particleScale;
        this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
        this.setParticleTextureIndex(49);
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        float var2 = ((float)this.particleAge + p_70070_1_) / (float)this.particleMaxAge;
        var2 = MathHelper.clamp_float(var2, 0.0f, 1.0f);
        int var3 = super.getBrightnessForRender(p_70070_1_);
        int var4 = 240;
        int var5 = var3 >> 16 & 0xFF;
        return var4 | var5 << 16;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return 1.0f;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge;
        this.particleScale = this.lavaParticleScale * (1.0f - var9 * var9);
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
        float var1 = (float)this.particleAge / (float)this.particleMaxAge;
        if (this.rand.nextFloat() > var1) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.999f;
        this.motionY *= (double)0.999f;
        this.motionZ *= (double)0.999f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002595";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityLavaFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}

