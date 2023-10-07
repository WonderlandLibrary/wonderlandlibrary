// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.util.ResourceLocation;

public class RenderGuardian extends RenderLiving
{
    private static final ResourceLocation field_177114_e;
    private static final ResourceLocation field_177116_j;
    private static final ResourceLocation field_177117_k;
    int field_177115_a;
    private static final String __OBFID = "CL_00002443";
    
    public RenderGuardian(final RenderManager p_i46171_1_) {
        super(p_i46171_1_, new ModelGuardian(), 0.5f);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }
    
    public boolean func_177113_a(final EntityGuardian p_177113_1_, final ICamera p_177113_2_, final double p_177113_3_, final double p_177113_5_, final double p_177113_7_) {
        if (super.func_177104_a(p_177113_1_, p_177113_2_, p_177113_3_, p_177113_5_, p_177113_7_)) {
            return true;
        }
        if (p_177113_1_.func_175474_cn()) {
            final EntityLivingBase var9 = p_177113_1_.func_175466_co();
            if (var9 != null) {
                final Vec3 var10 = this.func_177110_a(var9, var9.height * 0.5, 1.0f);
                final Vec3 var11 = this.func_177110_a(p_177113_1_, p_177113_1_.getEyeHeight(), 1.0f);
                if (p_177113_2_.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(var11.xCoord, var11.yCoord, var11.zCoord, var10.xCoord, var10.yCoord, var10.zCoord))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Vec3 func_177110_a(final EntityLivingBase p_177110_1_, final double p_177110_2_, final float p_177110_4_) {
        final double var5 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * p_177110_4_;
        final double var6 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * p_177110_4_;
        final double var7 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * p_177110_4_;
        return new Vec3(var5, var6, var7);
    }
    
    public void func_177109_a(final EntityGuardian p_177109_1_, final double p_177109_2_, final double p_177109_4_, final double p_177109_6_, final float p_177109_8_, final float p_177109_9_) {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a()) {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }
        super.doRender(p_177109_1_, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
        final EntityLivingBase var10 = p_177109_1_.func_175466_co();
        if (var10 != null) {
            final float var11 = p_177109_1_.func_175477_p(p_177109_9_);
            final Tessellator var12 = Tessellator.getInstance();
            final WorldRenderer var13 = var12.getWorldRenderer();
            this.bindTexture(RenderGuardian.field_177117_k);
            GL11.glTexParameterf(3553, 10242, 10497.0f);
            GL11.glTexParameterf(3553, 10243, 10497.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            final float var14 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var14, var14);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            final float var15 = p_177109_1_.worldObj.getTotalWorldTime() + p_177109_9_;
            final float var16 = var15 * 0.5f % 1.0f;
            final float var17 = p_177109_1_.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + var17, (float)p_177109_6_);
            final Vec3 var18 = this.func_177110_a(var10, var10.height * 0.5, p_177109_9_);
            final Vec3 var19 = this.func_177110_a(p_177109_1_, var17, p_177109_9_);
            Vec3 var20 = var18.subtract(var19);
            final double var21 = var20.lengthVector() + 1.0;
            var20 = var20.normalize();
            final float var22 = (float)Math.acos(var20.yCoord);
            final float var23 = (float)Math.atan2(var20.zCoord, var20.xCoord);
            GlStateManager.rotate((1.5707964f + -var23) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(var22 * 57.295776f, 1.0f, 0.0f, 0.0f);
            final byte var24 = 1;
            final double var25 = var15 * 0.05 * (1.0 - (var24 & 0x1) * 2.5);
            var13.startDrawingQuads();
            final float var26 = var11 * var11;
            var13.func_178961_b(64 + (int)(var26 * 240.0f), 32 + (int)(var26 * 192.0f), 128 - (int)(var26 * 64.0f), 255);
            final double var27 = var24 * 0.2;
            final double var28 = var27 * 1.41;
            final double var29 = 0.0 + Math.cos(var25 + 2.356194490192345) * var28;
            final double var30 = 0.0 + Math.sin(var25 + 2.356194490192345) * var28;
            final double var31 = 0.0 + Math.cos(var25 + 0.7853981633974483) * var28;
            final double var32 = 0.0 + Math.sin(var25 + 0.7853981633974483) * var28;
            final double var33 = 0.0 + Math.cos(var25 + 3.9269908169872414) * var28;
            final double var34 = 0.0 + Math.sin(var25 + 3.9269908169872414) * var28;
            final double var35 = 0.0 + Math.cos(var25 + 5.497787143782138) * var28;
            final double var36 = 0.0 + Math.sin(var25 + 5.497787143782138) * var28;
            final double var37 = 0.0 + Math.cos(var25 + 3.141592653589793) * var27;
            final double var38 = 0.0 + Math.sin(var25 + 3.141592653589793) * var27;
            final double var39 = 0.0 + Math.cos(var25 + 0.0) * var27;
            final double var40 = 0.0 + Math.sin(var25 + 0.0) * var27;
            final double var41 = 0.0 + Math.cos(var25 + 1.5707963267948966) * var27;
            final double var42 = 0.0 + Math.sin(var25 + 1.5707963267948966) * var27;
            final double var43 = 0.0 + Math.cos(var25 + 4.71238898038469) * var27;
            final double var44 = 0.0 + Math.sin(var25 + 4.71238898038469) * var27;
            final double var45 = 0.0;
            final double var46 = 0.4999;
            final double var47 = -1.0f + var16;
            final double var48 = var21 * (0.5 / var27) + var47;
            var13.addVertexWithUV(var37, var21, var38, var46, var48);
            var13.addVertexWithUV(var37, 0.0, var38, var46, var47);
            var13.addVertexWithUV(var39, 0.0, var40, var45, var47);
            var13.addVertexWithUV(var39, var21, var40, var45, var48);
            var13.addVertexWithUV(var41, var21, var42, var46, var48);
            var13.addVertexWithUV(var41, 0.0, var42, var46, var47);
            var13.addVertexWithUV(var43, 0.0, var44, var45, var47);
            var13.addVertexWithUV(var43, var21, var44, var45, var48);
            double var49 = 0.0;
            if (p_177109_1_.ticksExisted % 2 == 0) {
                var49 = 0.5;
            }
            var13.addVertexWithUV(var29, var21, var30, 0.5, var49 + 0.5);
            var13.addVertexWithUV(var31, var21, var32, 1.0, var49 + 0.5);
            var13.addVertexWithUV(var35, var21, var36, 1.0, var49);
            var13.addVertexWithUV(var33, var21, var34, 0.5, var49);
            var12.draw();
            GlStateManager.popMatrix();
        }
    }
    
    protected void func_177112_a(final EntityGuardian p_177112_1_, final float p_177112_2_) {
        if (p_177112_1_.func_175461_cl()) {
            GlStateManager.scale(2.35f, 2.35f, 2.35f);
        }
    }
    
    protected ResourceLocation func_177111_a(final EntityGuardian p_177111_1_) {
        return p_177111_1_.func_175461_cl() ? RenderGuardian.field_177116_j : RenderGuardian.field_177114_e;
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    public boolean func_177104_a(final EntityLiving p_177104_1_, final ICamera p_177104_2_, final double p_177104_3_, final double p_177104_5_, final double p_177104_7_) {
        return this.func_177113_a((EntityGuardian)p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_);
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.func_177112_a((EntityGuardian)p_77041_1_, p_77041_2_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_177111_a((EntityGuardian)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    public boolean func_177071_a(final Entity p_177071_1_, final ICamera p_177071_2_, final double p_177071_3_, final double p_177071_5_, final double p_177071_7_) {
        return this.func_177113_a((EntityGuardian)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
    
    static {
        field_177114_e = new ResourceLocation("textures/entity/guardian.png");
        field_177116_j = new ResourceLocation("textures/entity/guardian_elder.png");
        field_177117_k = new ResourceLocation("textures/entity/guardian_beam.png");
    }
}
