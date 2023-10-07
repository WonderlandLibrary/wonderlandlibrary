// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;

public class LayerSpiderEyes implements LayerRenderer
{
    private static final ResourceLocation field_177150_a;
    private final RenderSpider field_177149_b;
    private static final String __OBFID = "CL_00002410";
    
    public LayerSpiderEyes(final RenderSpider p_i46109_1_) {
        this.field_177149_b = p_i46109_1_;
    }
    
    public void func_177148_a(final EntitySpider p_177148_1_, final float p_177148_2_, final float p_177148_3_, final float p_177148_4_, final float p_177148_5_, final float p_177148_6_, final float p_177148_7_, final float p_177148_8_) {
        this.field_177149_b.bindTexture(LayerSpiderEyes.field_177150_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        if (p_177148_1_.isInvisible()) {
            GlStateManager.depthMask(false);
        }
        else {
            GlStateManager.depthMask(true);
        }
        final char var9 = '\uf0f0';
        int var10 = var9 % 65536;
        int var11 = var9 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var10 / 1.0f, var11 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.field_177149_b.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
        final int var12 = p_177148_1_.getBrightnessForRender(p_177148_4_);
        var10 = var12 % 65536;
        var11 = var12 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var10 / 1.0f, var11 / 1.0f);
        this.field_177149_b.func_177105_a(p_177148_1_, p_177148_4_);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.func_177148_a((EntitySpider)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        field_177150_a = new ResourceLocation("textures/entity/spider_eyes.png");
    }
}
