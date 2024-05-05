package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.CullState {
    public GlStateManager.BooleanState cullFace = new GlStateManager.BooleanState(2884);
    public int mode = 1029;

    private GlStateManager.CullState() {
    }
}
