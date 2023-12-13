// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.client.renderer.entity.RenderMooshroom;

public class LayerMooshroomMushroom implements LayerRenderer
{
    private final RenderMooshroom field_177205_a;
    private static final String __OBFID = "CL_00002415";
    
    public LayerMooshroomMushroom(final RenderMooshroom p_i46114_1_) {
        this.field_177205_a = p_i46114_1_;
    }
    
    public void func_177204_a(final EntityMooshroom p_177204_1_, final float p_177204_2_, final float p_177204_3_, final float p_177204_4_, final float p_177204_5_, final float p_177204_6_, final float p_177204_7_, final float p_177204_8_) {
        if (!p_177204_1_.isChild() && !p_177204_1_.isInvisible()) {
            final BlockRendererDispatcher var9 = Minecraft.getMinecraft().getBlockRendererDispatcher();
            this.field_177205_a.bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.enableCull();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f, -1.0f, 1.0f);
            GlStateManager.translate(0.2f, 0.35f, 0.5f);
            GlStateManager.rotate(42.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            var9.func_175016_a(Blocks.red_mushroom.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.1f, 0.0f, -0.6f);
            GlStateManager.rotate(42.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            var9.func_175016_a(Blocks.red_mushroom.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            ((ModelQuadruped)this.field_177205_a.getMainModel()).head.postRender(0.0625f);
            GlStateManager.scale(1.0f, -1.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.7f, -0.2f);
            GlStateManager.rotate(12.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            var9.func_175016_a(Blocks.red_mushroom.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.func_177204_a((EntityMooshroom)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
