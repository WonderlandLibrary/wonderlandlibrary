package net.minecraft.world.biome;

public static class BiomeGenBase.Height {
    public float rootHeight;
    public float variation;

    public BiomeGenBase.Height(float rootHeightIn, float variationIn) {
        this.rootHeight = rootHeightIn;
        this.variation = variationIn;
    }

    public BiomeGenBase.Height attenuate() {
        return new BiomeGenBase.Height(this.rootHeight * 0.8f, this.variation * 0.6f);
    }
}
