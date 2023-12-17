// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDragonBreath extends Particle
{
    private final float oSize;
    private boolean hasHitGround;
    
    protected ParticleDragonBreath(final World worldIn, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
        super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.particleRed = MathHelper.nextFloat(this.rand, 0.7176471f, 0.8745098f);
        this.particleGreen = MathHelper.nextFloat(this.rand, 0.0f, 0.0f);
        this.particleBlue = MathHelper.nextFloat(this.rand, 0.8235294f, 0.9764706f);
        this.particleScale *= 0.75f;
        this.oSize = this.particleScale;
        this.particleMaxAge = (int)(20.0 / (this.rand.nextFloat() * 0.8 + 0.2));
        this.hasHitGround = false;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        else {
            this.setParticleTextureIndex(3 * this.particleAge / this.particleMaxAge + 5);
            if (this.onGround) {
                this.motionY = 0.0;
                this.hasHitGround = true;
            }
            if (this.hasHitGround) {
                this.motionY += 0.002;
            }
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1;
                this.motionZ *= 1.1;
            }
            this.motionX *= 0.9599999785423279;
            this.motionZ *= 0.9599999785423279;
            if (this.hasHitGround) {
                this.motionY *= 0.9599999785423279;
            }
        }
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        this.particleScale = this.oSize * MathHelper.clamp((this.particleAge + partialTicks) / this.particleMaxAge * 32.0f, 0.0f, 1.0f);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleDragonBreath(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
