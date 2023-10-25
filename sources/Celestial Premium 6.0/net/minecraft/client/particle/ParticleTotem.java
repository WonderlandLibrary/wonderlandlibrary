/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.world.World;

public class ParticleTotem
extends ParticleSimpleAnimated {
    public ParticleTotem(World p_i47220_1_, double p_i47220_2_, double p_i47220_4_, double p_i47220_6_, double p_i47220_8_, double p_i47220_10_, double p_i47220_12_) {
        super(p_i47220_1_, p_i47220_2_, p_i47220_4_, p_i47220_6_, 176, 8, -0.05f);
        this.motionX = p_i47220_8_;
        this.motionY = p_i47220_10_;
        this.motionZ = p_i47220_12_;
        this.particleScale *= 0.75f;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        if (this.rand.nextInt(4) == 0) {
            this.setRBGColorF(0.6f + this.rand.nextFloat() * 0.2f, 0.6f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        } else {
            this.setRBGColorF(0.1f + this.rand.nextFloat() * 0.2f, 0.4f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
        this.func_191238_f(0.6f);
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new ParticleTotem(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

