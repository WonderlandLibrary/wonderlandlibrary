// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticleFlame extends Particle
{
    private final float flameScale;
    
    protected ParticleFlame(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = this.motionX * 0.009999999776482582 + xSpeedIn;
        this.motionY = this.motionY * 0.009999999776482582 + ySpeedIn;
        this.motionZ = this.motionZ * 0.009999999776482582 + zSpeedIn;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.flameScale = this.particleScale;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
        this.setParticleTextureIndex(48);
    }
    
    @Override
    public void move(final double x, final double y, final double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        final float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0f - f * f * 0.5f);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTick) {
        float f = (this.particleAge + partialTick) / this.particleMaxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        final int i = super.getBrightnessForRender(partialTick);
        int j = i & 0xFF;
        final int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0f * 16.0f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
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
            return new ParticleFlame(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
