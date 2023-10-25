package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.TexGenCoord {
    public GlStateManager.BooleanState textureGen;
    public int coord;
    public int param = -1;

    public GlStateManager.TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
        this.coord = p_i46254_1_;
        this.textureGen = new GlStateManager.BooleanState(p_i46254_2_);
    }
}
