/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class ParticleSimpleAnimated
extends Particle {
    private final int textureIdx;
    private final int numAgingFrames;
    private final float yAccel;
    private float field_191239_M = 0.91f;
    private float fadeTargetRed;
    private float fadeTargetGreen;
    private float fadeTargetBlue;
    private boolean fadingColor;

    public ParticleSimpleAnimated(World worldIn, double x, double y, double z, int textureIdxIn, int numFrames, float yAccelIn) {
        super(worldIn, x, y, z);
        this.textureIdx = textureIdxIn;
        this.numAgingFrames = numFrames;
        this.yAccel = yAccelIn;
    }

    public void setColor(int p_187146_1_) {
        float f = (float)((p_187146_1_ & 0xFF0000) >> 16) / 255.0f;
        float f1 = (float)((p_187146_1_ & 0xFF00) >> 8) / 255.0f;
        float f2 = (float)((p_187146_1_ & 0xFF) >> 0) / 255.0f;
        float f3 = 1.0f;
        this.setRBGColorF(f * 1.0f, f1 * 1.0f, f2 * 1.0f);
    }

    public void setColorFade(int rgb) {
        this.fadeTargetRed = (float)((rgb & 0xFF0000) >> 16) / 255.0f;
        this.fadeTargetGreen = (float)((rgb & 0xFF00) >> 8) / 255.0f;
        this.fadeTargetBlue = (float)((rgb & 0xFF) >> 0) / 255.0f;
        this.fadingColor = true;
    }

    @Override
    public boolean isTransparent() {
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
            this.setAlphaF(1.0f - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / (float)this.particleMaxAge);
            if (this.fadingColor) {
                this.particleRed += (this.fadeTargetRed - this.particleRed) * 0.2f;
                this.particleGreen += (this.fadeTargetGreen - this.particleGreen) * 0.2f;
                this.particleBlue += (this.fadeTargetBlue - this.particleBlue) * 0.2f;
            }
        }
        this.setParticleTextureIndex(this.textureIdx + (this.numAgingFrames - 1 - this.particleAge * this.numAgingFrames / this.particleMaxAge));
        this.motionY += (double)this.yAccel;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)this.field_191239_M;
        this.motionY *= (double)this.field_191239_M;
        this.motionZ *= (double)this.field_191239_M;
        if (this.isCollided) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return 0xF000F0;
    }

    protected void func_191238_f(float p_191238_1_) {
        this.field_191239_M = p_191238_1_;
    }
}

