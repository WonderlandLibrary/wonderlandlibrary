// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticleCrit extends Particle
{
    float oSize;
    
    protected ParticleCrit(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i46284_8_, final double p_i46284_10_, final double p_i46284_12_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46284_8_, p_i46284_10_, p_i46284_12_, 1.0f);
    }
    
    protected ParticleCrit(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i46285_8_, final double p_i46285_10_, final double p_i46285_12_, final float p_i46285_14_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        this.motionX += p_i46285_8_ * 0.4;
        this.motionY += p_i46285_10_ * 0.4;
        this.motionZ += p_i46285_12_ * 0.4;
        final float f = (float)(Math.random() * 0.30000001192092896 + 0.6000000238418579);
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i46285_14_;
        this.oSize = this.particleScale;
        this.particleMaxAge = (int)(6.0 / (Math.random() * 0.8 + 0.6));
        this.particleMaxAge *= (int)p_i46285_14_;
        this.setParticleTextureIndex(65);
        this.onUpdate();
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        this.particleScale = this.oSize * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.particleGreen *= (float)0.96;
        this.particleBlue *= (float)0.9;
        this.motionX *= 0.699999988079071;
        this.motionY *= 0.699999988079071;
        this.motionZ *= 0.699999988079071;
        this.motionY -= 0.019999999552965164;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class DamageIndicatorFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn + 1.0, zSpeedIn, 1.0f);
            particle.setMaxAge(20);
            particle.setParticleTextureIndex(67);
            return particle;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
    
    public static class MagicFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final Particle particle = new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setRBGColorF(particle.getRedColorF() * 0.3f, particle.getGreenColorF() * 0.8f, particle.getBlueColorF());
            particle.nextTextureIndexX();
            return particle;
        }
    }
}
