/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoidDecorator;

public class BiomeVoid
extends Biome {
    public BiomeVoid(Biome.BiomeProperties properties) {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.theBiomeDecorator = new BiomeVoidDecorator();
    }

    @Override
    public boolean ignorePlayerSpawnSuitability() {
        return true;
    }
}

