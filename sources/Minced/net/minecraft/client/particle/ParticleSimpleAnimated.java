// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSimpleAnimated extends Particle
{
    private final int textureIdx;
    private final int numAgingFrames;
    private final float yAccel;
    private float baseAirFriction;
    private float fadeTargetRed;
    private float fadeTargetGreen;
    private float fadeTargetBlue;
    private boolean fadingColor;
    
    public ParticleSimpleAnimated(final World worldIn, final double x, final double y, final double z, final int textureIdxIn, final int numFrames, final float yAccelIn) {
        super(worldIn, x, y, z);
        this.baseAirFriction = 0.91f;
        this.textureIdx = textureIdxIn;
        this.numAgingFrames = numFrames;
        this.yAccel = yAccelIn;
    }
    
    public void setColor(final int p_187146_1_) {
        final float f = ((p_187146_1_ & 0xFF0000) >> 16) / 255.0f;
        final float f2 = ((p_187146_1_ & 0xFF00) >> 8) / 255.0f;
        final float f3 = ((p_187146_1_ & 0xFF) >> 0) / 255.0f;
        final float f4 = 1.0f;
        this.setRBGColorF(f * 1.0f, f2 * 1.0f, f3 * 1.0f);
    }
    
    public void setColorFade(final int rgb) {
        this.fadeTargetRed = ((rgb & 0xFF0000) >> 16) / 255.0f;
        this.fadeTargetGreen = ((rgb & 0xFF00) >> 8) / 255.0f;
        this.fadeTargetBlue = ((rgb & 0xFF) >> 0) / 255.0f;
        this.fadingColor = true;
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
        if (this.particleAge > this.particleMaxAge / 2) {
            this.setAlphaF(1.0f - (this.particleAge - (float)(this.particleMaxAge / 2)) / this.particleMaxAge);
            if (this.fadingColor) {
                this.particleRed += (this.fadeTargetRed - this.particleRed) * 0.2f;
                this.particleGreen += (this.fadeTargetGreen - this.particleGreen) * 0.2f;
                this.particleBlue += (this.fadeTargetBlue - this.particleBlue) * 0.2f;
            }
        }
        this.setParticleTextureIndex(this.textureIdx + (this.numAgingFrames - 1 - this.particleAge * this.numAgingFrames / this.particleMaxAge));
        this.motionY += this.yAccel;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= this.baseAirFriction;
        this.motionY *= this.baseAirFriction;
        this.motionZ *= this.baseAirFriction;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    @Override
    public int getBrightnessForRender(final float partialTick) {
        return 15728880;
    }
    
    protected void setBaseAirFriction(final float p_191238_1_) {
        this.baseAirFriction = p_191238_1_;
    }
}
