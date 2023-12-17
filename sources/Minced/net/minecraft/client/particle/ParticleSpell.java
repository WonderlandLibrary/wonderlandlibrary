// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;
import java.util.Random;

public class ParticleSpell extends Particle
{
    private static final Random RANDOM;
    private int baseSpellTextureIndex;
    
    protected ParticleSpell(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1229_8_, final double ySpeed, final double p_i1229_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5 - ParticleSpell.RANDOM.nextDouble(), ySpeed, 0.5 - ParticleSpell.RANDOM.nextDouble());
        this.baseSpellTextureIndex = 128;
        this.motionY *= 0.20000000298023224;
        if (p_i1229_8_ == 0.0 && p_i1229_12_ == 0.0) {
            this.motionX *= 0.10000000149011612;
            this.motionZ *= 0.10000000149011612;
        }
        this.particleScale *= 0.75f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }
    
    @Override
    public boolean shouldDisableDepth() {
        return true;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004;
        this.move(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public void setBaseSpellTextureIndex(final int baseSpellTextureIndexIn) {
        this.baseSpellTextureIndex = baseSpellTextureIndexIn;
    }
    
    static {
        RANDOM = new Random();
    }
    
    public static class AmbientMobFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setAlphaF(0.15f);
            particle.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return particle;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
    
    public static class InstantFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell)particle).setBaseSpellTextureIndex(144);
            return particle;
        }
    }
    
    public static class MobFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return particle;
        }
    }
    
    public static class WitchFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell)particle).setBaseSpellTextureIndex(144);
            final float f = worldIn.rand.nextFloat() * 0.5f + 0.35f;
            particle.setRBGColorF(1.0f * f, 0.0f * f, 1.0f * f);
            return particle;
        }
    }
}
