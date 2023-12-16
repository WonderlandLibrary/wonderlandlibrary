// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import optifine.CustomColors;
import optifine.Config;
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
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    }
    
    public LayerSheepWool(final RenderSheep p_i46112_1_) {
        this.sheepModel = new ModelSheep1();
        this.sheepRenderer = p_i46112_1_;
    }
    
    public void doRenderLayer(final EntitySheep p_177162_1_, final float p_177162_2_, final float p_177162_3_, final float p_177162_4_, final float p_177162_5_, final float p_177162_6_, final float p_177162_7_, final float p_177162_8_) {
        if (!p_177162_1_.getSheared() && !p_177162_1_.isInvisible()) {
            this.sheepRenderer.bindTexture(LayerSheepWool.TEXTURE);
            if (p_177162_1_.hasCustomName() && "jeb_".equals(p_177162_1_.getCustomNameTag())) {
                final boolean var91 = true;
                final int var92 = p_177162_1_.ticksExisted / 25 + p_177162_1_.getEntityId();
                final int var93 = EnumDyeColor.values().length;
                final int var94 = var92 % var93;
                final int var95 = (var92 + 1) % var93;
                final float var96 = (p_177162_1_.ticksExisted % 25 + p_177162_4_) / 25.0f;
                float[] var97 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var94));
                float[] var98 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var95));
                if (Config.isCustomColors()) {
                    var97 = CustomColors.getSheepColors(EnumDyeColor.func_176764_b(var94), var97);
                    var98 = CustomColors.getSheepColors(EnumDyeColor.func_176764_b(var95), var98);
                }
                GlStateManager.color(var97[0] * (1.0f - var96) + var98[0] * var96, var97[1] * (1.0f - var96) + var98[1] * var96, var97[2] * (1.0f - var96) + var98[2] * var96);
            }
            else {
                float[] var99 = EntitySheep.func_175513_a(p_177162_1_.func_175509_cj());
                if (Config.isCustomColors()) {
                    var99 = CustomColors.getSheepColors(p_177162_1_.func_175509_cj(), var99);
                }
                GlStateManager.color(var99[0], var99[1], var99[2]);
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
}
