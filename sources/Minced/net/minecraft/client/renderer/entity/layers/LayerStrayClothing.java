// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityStray;

public class LayerStrayClothing implements LayerRenderer<EntityStray>
{
    private static final ResourceLocation STRAY_CLOTHES_TEXTURES;
    private final RenderLivingBase<?> renderer;
    private final ModelSkeleton layerModel;
    
    public LayerStrayClothing(final RenderLivingBase<?> p_i47183_1_) {
        this.layerModel = new ModelSkeleton(0.25f, true);
        this.renderer = p_i47183_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityStray entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.layerModel.setModelAttributes(this.renderer.getMainModel());
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderer.bindTexture(LayerStrayClothing.STRAY_CLOTHES_TEXTURES);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    static {
        STRAY_CLOTHES_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
    }
}
