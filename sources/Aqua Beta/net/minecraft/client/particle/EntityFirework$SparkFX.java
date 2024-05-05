package net.minecraft.client.particle;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public static class EntityFirework.SparkFX
extends EntityFX {
    private int baseTextureIndex = 160;
    private boolean trail;
    private boolean twinkle;
    private final EffectRenderer field_92047_az;
    private float fadeColourRed;
    private float fadeColourGreen;
    private float fadeColourBlue;
    private boolean hasFadeColour;

    public EntityFirework.SparkFX(World p_i46465_1_, double p_i46465_2_, double p_i46465_4_, double p_i46465_6_, double p_i46465_8_, double p_i46465_10_, double p_i46465_12_, EffectRenderer p_i46465_14_) {
        super(p_i46465_1_, p_i46465_2_, p_i46465_4_, p_i46465_6_);
        this.motionX = p_i46465_8_;
        this.motionY = p_i46465_10_;
        this.motionZ = p_i46465_12_;
        this.field_92047_az = p_i46465_14_;
        this.particleScale *= 0.75f;
        this.particleMaxAge = 48 + this.rand.nextInt(12);
        this.noClip = false;
    }

    public void setTrail(boolean trailIn) {
        this.trail = trailIn;
    }

    public void setTwinkle(boolean twinkleIn) {
        this.twinkle = twinkleIn;
    }

    public void setColour(int colour) {
        float f = (float)((colour & 0xFF0000) >> 16) / 255.0f;
        float f1 = (float)((colour & 0xFF00) >> 8) / 255.0f;
        float f2 = (float)((colour & 0xFF) >> 0) / 255.0f;
        float f3 = 1.0f;
        this.setRBGColorF(f * f3, f1 * f3, f2 * f3);
    }

    public void setFadeColour(int faceColour) {
        this.fadeColourRed = (float)((faceColour & 0xFF0000) >> 16) / 255.0f;
        this.fadeColourGreen = (float)((faceColour & 0xFF00) >> 8) / 255.0f;
        this.fadeColourBlue = (float)((faceColour & 0xFF) >> 0) / 255.0f;
        this.hasFadeColour = true;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    public boolean canBePushed() {
        return false;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
            super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        }
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        if (this.particleAge > this.particleMaxAge / 2) {
            this.setAlphaF(1.0f - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / (float)this.particleMaxAge);
            if (this.hasFadeColour) {
                this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2f;
                this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2f;
                this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2f;
            }
        }
        this.setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY -= 0.004;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.91f;
        this.motionY *= (double)0.91f;
        this.motionZ *= (double)0.91f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
        if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
            EntityFirework.SparkFX entityfirework$sparkfx = new EntityFirework.SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.field_92047_az);
            entityfirework$sparkfx.setAlphaF(0.99f);
            entityfirework$sparkfx.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
            entityfirework$sparkfx.particleAge = entityfirework$sparkfx.particleMaxAge / 2;
            if (this.hasFadeColour) {
                entityfirework$sparkfx.hasFadeColour = true;
                entityfirework$sparkfx.fadeColourRed = this.fadeColourRed;
                entityfirework$sparkfx.fadeColourGreen = this.fadeColourGreen;
                entityfirework$sparkfx.fadeColourBlue = this.fadeColourBlue;
            }
            entityfirework$sparkfx.twinkle = this.twinkle;
            this.field_92047_az.addEffect((EntityFX)entityfirework$sparkfx);
        }
    }

    public int getBrightnessForRender(float partialTicks) {
        return 0xF000F0;
    }

    public float getBrightness(float partialTicks) {
        return 1.0f;
    }
}
