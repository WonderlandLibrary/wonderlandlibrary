// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge implements LayerRenderer
{
    private static final ResourceLocation LIGHTNING_TEXTURE;
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel;
    private static final String __OBFID = "CL_00002423";
    
    public LayerCreeperCharge(final RenderCreeper p_i46121_1_) {
        this.creeperModel = new ModelCreeper(2.0f);
        this.creeperRenderer = p_i46121_1_;
    }
    
    public void doRenderLayer(final EntityCreeper p_177169_1_, final float p_177169_2_, final float p_177169_3_, final float p_177169_4_, final float p_177169_5_, final float p_177169_6_, final float p_177169_7_, final float p_177169_8_) {
        if (p_177169_1_.getPowered()) {
            GlStateManager.depthMask(!p_177169_1_.isInvisible());
            this.creeperRenderer.bindTexture(LayerCreeperCharge.LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float var9 = p_177169_1_.ticksExisted + p_177169_4_;
            GlStateManager.translate(var9 * 0.01f, var9 * 0.01f, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            final float var10 = 0.5f;
            GlStateManager.color(var10, var10, var10, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(p_177169_1_, p_177169_2_, p_177169_3_, p_177169_5_, p_177169_6_, p_177169_7_, p_177169_8_);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.doRenderLayer((EntityCreeper)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }
}
