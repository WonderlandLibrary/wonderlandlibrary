// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool implements LayerRenderer
{
    private static final ResourceLocation TEXTURE;
    private final RenderSheep sheepRenderer;
    private final ModelSheep1 sheepModel;
    private static final String __OBFID = "CL_00002413";
    
    public LayerSheepWool(final RenderSheep p_i46112_1_) {
        this.sheepModel = new ModelSheep1();
        this.sheepRenderer = p_i46112_1_;
    }
    
    public void doRenderLayer(final EntitySheep p_177162_1_, final float p_177162_2_, final float p_177162_3_, final float p_177162_4_, final float p_177162_5_, final float p_177162_6_, final float p_177162_7_, final float p_177162_8_) {
        if (!p_177162_1_.getSheared() && !p_177162_1_.isInvisible()) {
            this.sheepRenderer.bindTexture(LayerSheepWool.TEXTURE);
            if (p_177162_1_.hasCustomName() && "jeb_".equals(p_177162_1_.getCustomNameTag())) {
                final boolean var17 = true;
                final int var18 = p_177162_1_.ticksExisted / 25 + p_177162_1_.getEntityId();
                final int var19 = EnumDyeColor.values().length;
                final int var20 = var18 % var19;
                final int var21 = (var18 + 1) % var19;
                final float var22 = (p_177162_1_.ticksExisted % 25 + p_177162_4_) / 25.0f;
                final float[] var23 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var20));
                final float[] var24 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var21));
                GlStateManager.color(var23[0] * (1.0f - var22) + var24[0] * var22, var23[1] * (1.0f - var22) + var24[1] * var22, var23[2] * (1.0f - var22) + var24[2] * var22);
            }
            else {
                final float[] var25 = EntitySheep.func_175513_a(p_177162_1_.func_175509_cj());
                GlStateManager.color(var25[0], var25[1], var25[2]);
            }
            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(p_177162_1_, p_177162_2_, p_177162_3_, p_177162_4_);
            this.sheepModel.render(p_177162_1_, p_177162_2_, p_177162_3_, p_177162_5_, p_177162_6_, p_177162_7_, p_177162_8_);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.doRenderLayer((EntitySheep)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    }
}
