/*
 * Decompiled with CFR 0.152.
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

    @Override
    public float getBrightness(float f) {
        return 1.0f;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = ((float)this.particleAge + f) / (float)this.particleMaxAge;
        this.particleScale = this.lavaParticleScale * (1.0f - f7 * f7);
        super.renderParticle(worldRenderer, entity, f, f2, f3, f4, f5, f6);
    }

    @Override
    public int getBrightnessForRender(float f) {
        float f2 = ((float)this.particleAge + f) / (float)this.particleMaxAge;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        int n = super.getBrightnessForRender(f);
        int n2 = 240;
        int n3 = n >> 16 & 0xFF;
        return n2 | n3 << 16;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        if (this.rand.nextFloat() > f) {
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

    protected EntityLavaFX(World world, double d, double d2, double d3) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
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

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityLavaFX(world, d, d2, d3);
        }
    }
}

