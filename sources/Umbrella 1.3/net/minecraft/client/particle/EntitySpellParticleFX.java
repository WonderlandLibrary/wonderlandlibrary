/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySpellParticleFX
extends EntityFX {
    private static final Random field_174848_a = new Random();
    private int baseSpellTextureIndex = 128;
    private static final String __OBFID = "CL_00000926";

    protected EntitySpellParticleFX(World worldIn, double p_i1229_2_, double p_i1229_4_, double p_i1229_6_, double p_i1229_8_, double p_i1229_10_, double p_i1229_12_) {
        super(worldIn, p_i1229_2_, p_i1229_4_, p_i1229_6_, 0.5 - field_174848_a.nextDouble(), p_i1229_10_, 0.5 - field_174848_a.nextDouble());
        this.motionY *= (double)0.2f;
        if (p_i1229_8_ == 0.0 && p_i1229_12_ == 0.0) {
            this.motionX *= (double)0.1f;
            this.motionZ *= (double)0.1f;
        }
        this.particleScale *= 0.75f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        float var9 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
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
        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004;
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

    public void setBaseSpellTextureIndex(int p_70589_1_) {
        this.baseSpellTextureIndex = p_70589_1_;
    }

    public static class AmbientMobFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002585";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntitySpellParticleFX var16 = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setAlphaF(0.15f);
            var16.setRBGColorF((float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
            return var16;
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002582";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }

    public static class InstantFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002584";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntitySpellParticleFX var16 = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setBaseSpellTextureIndex(144);
            return var16;
        }
    }

    public static class MobFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002583";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntitySpellParticleFX var16 = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setRBGColorF((float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
            return var16;
        }
    }

    public static class WitchFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002581";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntitySpellParticleFX var16 = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setBaseSpellTextureIndex(144);
            float var17 = worldIn.rand.nextFloat() * 0.5f + 0.35f;
            var16.setRBGColorF(1.0f * var17, 0.0f * var17, 1.0f * var17);
            return var16;
        }
    }
}

