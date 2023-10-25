/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFX
extends Entity {
    protected TextureAtlasSprite particleIcon;
    public static double interpPosY;
    protected float particleGreen;
    protected int particleTextureIndexY;
    protected float particleGravity;
    protected int particleAge;
    protected float particleScale;
    protected float particleBlue;
    protected int particleTextureIndexX;
    protected float particleTextureJitterY;
    protected int particleMaxAge;
    public static double interpPosZ;
    protected float particleTextureJitterX;
    protected float particleAlpha = 1.0f;
    public static double interpPosX;
    protected float particleRed;

    protected EntityFX(World world, double d, double d2, double d3) {
        super(world);
        this.setSize(0.2f, 0.2f);
        this.setPosition(d, d2, d3);
        this.lastTickPosX = this.prevPosX = d;
        this.lastTickPosY = this.prevPosY = d2;
        this.lastTickPosZ = this.prevPosZ = d3;
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0f;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0f;
        this.particleScale = (this.rand.nextFloat() * 0.5f + 0.5f) * 2.0f;
        this.particleMaxAge = (int)(4.0f / (this.rand.nextFloat() * 0.9f + 0.1f));
        this.particleAge = 0;
    }

    public void setParticleIcon(TextureAtlasSprite textureAtlasSprite) {
        int n = this.getFXLayer();
        if (n != 1) {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
        }
        this.particleIcon = textureAtlasSprite;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.motionY -= 0.04 * (double)this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98f;
        this.motionY *= (double)0.98f;
        this.motionZ *= (double)0.98f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }

    public void setParticleTextureIndex(int n) {
        if (this.getFXLayer() != 0) {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        this.particleTextureIndexX = n % 16;
        this.particleTextureIndexY = n / 16;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
    }

    public EntityFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        this(world, d, d2, d3);
        this.motionX = d4 + (Math.random() * 2.0 - 1.0) * (double)0.4f;
        this.motionY = d5 + (Math.random() * 2.0 - 1.0) * (double)0.4f;
        this.motionZ = d6 + (Math.random() * 2.0 - 1.0) * (double)0.4f;
        float f = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double)f2 * (double)f * (double)0.4f;
        this.motionY = this.motionY / (double)f2 * (double)f * (double)0.4f + (double)0.1f;
        this.motionZ = this.motionZ / (double)f2 * (double)f * (double)0.4f;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
    }

    public int getFXLayer() {
        return 0;
    }

    public void nextTextureIndexX() {
        ++this.particleTextureIndexX;
    }

    public float getBlueColorF() {
        return this.particleBlue;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    public EntityFX multipleParticleScaleBy(float f) {
        this.setSize(0.2f * f, 0.2f * f);
        this.particleScale *= f;
        return this;
    }

    public float getRedColorF() {
        return this.particleRed;
    }

    public float getAlpha() {
        return this.particleAlpha;
    }

    public EntityFX multiplyVelocity(float f) {
        this.motionX *= (double)f;
        this.motionY = (this.motionY - (double)0.1f) * (double)f + (double)0.1f;
        this.motionZ *= (double)f;
        return this;
    }

    public float getGreenColorF() {
        return this.particleGreen;
    }

    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = (float)this.particleTextureIndexX / 16.0f;
        float f8 = f7 + 0.0624375f;
        float f9 = (float)this.particleTextureIndexY / 16.0f;
        float f10 = f9 + 0.0624375f;
        float f11 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f7 = this.particleIcon.getMinU();
            f8 = this.particleIcon.getMaxU();
            f9 = this.particleIcon.getMinV();
            f10 = this.particleIcon.getMaxV();
        }
        float f12 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
        float f13 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
        float f14 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
        int n = this.getBrightnessForRender(f);
        int n2 = n >> 16 & 0xFFFF;
        int n3 = n & 0xFFFF;
        worldRenderer.pos(f12 - f2 * f11 - f5 * f11, f13 - f3 * f11, f14 - f4 * f11 - f6 * f11).tex(f8, f10).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 - f2 * f11 + f5 * f11, f13 + f3 * f11, f14 - f4 * f11 + f6 * f11).tex(f8, f9).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * f11 + f5 * f11, f13 + f3 * f11, f14 + f4 * f11 + f6 * f11).tex(f7, f9).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
        worldRenderer.pos(f12 + f2 * f11 - f5 * f11, f13 - f3 * f11, f14 + f4 * f11 - f6 * f11).tex(f7, f10).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(n2, n3).endVertex();
    }

    public void setAlphaF(float f) {
        if (this.particleAlpha == 1.0f && f < 1.0f) {
            Minecraft.getMinecraft().effectRenderer.moveToAlphaLayer(this);
        } else if (this.particleAlpha < 1.0f && f == 1.0f) {
            Minecraft.getMinecraft().effectRenderer.moveToNoAlphaLayer(this);
        }
        this.particleAlpha = f;
    }

    public void setRBGColorF(float f, float f2, float f3) {
        this.particleRed = f;
        this.particleGreen = f2;
        this.particleBlue = f3;
    }
}

