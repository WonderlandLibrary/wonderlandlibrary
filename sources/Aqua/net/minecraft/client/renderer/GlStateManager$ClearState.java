package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.ClearState {
    public double depth = 1.0;
    public GlStateManager.Color color = new GlStateManager.Color(0.0f, 0.0f, 0.0f, 0.0f);
    public int field_179204_c = 0;

    private GlStateManager.ClearState() {
    }
}
