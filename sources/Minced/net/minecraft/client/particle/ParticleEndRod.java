// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEndRod extends ParticleSimpleAnimated
{
    public ParticleEndRod(final World p_i46580_1_, final double p_i46580_2_, final double p_i46580_4_, final double p_i46580_6_, final double p_i46580_8_, final double p_i46580_10_, final double p_i46580_12_) {
        super(p_i46580_1_, p_i46580_2_, p_i46580_4_, p_i46580_6_, 176, 8, -5.0E-4f);
        this.motionX = p_i46580_8_;
        this.motionY = p_i46580_10_;
        this.motionZ = p_i46580_12_;
        this.particleScale *= 0.75f;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        this.setColorFade(15916745);
    }
    
    @Override
    public void move(final double x, final double y, final double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleEndRod(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
