/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGuardian
extends RenderLiving<EntityGuardian> {
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    int field_177115_a;

    public RenderGuardian(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGuardian(), 0.5f);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    @Override
    public boolean shouldRender(EntityGuardian livingEntity, ICamera camera, double camX, double camY, double camZ) {
        EntityLivingBase entitylivingbase;
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.hasTargetedEntity() && (entitylivingbase = livingEntity.getTargetedEntity()) != null) {
            Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5, 1.0f);
            Vec3 vec31 = this.func_177110_a(livingEntity, livingEntity.getEyeHeight(), 1.0f);
            if (camera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord))) {
                return true;
            }
        }
        return false;
    }

    private Vec3 func_177110_a(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(d0, d1, d2);
    }

    @Override
    public void doRender(EntityGuardian entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a()) {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
        EntityLivingBase entitylivingbase = entity.getTargetedEntity();
        if (entitylivingbase != null) {
            float f2 = entity.func_175477_p(partialTicks);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(GUARDIAN_BEAM_TEXTURE);
            GL11.glTexParameterf(3553, 10242, 10497.0f);
            GL11.glTexParameterf(3553, 10243, 10497.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float f1 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f1, f1);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f22 = (float)entity.worldObj.getTotalWorldTime() + partialTicks;
            float f3 = f22 * 0.5f % 1.0f;
            float f4 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x2, (float)y2 + f4, (float)z2);
            Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5, partialTicks);
            Vec3 vec31 = this.func_177110_a(entity, f4, partialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            double d0 = vec32.lengthVector() + 1.0;
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.yCoord);
            float f6 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
            GlStateManager.rotate((1.5707964f + -f6) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f5 * 57.295776f, 1.0f, 0.0f, 0.0f);
            boolean i2 = true;
            double d1 = (double)f22 * 0.05 * (1.0 - (double)(i2 & true) * 2.5);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f2 * f2;
            int j2 = 64 + (int)(f7 * 240.0f);
            int k2 = 32 + (int)(f7 * 192.0f);
            int l2 = 128 - (int)(f7 * 64.0f);
            double d2 = (double)i2 * 0.2;
            double d3 = d2 * 1.41;
            double d4 = 0.0 + Math.cos(d1 + 2.356194490192345) * d3;
            double d5 = 0.0 + Math.sin(d1 + 2.356194490192345) * d3;
            double d6 = 0.0 + Math.cos(d1 + 0.7853981633974483) * d3;
            double d7 = 0.0 + Math.sin(d1 + 0.7853981633974483) * d3;
            double d8 = 0.0 + Math.cos(d1 + 3.9269908169872414) * d3;
            double d9 = 0.0 + Math.sin(d1 + 3.9269908169872414) * d3;
            double d10 = 0.0 + Math.cos(d1 + 5.497787143782138) * d3;
            double d11 = 0.0 + Math.sin(d1 + 5.497787143782138) * d3;
            double d12 = 0.0 + Math.cos(d1 + Math.PI) * d2;
            double d13 = 0.0 + Math.sin(d1 + Math.PI) * d2;
            double d14 = 0.0 + Math.cos(d1 + 0.0) * d2;
            double d15 = 0.0 + Math.sin(d1 + 0.0) * d2;
            double d16 = 0.0 + Math.cos(d1 + 1.5707963267948966) * d2;
            double d17 = 0.0 + Math.sin(d1 + 1.5707963267948966) * d2;
            double d18 = 0.0 + Math.cos(d1 + 4.71238898038469) * d2;
            double d19 = 0.0 + Math.sin(d1 + 4.71238898038469) * d2;
            double d20 = 0.0;
            double d21 = 0.4999;
            double d22 = -1.0f + f3;
            double d23 = d0 * (0.5 / d2) + d22;
            worldrenderer.pos(d12, d0, d13).tex(0.4999, d23).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d12, 0.0, d13).tex(0.4999, d22).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d14, 0.0, d15).tex(0.0, d22).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d14, d0, d15).tex(0.0, d23).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d16, d0, d17).tex(0.4999, d23).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d16, 0.0, d17).tex(0.4999, d22).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d18, 0.0, d19).tex(0.0, d22).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d18, d0, d19).tex(0.0, d23).color(j2, k2, l2, 255).endVertex();
            double d24 = 0.0;
            if (entity.ticksExisted % 2 == 0) {
                d24 = 0.5;
            }
            worldrenderer.pos(d4, d0, d5).tex(0.5, d24 + 0.5).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d6, d0, d7).tex(1.0, d24 + 0.5).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d10, d0, d11).tex(1.0, d24).color(j2, k2, l2, 255).endVertex();
            worldrenderer.pos(d8, d0, d9).tex(0.5, d24).color(j2, k2, l2, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void preRenderCallback(EntityGuardian entitylivingbaseIn, float partialTickTime) {
        if (entitylivingbaseIn.isElder()) {
            GlStateManager.scale(2.35f, 2.35f, 2.35f);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGuardian entity) {
        return entity.isElder() ? GUARDIAN_ELDER_TEXTURE : GUARDIAN_TEXTURE;
    }
}

