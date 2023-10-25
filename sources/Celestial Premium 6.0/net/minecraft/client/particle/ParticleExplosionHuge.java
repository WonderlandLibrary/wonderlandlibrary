/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleExplosionHuge
extends Particle {
    private int timeSinceStart;
    private final int maximumTime = 8;

    protected ParticleExplosionHuge(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
    }

    @Override
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    }

    @Override
    public void onUpdate() {
        for (int i = 0; i < 6; ++i) {
            double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            float f = this.timeSinceStart;
            this.getClass();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (double)(f / 8.0f), 0.0, 0.0, new int[0]);
        }
        ++this.timeSinceStart;
        this.getClass();
        if (this.timeSinceStart == 8) {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new ParticleExplosionHuge(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

