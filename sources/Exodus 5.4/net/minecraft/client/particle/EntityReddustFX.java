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

public class EntityReddustFX
extends EntityFX {
    float reddustParticleScale;

    protected EntityReddustFX(World world, double d, double d2, double d3, float f, float f2, float f3) {
        this(world, d, d2, d3, 1.0f, f, f2, f3);
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

    protected EntityReddustFX(World world, double d, double d2, double d3, float f, float f2, float f3, float f4) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        if (f2 == 0.0f) {
            f2 = 1.0f;
        }
        float f5 = (float)Math.random() * 0.4f + 0.6f;
        this.particleRed = ((float)(Math.random() * (double)0.2f) + 0.8f) * f2 * f5;
        this.particleGreen = ((float)(Math.random() * (double)0.2f) + 0.8f) * f3 * f5;
        this.particleBlue = ((float)(Math.random() * (double)0.2f) + 0.8f) * f4 * f5;
        this.particleScale *= 0.75f;
        this.particleScale *= f;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * f);
        this.noClip = false;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.particleAge + f) / (float)this.particleMaxAge * 32.0f;
        f7 = MathHelper.clamp_float(f7, 0.0f, 1.0f);
        this.particleScale = this.reddustParticleScale * f7;
        super.renderParticle(worldRenderer, entity, f, f2, f3, f4, f5, f6);
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityReddustFX(world, d, d2, d3, (float)d4, (float)d5, (float)d6);
        }
    }
}

