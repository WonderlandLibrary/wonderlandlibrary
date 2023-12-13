// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;

public class MobAppearance extends EntityFX
{
    private EntityLivingBase field_174844_a;
    private static final String __OBFID = "CL_00002594";
    
    protected MobAppearance(final World worldIn, final double p_i46283_2_, final double p_i46283_4_, final double p_i46283_6_) {
        super(worldIn, p_i46283_2_, p_i46283_4_, p_i46283_6_, 0.0, 0.0, 0.0);
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
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
        if (this.field_174844_a == null) {
            final EntityGuardian var1 = new EntityGuardian(this.worldObj);
            var1.func_175465_cm();
            this.field_174844_a = var1;
        }
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        if (this.field_174844_a != null) {
            final RenderManager var9 = Minecraft.getMinecraft().getRenderManager();
            var9.func_178628_a(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            final float var10 = 0.42553192f;
            final float var11 = (this.particleAge + p_180434_3_) / this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            final float var12 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var12, var12);
            GlStateManager.pushMatrix();
            final float var13 = 0.05f + 0.5f * MathHelper.sin(var11 * 3.1415927f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, var13);
            GlStateManager.translate(0.0f, 1.8f, 0.0f);
            GlStateManager.rotate(180.0f - p_180434_2_.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(60.0f - 150.0f * var11 - p_180434_2_.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, -1.5f);
            GlStateManager.scale(var10, var10, var10);
            final EntityLivingBase field_174844_a = this.field_174844_a;
            final EntityLivingBase field_174844_a2 = this.field_174844_a;
            final float n = 0.0f;
            field_174844_a2.prevRotationYaw = n;
            field_174844_a.rotationYaw = n;
            final EntityLivingBase field_174844_a3 = this.field_174844_a;
            final EntityLivingBase field_174844_a4 = this.field_174844_a;
            final float n2 = 0.0f;
            field_174844_a4.prevRotationYawHead = n2;
            field_174844_a3.rotationYawHead = n2;
            var9.renderEntityWithPosYaw(this.field_174844_a, 0.0, 0.0, 0.0, 0.0f, p_180434_3_);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002593";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new MobAppearance(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
