/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.data;

import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Map;

public final class BiomeMappings {
    private static final Object2IntMap<String> MODERN_TO_LEGACY_ID = new Object2IntOpenHashMap<String>();
    private static final Object2IntMap<String> LEGACY_BIOMES = new Object2IntOpenHashMap<String>();

    private static void add(int id, String biome) {
        LEGACY_BIOMES.put(biome, id);
    }

    public static int toLegacyBiome(String biome) {
        int legacyBiome;
        if (biome.indexOf(58) == -1) {
            biome = "minecraft:" + biome;
        }
        if ((legacyBiome = MODERN_TO_LEGACY_ID.getInt(biome)) == -1) {
            if (!Via.getConfig().isSuppressConversionWarnings()) {
                ViaBackwards.getPlatform().getLogger().warning("Biome with id " + biome + " has no legacy biome mapping (custom datapack?)");
            }
            return 1;
        }
        return legacyBiome;
    }

    static {
        LEGACY_BIOMES.defaultReturnValue(-1);
        MODERN_TO_LEGACY_ID.defaultReturnValue(-1);
        BiomeMappings.add(0, "ocean");
        BiomeMappings.add(1, "plains");
        BiomeMappings.add(2, "desert");
        BiomeMappings.add(3, "mountains");
        BiomeMappings.add(4, "forest");
        BiomeMappings.add(5, "taiga");
        BiomeMappings.add(6, "swamp");
        BiomeMappings.add(7, "river");
        BiomeMappings.add(8, "nether");
        BiomeMappings.add(9, "the_end");
        BiomeMappings.add(10, "frozen_ocean");
        BiomeMappings.add(11, "frozen_river");
        BiomeMappings.add(12, "snowy_tundra");
        BiomeMappings.add(13, "snowy_mountains");
        BiomeMappings.add(14, "mushroom_fields");
        BiomeMappings.add(15, "mushroom_field_shore");
        BiomeMappings.add(16, "beach");
        BiomeMappings.add(17, "desert_hills");
        BiomeMappings.add(18, "wooded_hills");
        BiomeMappings.add(19, "taiga_hills");
        BiomeMappings.add(20, "mountain_edge");
        BiomeMappings.add(21, "jungle");
        BiomeMappings.add(22, "jungle_hills");
        BiomeMappings.add(23, "jungle_edge");
        BiomeMappings.add(24, "deep_ocean");
        BiomeMappings.add(25, "stone_shore");
        BiomeMappings.add(26, "snowy_beach");
        BiomeMappings.add(27, "birch_forest");
        BiomeMappings.add(28, "birch_forest_hills");
        BiomeMappings.add(29, "dark_forest");
        BiomeMappings.add(30, "snowy_taiga");
        BiomeMappings.add(31, "snowy_taiga_hills");
        BiomeMappings.add(32, "giant_tree_taiga");
        BiomeMappings.add(33, "giant_tree_taiga_hills");
        BiomeMappings.add(34, "wooded_mountains");
        BiomeMappings.add(35, "savanna");
        BiomeMappings.add(36, "savanna_plateau");
        BiomeMappings.add(37, "badlands");
        BiomeMappings.add(38, "wooded_badlands_plateau");
        BiomeMappings.add(39, "badlands_plateau");
        BiomeMappings.add(40, "small_end_islands");
        BiomeMappings.add(41, "end_midlands");
        BiomeMappings.add(42, "end_highlands");
        BiomeMappings.add(43, "end_barrens");
        BiomeMappings.add(44, "warm_ocean");
        BiomeMappings.add(45, "lukewarm_ocean");
        BiomeMappings.add(46, "cold_ocean");
        BiomeMappings.add(47, "deep_warm_ocean");
        BiomeMappings.add(48, "deep_lukewarm_ocean");
        BiomeMappings.add(49, "deep_cold_ocean");
        BiomeMappings.add(50, "deep_frozen_ocean");
        BiomeMappings.add(127, "the_void");
        BiomeMappings.add(129, "sunflower_plains");
        BiomeMappings.add(130, "desert_lakes");
        BiomeMappings.add(131, "gravelly_mountains");
        BiomeMappings.add(132, "flower_forest");
        BiomeMappings.add(133, "taiga_mountains");
        BiomeMappings.add(134, "swamp_hills");
        BiomeMappings.add(140, "ice_spikes");
        BiomeMappings.add(149, "modified_jungle");
        BiomeMappings.add(151, "modified_jungle_edge");
        BiomeMappings.add(155, "tall_birch_forest");
        BiomeMappings.add(156, "tall_birch_hills");
        BiomeMappings.add(157, "dark_forest_hills");
        BiomeMappings.add(158, "snowy_taiga_mountains");
        BiomeMappings.add(160, "giant_spruce_taiga");
        BiomeMappings.add(161, "giant_spruce_taiga_hills");
        BiomeMappings.add(162, "modified_gravelly_mountains");
        BiomeMappings.add(163, "shattered_savanna");
        BiomeMappings.add(164, "shattered_savanna_plateau");
        BiomeMappings.add(165, "eroded_badlands");
        BiomeMappings.add(166, "modified_wooded_badlands_plateau");
        BiomeMappings.add(167, "modified_badlands_plateau");
        BiomeMappings.add(168, "bamboo_jungle");
        BiomeMappings.add(169, "bamboo_jungle_hills");
        for (Object2IntMap.Entry entry : LEGACY_BIOMES.object2IntEntrySet()) {
            MODERN_TO_LEGACY_ID.put("minecraft:" + (String)entry.getKey(), entry.getIntValue());
        }
        JsonObject mappings = VBMappingDataLoader.loadFromDataDir("biome-mappings.json");
        for (Map.Entry<String, JsonElement> entry : mappings.entrySet()) {
            int legacyBiome = LEGACY_BIOMES.getInt(entry.getValue().getAsString());
            if (legacyBiome == -1) {
                ViaBackwards.getPlatform().getLogger().warning("Unknown legacy biome: " + entry.getValue().getAsString());
                continue;
            }
            MODERN_TO_LEGACY_ID.put(entry.getKey(), legacyBiome);
        }
    }
}

