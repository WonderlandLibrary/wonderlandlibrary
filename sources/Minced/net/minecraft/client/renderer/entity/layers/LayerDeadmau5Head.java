// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    
    public LayerDeadmau5Head(final RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if ("deadmau5".equals(entitylivingbaseIn.getName()) && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible()) {
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());
            for (int i = 0; i < 2; ++i) {
                final float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                final float f2 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f2, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.375f * (i * 2 - 1), 0.0f, 0.0f);
                GlStateManager.translate(0.0f, -0.375f, 0.0f);
                GlStateManager.rotate(-f2, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-f, 0.0f, 1.0f, 0.0f);
                final float f3 = 1.3333334f;
                GlStateManager.scale(1.3333334f, 1.3333334f, 1.3333334f);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625f);
                GlStateManager.popMatrix();
            }
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
