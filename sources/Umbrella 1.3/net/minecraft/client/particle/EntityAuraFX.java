/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntityAuraFX
extends EntityFX {
    private static final String __OBFID = "CL_00000929";

    protected EntityAuraFX(World worldIn, double p_i1232_2_, double p_i1232_4_, double p_i1232_6_, double p_i1232_8_, double p_i1232_10_, double p_i1232_12_) {
        super(worldIn, p_i1232_2_, p_i1232_4_, p_i1232_6_, p_i1232_8_, p_i1232_10_, p_i1232_12_);
        float var14;
        this.particleRed = var14 = this.rand.nextFloat() * 0.1f + 0.2f;
        this.particleGreen = var14;
        this.particleBlue = var14;
        this.setParticleTextureIndex(0);
        this.setSize(0.02f, 0.02f);
        this.particleScale *= this.rand.nextFloat() * 0.6f + 0.5f;
        this.motionX *= (double)0.02f;
        this.motionY *= (double)0.02f;
        this.motionZ *= (double)0.02f;
        this.particleMaxAge = (int)(20.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = true;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99;
        this.motionY *= 0.99;
        this.motionZ *= 0.99;
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002577";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityAuraFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }

    public static class HappyVillagerFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002578";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            EntityAuraFX var16 = new EntityAuraFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setParticleTextureIndex(82);
            var16.setRBGColorF(1.0f, 1.0f, 1.0f);
            return var16;
        }
    }
}

