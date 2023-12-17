// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEnchantmentTable extends Particle
{
    private final float oSize;
    private final double coordX;
    private final double coordY;
    private final double coordZ;
    
    protected ParticleEnchantmentTable(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        this.prevPosX = xCoordIn + xSpeedIn;
        this.prevPosY = yCoordIn + ySpeedIn;
        this.prevPosZ = zCoordIn + zSpeedIn;
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.posZ = this.prevPosZ;
        final float f = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleScale = this.rand.nextFloat() * 0.5f + 0.2f;
        this.oSize = this.particleScale;
        this.particleRed = 0.9f * f;
        this.particleGreen = 0.9f * f;
        this.particleBlue = f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
        this.setParticleTextureIndex((int)(Math.random() * 26.0 + 1.0 + 224.0));
    }
    
    @Override
    public void move(final double x, final double y, final double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
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
        float f = this.particleAge / (float)this.particleMaxAge;
        f = 1.0f - f;
        float f2 = 1.0f - f;
        f2 *= f2;
        f2 *= f2;
        this.posX = this.coordX + this.motionX * f;
        this.posY = this.coordY + this.motionY * f - f2 * 1.2f;
        this.posZ = this.coordZ + this.motionZ * f;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }
    
    public static class EnchantmentTable implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleEnchantmentTable(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
