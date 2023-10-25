/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.world.World;

public class ParticleSpit
extends ParticleExplosion {
    protected ParticleSpit(World p_i47221_1_, double p_i47221_2_, double p_i47221_4_, double p_i47221_6_, double p_i47221_8_, double p_i47221_10_, double p_i47221_12_) {
        super(p_i47221_1_, p_i47221_2_, p_i47221_4_, p_i47221_6_, p_i47221_8_, p_i47221_10_, p_i47221_12_);
        this.particleGravity = 0.5f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY -= 0.004 + 0.04 * (double)this.particleGravity;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new ParticleSpit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

