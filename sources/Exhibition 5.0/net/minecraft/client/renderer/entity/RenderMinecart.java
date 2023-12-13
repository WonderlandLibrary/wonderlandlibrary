// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderMinecart extends Render
{
    private static final ResourceLocation minecartTextures;
    protected ModelBase modelMinecart;
    private static final String __OBFID = "CL_00001013";
    
    public RenderMinecart(final RenderManager p_i46155_1_) {
        super(p_i46155_1_);
        this.modelMinecart = new ModelMinecart();
        this.shadowSize = 0.5f;
    }
    
    public void doRender(final EntityMinecart p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(p_76986_1_);
        long var10 = p_76986_1_.getEntityId() * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        final float var11 = (((var10 >> 16 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float var12 = (((var10 >> 20 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float var13 = (((var10 >> 24 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate(var11, var12, var13);
        final double var14 = p_76986_1_.lastTickPosX + (p_76986_1_.posX - p_76986_1_.lastTickPosX) * p_76986_9_;
        final double var15 = p_76986_1_.lastTickPosY + (p_76986_1_.posY - p_76986_1_.lastTickPosY) * p_76986_9_;
        final double var16 = p_76986_1_.lastTickPosZ + (p_76986_1_.posZ - p_76986_1_.lastTickPosZ) * p_76986_9_;
        final double var17 = 0.30000001192092896;
        final Vec3 var18 = p_76986_1_.func_70489_a(var14, var15, var16);
        float var19 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
        if (var18 != null) {
            Vec3 var20 = p_76986_1_.func_70495_a(var14, var15, var16, var17);
            Vec3 var21 = p_76986_1_.func_70495_a(var14, var15, var16, -var17);
            if (var20 == null) {
                var20 = var18;
            }
            if (var21 == null) {
                var21 = var18;
            }
            p_76986_2_ += var18.xCoord - var14;
            p_76986_4_ += (var20.yCoord + var21.yCoord) / 2.0 - var15;
            p_76986_6_ += var18.zCoord - var16;
            Vec3 var22 = var21.addVector(-var20.xCoord, -var20.yCoord, -var20.zCoord);
            if (var22.lengthVector() != 0.0) {
                var22 = var22.normalize();
                p_76986_8_ = (float)(Math.atan2(var22.zCoord, var22.xCoord) * 180.0 / 3.141592653589793);
                var19 = (float)(Math.atan(var22.yCoord) * 73.0);
            }
        }
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_ + 0.375f, (float)p_76986_6_);
        GlStateManager.rotate(180.0f - p_76986_8_, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-var19, 0.0f, 0.0f, 1.0f);
        final float var23 = p_76986_1_.getRollingAmplitude() - p_76986_9_;
        float var24 = p_76986_1_.getDamage() - p_76986_9_;
        if (var24 < 0.0f) {
            var24 = 0.0f;
        }
        if (var23 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(var23) * var23 * var24 / 10.0f * p_76986_1_.getRollingDirection(), 1.0f, 0.0f, 0.0f);
        }
        final int var25 = p_76986_1_.getDisplayTileOffset();
        final IBlockState var26 = p_76986_1_.func_174897_t();
        if (var26.getBlock().getRenderType() != -1) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            final float var27 = 0.75f;
            GlStateManager.scale(var27, var27, var27);
            GlStateManager.translate(-0.5f, (var25 - 8) / 16.0f, 0.5f);
            this.func_180560_a(p_76986_1_, p_76986_9_, var26);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindEntityTexture(p_76986_1_);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render(p_76986_1_, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation getEntityTexture(final EntityMinecart p_110775_1_) {
        return RenderMinecart.minecartTextures;
    }
    
    protected void func_180560_a(final EntityMinecart p_180560_1_, final float p_180560_2_, final IBlockState p_180560_3_) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().func_175016_a(p_180560_3_, p_180560_1_.getBrightness(p_180560_2_));
        GlStateManager.popMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityMinecart)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityMinecart)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        minecartTextures = new ResourceLocation("textures/entity/minecart.png");
    }
}
