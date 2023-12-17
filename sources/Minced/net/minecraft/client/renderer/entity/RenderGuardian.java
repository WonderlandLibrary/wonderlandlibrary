// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGuardian;

public class RenderGuardian extends RenderLiving<EntityGuardian>
{
    private static final ResourceLocation GUARDIAN_TEXTURE;
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE;
    
    public RenderGuardian(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGuardian(), 0.5f);
    }
    
    @Override
    public boolean shouldRender(final EntityGuardian livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.hasTargetedEntity()) {
            final EntityLivingBase entitylivingbase = livingEntity.getTargetedEntity();
            if (entitylivingbase != null) {
                final Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase.height * 0.5, 1.0f);
                final Vec3d vec3d2 = this.getPosition(livingEntity, livingEntity.getEyeHeight(), 1.0f);
                if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Vec3d getPosition(final EntityLivingBase entityLivingBaseIn, final double p_177110_2_, final float p_177110_4_) {
        final double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * p_177110_4_;
        final double d2 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * p_177110_4_;
        final double d3 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * p_177110_4_;
        return new Vec3d(d0, d2, d3);
    }
    
    @Override
    public void doRender(final EntityGuardian entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        final EntityLivingBase entitylivingbase = entity.getTargetedEntity();
        if (entitylivingbase != null) {
            final float f = entity.getAttackAnimationScale(partialTicks);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(RenderGuardian.GUARDIAN_BEAM_TEXTURE);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            final float f2 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            final float f3 = entity.world.getTotalWorldTime() + partialTicks;
            final float f4 = f3 * 0.5f % 1.0f;
            final float f5 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + f5, (float)z);
            final Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase.height * 0.5, partialTicks);
            final Vec3d vec3d2 = this.getPosition(entity, f5, partialTicks);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            final double d0 = vec3d3.length() + 1.0;
            vec3d3 = vec3d3.normalize();
            final float f6 = (float)Math.acos(vec3d3.y);
            final float f7 = (float)Math.atan2(vec3d3.z, vec3d3.x);
            GlStateManager.rotate((1.5707964f + -f7) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f6 * 57.295776f, 1.0f, 0.0f, 0.0f);
            final int i = 1;
            final double d2 = f3 * 0.05 * -1.5;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            final float f8 = f * f;
            final int j = 64 + (int)(f8 * 191.0f);
            final int k = 32 + (int)(f8 * 191.0f);
            final int l = 128 - (int)(f8 * 64.0f);
            final double d3 = 0.2;
            final double d4 = 0.282;
            final double d5 = 0.0 + Math.cos(d2 + 2.356194490192345) * 0.282;
            final double d6 = 0.0 + Math.sin(d2 + 2.356194490192345) * 0.282;
            final double d7 = 0.0 + Math.cos(d2 + 0.7853981633974483) * 0.282;
            final double d8 = 0.0 + Math.sin(d2 + 0.7853981633974483) * 0.282;
            final double d9 = 0.0 + Math.cos(d2 + 3.9269908169872414) * 0.282;
            final double d10 = 0.0 + Math.sin(d2 + 3.9269908169872414) * 0.282;
            final double d11 = 0.0 + Math.cos(d2 + 5.497787143782138) * 0.282;
            final double d12 = 0.0 + Math.sin(d2 + 5.497787143782138) * 0.282;
            final double d13 = 0.0 + Math.cos(d2 + 3.141592653589793) * 0.2;
            final double d14 = 0.0 + Math.sin(d2 + 3.141592653589793) * 0.2;
            final double d15 = 0.0 + Math.cos(d2 + 0.0) * 0.2;
            final double d16 = 0.0 + Math.sin(d2 + 0.0) * 0.2;
            final double d17 = 0.0 + Math.cos(d2 + 1.5707963267948966) * 0.2;
            final double d18 = 0.0 + Math.sin(d2 + 1.5707963267948966) * 0.2;
            final double d19 = 0.0 + Math.cos(d2 + 4.71238898038469) * 0.2;
            final double d20 = 0.0 + Math.sin(d2 + 4.71238898038469) * 0.2;
            final double d21 = 0.0;
            final double d22 = 0.4999;
            final double d23 = -1.0f + f4;
            final double d24 = d0 * 2.5 + d23;
            bufferbuilder.pos(d13, d0, d14).tex(0.4999, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d13, 0.0, d14).tex(0.4999, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d15, 0.0, d16).tex(0.0, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d15, d0, d16).tex(0.0, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d17, d0, d18).tex(0.4999, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d17, 0.0, d18).tex(0.4999, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d19, 0.0, d20).tex(0.0, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d19, d0, d20).tex(0.0, d24).color(j, k, l, 255).endVertex();
            double d25 = 0.0;
            if (entity.ticksExisted % 2 == 0) {
                d25 = 0.5;
            }
            bufferbuilder.pos(d5, d0, d6).tex(0.5, d25 + 0.5).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d7, d0, d8).tex(1.0, d25 + 0.5).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d11, d0, d12).tex(1.0, d25).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d9, d0, d10).tex(0.5, d25).color(j, k, l, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGuardian entity) {
        return RenderGuardian.GUARDIAN_TEXTURE;
    }
    
    static {
        GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
        GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    }
}
