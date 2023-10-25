/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntityAuraFX
extends EntityFX {
    protected EntityAuraFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, d4, d5, d6);
        float f;
        this.particleRed = f = this.rand.nextFloat() * 0.1f + 0.2f;
        this.particleGreen = f;
        this.particleBlue = f;
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
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityAuraFX(world, d, d2, d3, d4, d5, d6);
        }
    }

    public static class HappyVillagerFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntityAuraFX entityAuraFX = new EntityAuraFX(world, d, d2, d3, d4, d5, d6);
            entityAuraFX.setParticleTextureIndex(82);
            entityAuraFX.setRBGColorF(1.0f, 1.0f, 1.0f);
            return entityAuraFX;
        }
    }
}

