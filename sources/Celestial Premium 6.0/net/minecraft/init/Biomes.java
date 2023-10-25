/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.init;

import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public abstract class Biomes {
    public static final Biome OCEAN;
    public static final Biome DEFAULT;
    public static final Biome PLAINS;
    public static final Biome DESERT;
    public static final Biome EXTREME_HILLS;
    public static final Biome FOREST;
    public static final Biome TAIGA;
    public static final Biome SWAMPLAND;
    public static final Biome RIVER;
    public static final Biome HELL;
    public static final Biome SKY;
    public static final Biome FROZEN_OCEAN;
    public static final Biome FROZEN_RIVER;
    public static final Biome ICE_PLAINS;
    public static final Biome ICE_MOUNTAINS;
    public static final Biome MUSHROOM_ISLAND;
    public static final Biome MUSHROOM_ISLAND_SHORE;
    public static final Biome BEACH;
    public static final Biome DESERT_HILLS;
    public static final Biome FOREST_HILLS;
    public static final Biome TAIGA_HILLS;
    public static final Biome EXTREME_HILLS_EDGE;
    public static final Biome JUNGLE;
    public static final Biome JUNGLE_HILLS;
    public static final Biome JUNGLE_EDGE;
    public static final Biome DEEP_OCEAN;
    public static final Biome STONE_BEACH;
    public static final Biome COLD_BEACH;
    public static final Biome BIRCH_FOREST;
    public static final Biome BIRCH_FOREST_HILLS;
    public static final Biome ROOFED_FOREST;
    public static final Biome COLD_TAIGA;
    public static final Biome COLD_TAIGA_HILLS;
    public static final Biome REDWOOD_TAIGA;
    public static final Biome REDWOOD_TAIGA_HILLS;
    public static final Biome EXTREME_HILLS_WITH_TREES;
    public static final Biome SAVANNA;
    public static final Biome SAVANNA_PLATEAU;
    public static final Biome MESA;
    public static final Biome MESA_ROCK;
    public static final Biome MESA_CLEAR_ROCK;
    public static final Biome VOID;
    public static final Biome MUTATED_PLAINS;
    public static final Biome MUTATED_DESERT;
    public static final Biome MUTATED_EXTREME_HILLS;
    public static final Biome MUTATED_FOREST;
    public static final Biome MUTATED_TAIGA;
    public static final Biome MUTATED_SWAMPLAND;
    public static final Biome MUTATED_ICE_FLATS;
    public static final Biome MUTATED_JUNGLE;
    public static final Biome MUTATED_JUNGLE_EDGE;
    public static final Biome MUTATED_BIRCH_FOREST;
    public static final Biome MUTATED_BIRCH_FOREST_HILLS;
    public static final Biome MUTATED_ROOFED_FOREST;
    public static final Biome MUTATED_TAIGA_COLD;
    public static final Biome MUTATED_REDWOOD_TAIGA;
    public static final Biome MUTATED_REDWOOD_TAIGA_HILLS;
    public static final Biome MUTATED_EXTREME_HILLS_WITH_TREES;
    public static final Biome MUTATED_SAVANNA;
    public static final Biome MUTATED_SAVANNA_ROCK;
    public static final Biome MUTATED_MESA;
    public static final Biome MUTATED_MESA_ROCK;
    public static final Biome MUTATED_MESA_CLEAR_ROCK;

    private static Biome getRegisteredBiome(String id) {
        Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(id));
        if (biome == null) {
            throw new IllegalStateException("Invalid Biome requested: " + id);
        }
        return biome;
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Biomes before Bootstrap!");
        }
        DEFAULT = OCEAN = Biomes.getRegisteredBiome("ocean");
        PLAINS = Biomes.getRegisteredBiome("plains");
        DESERT = Biomes.getRegisteredBiome("desert");
        EXTREME_HILLS = Biomes.getRegisteredBiome("extreme_hills");
        FOREST = Biomes.getRegisteredBiome("forest");
        TAIGA = Biomes.getRegisteredBiome("taiga");
        SWAMPLAND = Biomes.getRegisteredBiome("swampland");
        RIVER = Biomes.getRegisteredBiome("river");
        HELL = Biomes.getRegisteredBiome("hell");
        SKY = Biomes.getRegisteredBiome("sky");
        FROZEN_OCEAN = Biomes.getRegisteredBiome("frozen_ocean");
        FROZEN_RIVER = Biomes.getRegisteredBiome("frozen_river");
        ICE_PLAINS = Biomes.getRegisteredBiome("ice_flats");
        ICE_MOUNTAINS = Biomes.getRegisteredBiome("ice_mountains");
        MUSHROOM_ISLAND = Biomes.getRegisteredBiome("mushroom_island");
        MUSHROOM_ISLAND_SHORE = Biomes.getRegisteredBiome("mushroom_island_shore");
        BEACH = Biomes.getRegisteredBiome("beaches");
        DESERT_HILLS = Biomes.getRegisteredBiome("desert_hills");
        FOREST_HILLS = Biomes.getRegisteredBiome("forest_hills");
        TAIGA_HILLS = Biomes.getRegisteredBiome("taiga_hills");
        EXTREME_HILLS_EDGE = Biomes.getRegisteredBiome("smaller_extreme_hills");
        JUNGLE = Biomes.getRegisteredBiome("jungle");
        JUNGLE_HILLS = Biomes.getRegisteredBiome("jungle_hills");
        JUNGLE_EDGE = Biomes.getRegisteredBiome("jungle_edge");
        DEEP_OCEAN = Biomes.getRegisteredBiome("deep_ocean");
        STONE_BEACH = Biomes.getRegisteredBiome("stone_beach");
        COLD_BEACH = Biomes.getRegisteredBiome("cold_beach");
        BIRCH_FOREST = Biomes.getRegisteredBiome("birch_forest");
        BIRCH_FOREST_HILLS = Biomes.getRegisteredBiome("birch_forest_hills");
        ROOFED_FOREST = Biomes.getRegisteredBiome("roofed_forest");
        COLD_TAIGA = Biomes.getRegisteredBiome("taiga_cold");
        COLD_TAIGA_HILLS = Biomes.getRegisteredBiome("taiga_cold_hills");
        REDWOOD_TAIGA = Biomes.getRegisteredBiome("redwood_taiga");
        REDWOOD_TAIGA_HILLS = Biomes.getRegisteredBiome("redwood_taiga_hills");
        EXTREME_HILLS_WITH_TREES = Biomes.getRegisteredBiome("extreme_hills_with_trees");
        SAVANNA = Biomes.getRegisteredBiome("savanna");
        SAVANNA_PLATEAU = Biomes.getRegisteredBiome("savanna_rock");
        MESA = Biomes.getRegisteredBiome("mesa");
        MESA_ROCK = Biomes.getRegisteredBiome("mesa_rock");
        MESA_CLEAR_ROCK = Biomes.getRegisteredBiome("mesa_clear_rock");
        VOID = Biomes.getRegisteredBiome("void");
        MUTATED_PLAINS = Biomes.getRegisteredBiome("mutated_plains");
        MUTATED_DESERT = Biomes.getRegisteredBiome("mutated_desert");
        MUTATED_EXTREME_HILLS = Biomes.getRegisteredBiome("mutated_extreme_hills");
        MUTATED_FOREST = Biomes.getRegisteredBiome("mutated_forest");
        MUTATED_TAIGA = Biomes.getRegisteredBiome("mutated_taiga");
        MUTATED_SWAMPLAND = Biomes.getRegisteredBiome("mutated_swampland");
        MUTATED_ICE_FLATS = Biomes.getRegisteredBiome("mutated_ice_flats");
        MUTATED_JUNGLE = Biomes.getRegisteredBiome("mutated_jungle");
        MUTATED_JUNGLE_EDGE = Biomes.getRegisteredBiome("mutated_jungle_edge");
        MUTATED_BIRCH_FOREST = Biomes.getRegisteredBiome("mutated_birch_forest");
        MUTATED_BIRCH_FOREST_HILLS = Biomes.getRegisteredBiome("mutated_birch_forest_hills");
        MUTATED_ROOFED_FOREST = Biomes.getRegisteredBiome("mutated_roofed_forest");
        MUTATED_TAIGA_COLD = Biomes.getRegisteredBiome("mutated_taiga_cold");
        MUTATED_REDWOOD_TAIGA = Biomes.getRegisteredBiome("mutated_redwood_taiga");
        MUTATED_REDWOOD_TAIGA_HILLS = Biomes.getRegisteredBiome("mutated_redwood_taiga_hills");
        MUTATED_EXTREME_HILLS_WITH_TREES = Biomes.getRegisteredBiome("mutated_extreme_hills_with_trees");
        MUTATED_SAVANNA = Biomes.getRegisteredBiome("mutated_savanna");
        MUTATED_SAVANNA_ROCK = Biomes.getRegisteredBiome("mutated_savanna_rock");
        MUTATED_MESA = Biomes.getRegisteredBiome("mutated_mesa");
        MUTATED_MESA_ROCK = Biomes.getRegisteredBiome("mutated_mesa_rock");
        MUTATED_MESA_CLEAR_ROCK = Biomes.getRegisteredBiome("mutated_mesa_clear_rock");
    }
}

