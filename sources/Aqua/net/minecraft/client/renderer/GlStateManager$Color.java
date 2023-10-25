package net.minecraft.client.renderer;

static class GlStateManager.Color {
    public float red = 1.0f;
    public float green = 1.0f;
    public float blue = 1.0f;
    public float alpha = 1.0f;

    public GlStateManager.Color() {
    }

    public GlStateManager.Color(float redIn, float greenIn, float blueIn, float alphaIn) {
        this.red = redIn;
        this.green = greenIn;
        this.blue = blueIn;
        this.alpha = alphaIn;
    }
}
