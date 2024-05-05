package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySnowShovelFX
extends EntityFX {
    float snowDigParticleScale;

    protected EntitySnowShovelFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1.0f);
    }

    protected EntitySnowShovelFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float p_i1228_14_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        this.motionX += xSpeedIn;
        this.motionY += ySpeedIn;
        this.motionZ += zSpeedIn;
        this.particleGreen = this.particleBlue = 1.0f - (float)(Math.random() * (double)0.3f);
        this.particleRed = this.particleBlue;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i1228_14_;
        this.snowDigParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i1228_14_);
        this.noClip = false;
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0f;
        f = MathHelper.clamp_float((float)f, (float)0.0f, (float)1.0f);
        this.particleScale = this.snowDigParticleScale * f;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.99f;
        this.motionY *= (double)0.99f;
        this.motionZ *= (double)0.99f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }
}
