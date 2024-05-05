package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.TextureState {
    public GlStateManager.BooleanState texture2DState = new GlStateManager.BooleanState(3553);
    public int textureName = 0;

    private GlStateManager.TextureState() {
    }
}
