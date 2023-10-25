package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.FogState {
    public GlStateManager.BooleanState fog = new GlStateManager.BooleanState(2912);
    public int mode = 2048;
    public float density = 1.0f;
    public float start = 0.0f;
    public float end = 1.0f;

    private GlStateManager.FogState() {
    }
}
