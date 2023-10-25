/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobAppearance
extends EntityFX {
    private EntityLivingBase entity;

    protected MobAppearance(World world, double d, double d2, double d3) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 30;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.entity == null) {
            EntityGuardian entityGuardian = new EntityGuardian(this.worldObj);
            entityGuardian.setElder();
            this.entity = entityGuardian;
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.entity != null) {
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            renderManager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            float f7 = 0.42553192f;
            float f8 = ((float)this.particleAge + f) / (float)this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            float f9 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f9, f9);
            GlStateManager.pushMatrix();
            float f10 = 0.05f + 0.5f * MathHelper.sin(f8 * (float)Math.PI);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f10);
            GlStateManager.translate(0.0f, 1.8f, 0.0f);
            GlStateManager.rotate(180.0f - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(60.0f - 150.0f * f8 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, -1.5f);
            GlStateManager.scale(f7, f7, f7);
            this.entity.prevRotationYaw = 0.0f;
            this.entity.rotationYaw = 0.0f;
            this.entity.prevRotationYawHead = 0.0f;
            this.entity.rotationYawHead = 0.0f;
            renderManager.renderEntityWithPosYaw(this.entity, 0.0, 0.0, 0.0, 0.0f, f);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new MobAppearance(world, d, d2, d3);
        }
    }
}

