/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerDeadmau5Head
implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerDeadmau5Head(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (abstractClientPlayer.getName().equals("deadmau5") && abstractClientPlayer.hasSkin() && !abstractClientPlayer.isInvisible()) {
            this.playerRenderer.bindTexture(abstractClientPlayer.getLocationSkin());
            int n = 0;
            while (n < 2) {
                float f8 = abstractClientPlayer.prevRotationYaw + (abstractClientPlayer.rotationYaw - abstractClientPlayer.prevRotationYaw) * f3 - (abstractClientPlayer.prevRenderYawOffset + (abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset) * f3);
                float f9 = abstractClientPlayer.prevRotationPitch + (abstractClientPlayer.rotationPitch - abstractClientPlayer.prevRotationPitch) * f3;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f8, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f9, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.375f * (float)(n * 2 - 1), 0.0f, 0.0f);
                GlStateManager.translate(0.0f, -0.375f, 0.0f);
                GlStateManager.rotate(-f9, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-f8, 0.0f, 1.0f, 0.0f);
                float f10 = 1.3333334f;
                GlStateManager.scale(f10, f10, f10);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625f);
                GlStateManager.popMatrix();
                ++n;
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}

