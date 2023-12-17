// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSpit extends ParticleExplosion
{
    protected ParticleSpit(final World p_i47221_1_, final double p_i47221_2_, final double p_i47221_4_, final double p_i47221_6_, final double p_i47221_8_, final double p_i47221_10_, final double p_i47221_12_) {
        super(p_i47221_1_, p_i47221_2_, p_i47221_4_, p_i47221_6_, p_i47221_8_, p_i47221_10_, p_i47221_12_);
        this.particleGravity = 0.5f;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY -= 0.004 + 0.04 * this.particleGravity;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleSpit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
