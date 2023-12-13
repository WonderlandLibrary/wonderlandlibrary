/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntityEnchantmentTableParticleFX
extends EntityFX {
    private float field_70565_a;
    private double field_70568_aq;
    private double field_70567_ar;
    private double field_70566_as;
    private static final String __OBFID = "CL_00000902";

    protected EntityEnchantmentTableParticleFX(World worldIn, double p_i1204_2_, double p_i1204_4_, double p_i1204_6_, double p_i1204_8_, double p_i1204_10_, double p_i1204_12_) {
        super(worldIn, p_i1204_2_, p_i1204_4_, p_i1204_6_, p_i1204_8_, p_i1204_10_, p_i1204_12_);
        this.motionX = p_i1204_8_;
        this.motionY = p_i1204_10_;
        this.motionZ = p_i1204_12_;
        this.field_70568_aq = p_i1204_2_;
        this.field_70567_ar = p_i1204_4_;
        this.field_70566_as = p_i1204_6_;
        this.posX = this.prevPosX = p_i1204_2_ + p_i1204_8_;
        this.posY = this.prevPosY = p_i1204_4_ + p_i1204_10_;
        this.posZ = this.prevPosZ = p_i1204_6_ + p_i1204_12_;
        float var14 = this.rand.nextFloat() * 0.6f + 0.4f;
        this.field_70565_a = this.particleScale = this.rand.nextFloat() * 0.5f + 0.2f;
        this.particleGreen = this.particleBlue = 1.0f * var14;
        this.particleRed = this.particleBlue;
        this.particleGreen *= 0.9f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 26.0 + 1.0 + 224.0));
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
        var3 *= var3;
        var3 *= var3;
        return var2 * (1.0f - var3) + var3;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float var1 = (float)this.particleAge / (float)this.particleMaxAge;
        var1 = 1.0f - var1;
        float var2 = 1.0f - var1;
        var2 *= var2;
        var2 *= var2;
        this.posX = this.field_70568_aq + this.motionX * (double)var1;
        this.posY = this.field_70567_ar + this.motionY * (double)var1 - (double)(var2 * 1.2f);
        this.posZ = this.field_70566_as + this.motionZ * (double)var1;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }

    public static class EnchantmentTable
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002605";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityEnchantmentTableParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}

