/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
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
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE;
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE;
    int field_177115_a;
    private static final ResourceLocation GUARDIAN_TEXTURE;

    @Override
    public void doRender(EntityGuardian entityGuardian, double d, double d2, double d3, float f, float f2) {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a()) {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }
        super.doRender(entityGuardian, d, d2, d3, f, f2);
        EntityLivingBase entityLivingBase = entityGuardian.getTargetedEntity();
        if (entityLivingBase != null) {
            float f3 = entityGuardian.func_175477_p(f2);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            this.bindTexture(GUARDIAN_BEAM_TEXTURE);
            GL11.glTexParameterf((int)3553, (int)10242, (float)10497.0f);
            GL11.glTexParameterf((int)3553, (int)10243, (float)10497.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float f4 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f4, f4);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f5 = (float)entityGuardian.worldObj.getTotalWorldTime() + f2;
            float f6 = f5 * 0.5f % 1.0f;
            float f7 = entityGuardian.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)d, (float)d2 + f7, (float)d3);
            Vec3 vec3 = this.func_177110_a(entityLivingBase, (double)entityLivingBase.height * 0.5, f2);
            Vec3 vec32 = this.func_177110_a(entityGuardian, f7, f2);
            Vec3 vec33 = vec3.subtract(vec32);
            double d4 = vec33.lengthVector() + 1.0;
            vec33 = vec33.normalize();
            float f8 = (float)Math.acos(vec33.yCoord);
            float f9 = (float)Math.atan2(vec33.zCoord, vec33.xCoord);
            GlStateManager.rotate((1.5707964f + -f9) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f8 * 57.295776f, 1.0f, 0.0f, 0.0f);
            boolean bl = true;
            double d5 = (double)f5 * 0.05 * (1.0 - (double)(bl & true) * 2.5);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f10 = f3 * f3;
            int n = 64 + (int)(f10 * 240.0f);
            int n2 = 32 + (int)(f10 * 192.0f);
            int n3 = 128 - (int)(f10 * 64.0f);
            double d6 = (double)bl * 0.2;
            double d7 = d6 * 1.41;
            double d8 = 0.0 + Math.cos(d5 + 2.356194490192345) * d7;
            double d9 = 0.0 + Math.sin(d5 + 2.356194490192345) * d7;
            double d10 = 0.0 + Math.cos(d5 + 0.7853981633974483) * d7;
            double d11 = 0.0 + Math.sin(d5 + 0.7853981633974483) * d7;
            double d12 = 0.0 + Math.cos(d5 + 3.9269908169872414) * d7;
            double d13 = 0.0 + Math.sin(d5 + 3.9269908169872414) * d7;
            double d14 = 0.0 + Math.cos(d5 + 5.497787143782138) * d7;
            double d15 = 0.0 + Math.sin(d5 + 5.497787143782138) * d7;
            double d16 = 0.0 + Math.cos(d5 + Math.PI) * d6;
            double d17 = 0.0 + Math.sin(d5 + Math.PI) * d6;
            double d18 = 0.0 + Math.cos(d5 + 0.0) * d6;
            double d19 = 0.0 + Math.sin(d5 + 0.0) * d6;
            double d20 = 0.0 + Math.cos(d5 + 1.5707963267948966) * d6;
            double d21 = 0.0 + Math.sin(d5 + 1.5707963267948966) * d6;
            double d22 = 0.0 + Math.cos(d5 + 4.71238898038469) * d6;
            double d23 = 0.0 + Math.sin(d5 + 4.71238898038469) * d6;
            double d24 = 0.0;
            double d25 = 0.4999;
            double d26 = -1.0f + f6;
            double d27 = d4 * (0.5 / d6) + d26;
            worldRenderer.pos(d16, d4, d17).tex(0.4999, d27).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d16, 0.0, d17).tex(0.4999, d26).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d18, 0.0, d19).tex(0.0, d26).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d18, d4, d19).tex(0.0, d27).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d20, d4, d21).tex(0.4999, d27).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d20, 0.0, d21).tex(0.4999, d26).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d22, 0.0, d23).tex(0.0, d26).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d22, d4, d23).tex(0.0, d27).color(n, n2, n3, 255).endVertex();
            double d28 = 0.0;
            if (entityGuardian.ticksExisted % 2 == 0) {
                d28 = 0.5;
            }
            worldRenderer.pos(d8, d4, d9).tex(0.5, d28 + 0.5).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d10, d4, d11).tex(1.0, d28 + 0.5).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d14, d4, d15).tex(1.0, d28).color(n, n2, n3, 255).endVertex();
            worldRenderer.pos(d12, d4, d13).tex(0.5, d28).color(n, n2, n3, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    private Vec3 func_177110_a(EntityLivingBase entityLivingBase, double d, float f) {
        double d2 = entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * (double)f;
        double d3 = d + entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * (double)f;
        double d4 = entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * (double)f;
        return new Vec3(d2, d3, d4);
    }

    @Override
    public boolean shouldRender(EntityGuardian entityGuardian, ICamera iCamera, double d, double d2, double d3) {
        EntityLivingBase entityLivingBase;
        if (super.shouldRender(entityGuardian, iCamera, d, d2, d3)) {
            return true;
        }
        if (entityGuardian.hasTargetedEntity() && (entityLivingBase = entityGuardian.getTargetedEntity()) != null) {
            Vec3 vec3 = this.func_177110_a(entityLivingBase, (double)entityLivingBase.height * 0.5, 1.0f);
            Vec3 vec32 = this.func_177110_a(entityGuardian, entityGuardian.getEyeHeight(), 1.0f);
            if (iCamera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec32.xCoord, vec32.yCoord, vec32.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void preRenderCallback(EntityGuardian entityGuardian, float f) {
        if (entityGuardian.isElder()) {
            GlStateManager.scale(2.35f, 2.35f, 2.35f);
        }
    }

    static {
        GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
        GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
        GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    }

    public RenderGuardian(RenderManager renderManager) {
        super(renderManager, new ModelGuardian(), 0.5f);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGuardian entityGuardian) {
        return entityGuardian.isElder() ? GUARDIAN_ELDER_TEXTURE : GUARDIAN_TEXTURE;
    }
}

