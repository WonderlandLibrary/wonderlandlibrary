/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.biome;

import net.minecraft.world.biome.Biome;

public class BiomeOcean
extends Biome {
    public BiomeOcean(Biome.BiomeProperties properties) {
        super(properties);
        this.spawnableCreatureList.clear();
    }

    @Override
    public Biome.TempCategory getTempCategory() {
        return Biome.TempCategory.OCEAN;
    }
}

