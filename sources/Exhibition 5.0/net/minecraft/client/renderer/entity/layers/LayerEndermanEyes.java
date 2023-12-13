// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.util.ResourceLocation;

public class LayerEndermanEyes implements LayerRenderer
{
    private static final ResourceLocation field_177203_a;
    private final RenderEnderman field_177202_b;
    private static final String __OBFID = "CL_00002418";
    
    public LayerEndermanEyes(final RenderEnderman p_i46117_1_) {
        this.field_177202_b = p_i46117_1_;
    }
    
    public void func_177201_a(final EntityEnderman p_177201_1_, final float p_177201_2_, final float p_177201_3_, final float p_177201_4_, final float p_177201_5_, final float p_177201_6_, final float p_177201_7_, final float p_177201_8_) {
        this.field_177202_b.bindTexture(LayerEndermanEyes.field_177203_a);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableLighting();
        if (p_177201_1_.isInvisible()) {
            GlStateManager.depthMask(false);
        }
        else {
            GlStateManager.depthMask(true);
        }
        final char var9 = '\uf0f0';
        final int var10 = var9 % 65536;
        final int var11 = var9 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var10 / 1.0f, var11 / 1.0f);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.field_177202_b.getMainModel().render(p_177201_1_, p_177201_2_, p_177201_3_, p_177201_5_, p_177201_6_, p_177201_7_, p_177201_8_);
        this.field_177202_b.func_177105_a(p_177201_1_, p_177201_4_);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.func_177201_a((EntityEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        field_177203_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    }
}
