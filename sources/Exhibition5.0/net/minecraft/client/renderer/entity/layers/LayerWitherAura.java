// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.util.ResourceLocation;

public class LayerWitherAura implements LayerRenderer
{
    private static final ResourceLocation field_177217_a;
    private final RenderWither field_177215_b;
    private final ModelWither field_177216_c;
    private static final String __OBFID = "CL_00002406";
    
    public LayerWitherAura(final RenderWither p_i46105_1_) {
        this.field_177216_c = new ModelWither(0.5f);
        this.field_177215_b = p_i46105_1_;
    }
    
    public void func_177214_a(final EntityWither p_177214_1_, final float p_177214_2_, final float p_177214_3_, final float p_177214_4_, final float p_177214_5_, final float p_177214_6_, final float p_177214_7_, final float p_177214_8_) {
        if (p_177214_1_.isArmored()) {
            GlStateManager.depthMask(!p_177214_1_.isInvisible());
            this.field_177215_b.bindTexture(LayerWitherAura.field_177217_a);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float var9 = p_177214_1_.ticksExisted + p_177214_4_;
            final float var10 = MathHelper.cos(var9 * 0.02f) * 3.0f;
            final float var11 = var9 * 0.01f;
            GlStateManager.translate(var10, var11, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            final float var12 = 0.5f;
            GlStateManager.color(var12, var12, var12, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.field_177216_c.setLivingAnimations(p_177214_1_, p_177214_2_, p_177214_3_, p_177214_4_);
            this.field_177216_c.setModelAttributes(this.field_177215_b.getMainModel());
            this.field_177216_c.render(p_177214_1_, p_177214_2_, p_177214_3_, p_177214_5_, p_177214_6_, p_177214_7_, p_177214_8_);
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
        this.func_177214_a((EntityWither)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        field_177217_a = new ResourceLocation("textures/entity/wither/wither_armor.png");
    }
}
