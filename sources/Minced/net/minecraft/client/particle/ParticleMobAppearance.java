// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;

public class ParticleMobAppearance extends Particle
{
    private EntityLivingBase entity;
    
    protected ParticleMobAppearance(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 30;
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.entity == null) {
            final EntityElderGuardian entityelderguardian = new EntityElderGuardian(this.world);
            entityelderguardian.setGhost();
            this.entity = entityelderguardian;
        }
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (this.entity != null) {
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
            final float f = 0.42553192f;
            final float f2 = (this.particleAge + partialTicks) / this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            final float f3 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            GlStateManager.pushMatrix();
            final float f4 = 0.05f + 0.5f * MathHelper.sin(f2 * 3.1415927f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f4);
            GlStateManager.translate(0.0f, 1.8f, 0.0f);
            GlStateManager.rotate(180.0f - entityIn.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(60.0f - 150.0f * f2 - entityIn.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, -1.5f);
            GlStateManager.scale(0.42553192f, 0.42553192f, 0.42553192f);
            this.entity.rotationYaw = 0.0f;
            this.entity.rotationYawHead = 0.0f;
            this.entity.prevRotationYaw = 0.0f;
            this.entity.prevRotationYawHead = 0.0f;
            rendermanager.renderEntity(this.entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, false);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleMobAppearance(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
