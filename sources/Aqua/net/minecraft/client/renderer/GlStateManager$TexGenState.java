package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.TexGenState {
    public GlStateManager.TexGenCoord s = new GlStateManager.TexGenCoord(8192, 3168);
    public GlStateManager.TexGenCoord t = new GlStateManager.TexGenCoord(8193, 3169);
    public GlStateManager.TexGenCoord r = new GlStateManager.TexGenCoord(8194, 3170);
    public GlStateManager.TexGenCoord q = new GlStateManager.TexGenCoord(8195, 3171);

    private GlStateManager.TexGenState() {
    }
}
