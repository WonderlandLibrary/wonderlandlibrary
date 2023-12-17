// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticleRedstone extends Particle
{
    float reddustParticleScale;
    
    protected ParticleRedstone(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final float p_i46349_8_, final float p_i46349_9_, final float p_i46349_10_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0f, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }
    
    protected ParticleRedstone(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final float scale, float red, final float green, final float blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        if (red == 0.0f) {
            red = 1.0f;
        }
        final float f = (float)Math.random() * 0.4f + 0.6f;
        this.particleRed = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * red * f;
        this.particleGreen = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * green * f;
        this.particleBlue = ((float)(Math.random() * 0.20000000298023224) + 0.8f) * blue * f;
        this.particleScale *= 0.75f;
        this.particleScale *= scale;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge *= (int)scale;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        this.particleScale = this.reddustParticleScale * f;
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
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
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
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleRedstone(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
        }
    }
}
