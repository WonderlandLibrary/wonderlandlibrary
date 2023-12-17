// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleTotem extends ParticleSimpleAnimated
{
    public ParticleTotem(final World p_i47220_1_, final double p_i47220_2_, final double p_i47220_4_, final double p_i47220_6_, final double p_i47220_8_, final double p_i47220_10_, final double p_i47220_12_) {
        super(p_i47220_1_, p_i47220_2_, p_i47220_4_, p_i47220_6_, 176, 8, -0.05f);
        this.motionX = p_i47220_8_;
        this.motionY = p_i47220_10_;
        this.motionZ = p_i47220_12_;
        this.particleScale *= 0.75f;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        if (this.rand.nextInt(4) == 0) {
            this.setRBGColorF(0.6f + this.rand.nextFloat() * 0.2f, 0.6f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
        else {
            this.setRBGColorF(0.1f + this.rand.nextFloat() * 0.2f, 0.4f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
        this.setBaseAirFriction(0.6f);
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleTotem(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
