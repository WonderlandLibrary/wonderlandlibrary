package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
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

    protected MobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 30;
    }

    public int getFXLayer() {
        return 3;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.entity == null) {
            EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
            entityguardian.setElder();
            this.entity = entityguardian;
        }
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (this.entity != null) {
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            float f = 0.42553192f;
            float f1 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc((int)770, (int)771);
            float f2 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)f2, (float)f2);
            GlStateManager.pushMatrix();
            float f3 = 0.05f + 0.5f * MathHelper.sin((float)(f1 * (float)Math.PI));
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)f3);
            GlStateManager.translate((float)0.0f, (float)1.8f, (float)0.0f);
            GlStateManager.rotate((float)(180.0f - entityIn.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(60.0f - 150.0f * f1 - entityIn.rotationPitch), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-0.4f, (float)-1.5f);
            GlStateManager.scale((float)f, (float)f, (float)f);
            this.entity.prevRotationYaw = 0.0f;
            this.entity.rotationYaw = 0.0f;
            this.entity.prevRotationYawHead = 0.0f;
            this.entity.rotationYawHead = 0.0f;
            rendermanager.renderEntityWithPosYaw((Entity)this.entity, 0.0, 0.0, 0.0, 0.0f, partialTicks);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
}
