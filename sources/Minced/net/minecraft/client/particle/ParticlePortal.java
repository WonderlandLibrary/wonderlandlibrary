// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticlePortal extends Particle
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    
    protected ParticlePortal(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.posX = xCoordIn;
        this.posY = yCoordIn;
        this.posZ = zCoordIn;
        this.portalPosX = this.posX;
        this.portalPosY = this.posY;
        this.portalPosZ = this.posZ;
        final float f = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleScale = this.rand.nextFloat() * 0.2f + 0.5f;
        this.portalParticleScale = this.particleScale;
        this.particleRed = f * 0.9f;
        this.particleGreen = f * 0.3f;
        this.particleBlue = f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
        this.setParticleTextureIndex((int)(Math.random() * 8.0));
    }
    
    @Override
    public void move(final double x, final double y, final double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        f = 1.0f - f;
        f *= f;
        f = 1.0f - f;
        this.particleScale = this.portalParticleScale * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTick) {
        final int i = super.getBrightnessForRender(partialTick);
        float f = this.particleAge / (float)this.particleMaxAge;
        f *= f;
        f *= f;
        final int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        k += (int)(f * 15.0f * 16.0f);
        if (k > 240) {
            k = 240;
        }
        return j | k << 16;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        final float f = this.particleAge / (float)this.particleMaxAge;
        final float f2 = -f + f * f * 2.0f;
        final float f3 = 1.0f - f2;
        this.posX = this.portalPosX + this.motionX * f3;
        this.posY = this.portalPosY + this.motionY * f3 + (1.0f - f);
        this.posZ = this.portalPosZ + this.motionZ * f3;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticlePortal(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
