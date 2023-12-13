/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGuardian
extends RenderLiving {
    private static final ResourceLocation field_177114_e = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation field_177116_j = new ResourceLocation("textures/entity/guardian_elder.png");
    private static final ResourceLocation field_177117_k = new ResourceLocation("textures/entity/guardian_beam.png");
    int field_177115_a;
    private static final String __OBFID = "CL_00002443";

    public RenderGuardian(RenderManager p_i46171_1_) {
        super(p_i46171_1_, new ModelGuardian(), 0.5f);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    public boolean func_177113_a(EntityGuardian p_177113_1_, ICamera p_177113_2_, double p_177113_3_, double p_177113_5_, double p_177113_7_) {
        EntityLivingBase var9;
        if (super.func_177104_a(p_177113_1_, p_177113_2_, p_177113_3_, p_177113_5_, p_177113_7_)) {
            return true;
        }
        if (p_177113_1_.func_175474_cn() && (var9 = p_177113_1_.func_175466_co()) != null) {
            Vec3 var10 = this.func_177110_a(var9, (double)var9.height * 0.5, 1.0f);
            Vec3 var11 = this.func_177110_a(p_177113_1_, p_177113_1_.getEyeHeight(), 1.0f);
            if (p_177113_2_.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(var11.xCoord, var11.yCoord, var11.zCoord, var10.xCoord, var10.yCoord, var10.zCoord))) {
                return true;
            }
        }
        return false;
    }

    private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_) {
        double var5 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * (double)p_177110_4_;
        double var7 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * (double)p_177110_4_;
        double var9 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(var5, var7, var9);
    }

    public void func_177109_a(EntityGuardian p_177109_1_, double p_177109_2_, double p_177109_4_, double p_177109_6_, float p_177109_8_, float p_177109_9_) {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a()) {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }
        super.doRender(p_177109_1_, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
        EntityLivingBase var10 = p_177109_1_.func_175466_co();
        if (var10 != null) {
            float var11 = p_177109_1_.func_175477_p(p_177109_9_);
            Tessellator var12 = Tessellator.getInstance();
            WorldRenderer var13 = var12.getWorldRenderer();
            this.bindTexture(field_177117_k);
            GL11.glTexParameterf((int)3553, (int)10242, (float)10497.0f);
            GL11.glTexParameterf((int)3553, (int)10243, (float)10497.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float var14 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var14, var14);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float var15 = (float)p_177109_1_.worldObj.getTotalWorldTime() + p_177109_9_;
            float var16 = var15 * 0.5f % 1.0f;
            float var17 = p_177109_1_.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + var17, (float)p_177109_6_);
            Vec3 var18 = this.func_177110_a(var10, (double)var10.height * 0.5, p_177109_9_);
            Vec3 var19 = this.func_177110_a(p_177109_1_, var17, p_177109_9_);
            Vec3 var20 = var18.subtract(var19);
            double var21 = var20.lengthVector() + 1.0;
            var20 = var20.normalize();
            float var23 = (float)Math.acos(var20.yCoord);
            float var24 = (float)Math.atan2(var20.zCoord, var20.xCoord);
            GlStateManager.rotate((1.5707964f + -var24) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(var23 * 57.295776f, 1.0f, 0.0f, 0.0f);
            boolean var25 = true;
            double var26 = (double)var15 * 0.05 * (1.0 - (double)(var25 & true) * 2.5);
            var13.startDrawingQuads();
            float var28 = var11 * var11;
            var13.func_178961_b(64 + (int)(var28 * 240.0f), 32 + (int)(var28 * 192.0f), 128 - (int)(var28 * 64.0f), 255);
            double var29 = (double)var25 * 0.2;
            double var31 = var29 * 1.41;
            double var33 = 0.0 + Math.cos(var26 + 2.356194490192345) * var31;
            double var35 = 0.0 + Math.sin(var26 + 2.356194490192345) * var31;
            double var37 = 0.0 + Math.cos(var26 + 0.7853981633974483) * var31;
            double var39 = 0.0 + Math.sin(var26 + 0.7853981633974483) * var31;
            double var41 = 0.0 + Math.cos(var26 + 3.9269908169872414) * var31;
            double var43 = 0.0 + Math.sin(var26 + 3.9269908169872414) * var31;
            double var45 = 0.0 + Math.cos(var26 + 5.497787143782138) * var31;
            double var47 = 0.0 + Math.sin(var26 + 5.497787143782138) * var31;
            double var49 = 0.0 + Math.cos(var26 + Math.PI) * var29;
            double var51 = 0.0 + Math.sin(var26 + Math.PI) * var29;
            double var53 = 0.0 + Math.cos(var26 + 0.0) * var29;
            double var55 = 0.0 + Math.sin(var26 + 0.0) * var29;
            double var57 = 0.0 + Math.cos(var26 + 1.5707963267948966) * var29;
            double var59 = 0.0 + Math.sin(var26 + 1.5707963267948966) * var29;
            double var61 = 0.0 + Math.cos(var26 + 4.71238898038469) * var29;
            double var63 = 0.0 + Math.sin(var26 + 4.71238898038469) * var29;
            double var67 = 0.0;
            double var69 = 0.4999;
            double var71 = -1.0f + var16;
            double var73 = var21 * (0.5 / var29) + var71;
            var13.addVertexWithUV(var49, var21, var51, var69, var73);
            var13.addVertexWithUV(var49, 0.0, var51, var69, var71);
            var13.addVertexWithUV(var53, 0.0, var55, var67, var71);
            var13.addVertexWithUV(var53, var21, var55, var67, var73);
            var13.addVertexWithUV(var57, var21, var59, var69, var73);
            var13.addVertexWithUV(var57, 0.0, var59, var69, var71);
            var13.addVertexWithUV(var61, 0.0, var63, var67, var71);
            var13.addVertexWithUV(var61, var21, var63, var67, var73);
            double var75 = 0.0;
            if (p_177109_1_.ticksExisted % 2 == 0) {
                var75 = 0.5;
            }
            var13.addVertexWithUV(var33, var21, var35, 0.5, var75 + 0.5);
            var13.addVertexWithUV(var37, var21, var39, 1.0, var75 + 0.5);
            var13.addVertexWithUV(var45, var21, var47, 1.0, var75);
            var13.addVertexWithUV(var41, var21, var43, 0.5, var75);
            var12.draw();
            GlStateManager.popMatrix();
        }
    }

    protected void func_177112_a(EntityGuardian p_177112_1_, float p_177112_2_) {
        if (p_177112_1_.func_175461_cl()) {
            GlStateManager.scale(2.35f, 2.35f, 2.35f);
        }
    }

    protected ResourceLocation func_177111_a(EntityGuardian p_177111_1_) {
        return p_177111_1_.func_175461_cl() ? field_177116_j : field_177114_e;
    }

    @Override
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    public boolean func_177104_a(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_) {
        return this.func_177113_a((EntityGuardian)p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.func_177112_a((EntityGuardian)p_77041_1_, p_77041_2_);
    }

    @Override
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_177111_a((EntityGuardian)p_110775_1_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    public boolean func_177071_a(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return this.func_177113_a((EntityGuardian)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
}

