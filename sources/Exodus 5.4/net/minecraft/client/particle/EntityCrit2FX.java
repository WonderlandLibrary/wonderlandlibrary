/*
 * Decompiled with CFR 0.152.
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

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.particleAge + f) / (float)this.particleMaxAge * 32.0f;
        f7 = MathHelper.clamp_float(f7, 0.0f, 1.0f);
        this.particleScale = this.field_174839_a * f7;
        super.renderParticle(worldRenderer, entity, f, f2, f3, f4, f5, f6);
    }

    protected EntityCrit2FX(World world, double d, double d2, double d3, double d4, double d5, double d6, float f) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += d4 * 0.4;
        this.motionY += d5 * 0.4;
        this.motionZ += d6 * 0.4;
        this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.3f + (double)0.6f);
        this.particleRed = this.particleBlue;
        this.particleScale *= 0.75f;
        this.particleScale *= f;
        this.field_174839_a = this.particleScale;
        this.particleMaxAge = (int)(6.0 / (Math.random() * 0.8 + 0.6));
        this.particleMaxAge = (int)((float)this.particleMaxAge * f);
        this.noClip = false;
        this.setParticleTextureIndex(65);
        this.onUpdate();
    }

    protected EntityCrit2FX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        this(world, d, d2, d3, d4, d5, d6, 1.0f);
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityCrit2FX(world, d, d2, d3, d4, d5, d6);
        }
    }

    public static class MagicFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            EntityCrit2FX entityCrit2FX = new EntityCrit2FX(world, d, d2, d3, d4, d5, d6);
            entityCrit2FX.setRBGColorF(entityCrit2FX.getRedColorF() * 0.3f, entityCrit2FX.getGreenColorF() * 0.8f, entityCrit2FX.getBlueColorF());
            entityCrit2FX.nextTextureIndexX();
            return entityCrit2FX;
        }
    }
}

