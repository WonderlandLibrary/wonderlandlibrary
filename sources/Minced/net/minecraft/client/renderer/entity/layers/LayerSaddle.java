// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityPig;

public class LayerSaddle implements LayerRenderer<EntityPig>
{
    private static final ResourceLocation TEXTURE;
    private final RenderPig pigRenderer;
    private final ModelPig pigModel;
    
    public LayerSaddle(final RenderPig pigRendererIn) {
        this.pigModel = new ModelPig(0.5f);
        this.pigRenderer = pigRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityPig entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.getSaddled()) {
            this.pigRenderer.bindTexture(LayerSaddle.TEXTURE);
            this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
            this.pigModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
    }
}
