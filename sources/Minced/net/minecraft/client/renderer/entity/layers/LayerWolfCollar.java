// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.CustomColors;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityWolf;

public class LayerWolfCollar implements LayerRenderer<EntityWolf>
{
    private static final ResourceLocation WOLF_COLLAR;
    private final RenderWolf wolfRenderer;
    
    public LayerWolfCollar(final RenderWolf wolfRendererIn) {
        this.wolfRenderer = wolfRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityWolf entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
            this.wolfRenderer.bindTexture(LayerWolfCollar.WOLF_COLLAR);
            float[] afloat = entitylivingbaseIn.getCollarColor().getColorComponentValues();
            if (Config.isCustomColors()) {
                afloat = CustomColors.getWolfCollarColors(entitylivingbaseIn.getCollarColor(), afloat);
            }
            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.wolfRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    static {
        WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    }
}
