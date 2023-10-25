/*
 * Decompiled with CFR 0.152.
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
    private int baseSpellTextureIndex = 128;
    private static final Random RANDOM = new Random();

    protected EntitySpellParticleFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, 0.5 - RANDOM.nextDouble(), d5, 0.5 - RANDOM.nextDouble());
        this.motionY *= (double)0.2f;
        if (d4 == 0.0 && d6 == 0.0) {
            this.motionX *= (double)0.1f;
            this.motionZ *= (double)0.1f;
        }
        this.particleScale *= 0.75f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
    }

    public void setBaseSpellTextureIndex(int n) {
        this.baseSpellTextureIndex = n;
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

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.particleAge + f) / (float)this.particleMaxAge * 32.0f;
        f7 = MathHelper.clamp_float(f7, 0.0f, 1.0f);
        super.renderParticle(worldRenderer, entity, f, f2, f3, f4, f5, f6);
    }

    public static class AmbientMobFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntitySpellParticleFX entitySpellParticleFX = new EntitySpellParticleFX(world, d, d2, d3, d4, d5, d6);
            entitySpellParticleFX.setAlphaF(0.15f);
            entitySpellParticleFX.setRBGColorF((float)d4, (float)d5, (float)d6);
            return entitySpellParticleFX;
        }
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntitySpellParticleFX(world, d, d2, d3, d4, d5, d6);
        }
    }

    public static class InstantFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntitySpellParticleFX entitySpellParticleFX = new EntitySpellParticleFX(world, d, d2, d3, d4, d5, d6);
            entitySpellParticleFX.setBaseSpellTextureIndex(144);
            return entitySpellParticleFX;
        }
    }

    public static class MobFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntitySpellParticleFX entitySpellParticleFX = new EntitySpellParticleFX(world, d, d2, d3, d4, d5, d6);
            entitySpellParticleFX.setRBGColorF((float)d4, (float)d5, (float)d6);
            return entitySpellParticleFX;
        }
    }

    public static class WitchFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntitySpellParticleFX entitySpellParticleFX = new EntitySpellParticleFX(world, d, d2, d3, d4, d5, d6);
            entitySpellParticleFX.setBaseSpellTextureIndex(144);
            float f = world.rand.nextFloat() * 0.5f + 0.35f;
            entitySpellParticleFX.setRBGColorF(1.0f * f, 0.0f * f, 1.0f * f);
            return entitySpellParticleFX;
        }
    }
}

