package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.PolygonOffsetState {
    public GlStateManager.BooleanState polygonOffsetFill = new GlStateManager.BooleanState(32823);
    public GlStateManager.BooleanState polygonOffsetLine = new GlStateManager.BooleanState(10754);
    public float factor = 0.0f;
    public float units = 0.0f;

    private GlStateManager.PolygonOffsetState() {
    }
}
