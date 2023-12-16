// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.enums;

import java.util.Optional;
import org.apache.commons.lang3.text.WordUtils;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.world.biome.BiomeGenBase;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public enum BiomeType
{
    OCEAN("OCEAN", 0, "ocean", Integer.valueOf(0)), 
    PLAINS("PLAINS", 1, "plains", Integer.valueOf(1)), 
    DESERT("DESERT", 2, "desert", Integer.valueOf(2)), 
    MOUNTAINS("MOUNTAINS", 3, "mountains", Integer.valueOf(3)), 
    FOREST("FOREST", 4, "forest", Integer.valueOf(4)), 
    TAIGA("TAIGA", 5, "taiga", Integer.valueOf(5)), 
    SWAMP("SWAMP", 6, "swamp", Integer.valueOf(6)), 
    RIVER("RIVER", 7, "river", Integer.valueOf(7)), 
    NETHER_WASTES("NETHER_WASTES", 8, "nether_wastes", Integer.valueOf(8)), 
    THE_END("THE_END", 9, "the_end", Integer.valueOf(9)), 
    FROZEN_OCEAN("FROZEN_OCEAN", 10, "frozen_ocean", Integer.valueOf(10)), 
    FROZEN_RIVER("FROZEN_RIVER", 11, "frozen_river", Integer.valueOf(11)), 
    SNOWY_TUNDRA("SNOWY_TUNDRA", 12, "snowy_tundra", Integer.valueOf(12)), 
    SNOWY_MOUNTAINS("SNOWY_MOUNTAINS", 13, "snowy_mountains", Integer.valueOf(13)), 
    MUSHROOM_FIELDS("MUSHROOM_FIELDS", 14, "mushroom_fields", Integer.valueOf(14)), 
    MUSHROOM_FIELD_SHORE("MUSHROOM_FIELD_SHORE", 15, "mushroom_field_shore", Integer.valueOf(15)), 
    BEACH("BEACH", 16, "beach", Integer.valueOf(16)), 
    DESERT_HILLS("DESERT_HILLS", 17, "desert_hills", Integer.valueOf(17)), 
    WOODED_HILLS("WOODED_HILLS", 18, "wooded_hills", Integer.valueOf(18)), 
    TAIGA_HILLS("TAIGA_HILLS", 19, "taiga_hills", Integer.valueOf(19)), 
    MOUNTAIN_EDGE("MOUNTAIN_EDGE", 20, "mountain_edge", Integer.valueOf(20)), 
    JUNGLE("JUNGLE", 21, "jungle", Integer.valueOf(21)), 
    JUNGLE_HILLS("JUNGLE_HILLS", 22, "jungle_hills", Integer.valueOf(22)), 
    JUNGLE_EDGE("JUNGLE_EDGE", 23, "jungle_edge", Integer.valueOf(23)), 
    DEEP_OCEAN("DEEP_OCEAN", 24, "deep_ocean", Integer.valueOf(24)), 
    STONE_SHORE("STONE_SHORE", 25, "stone_shore", Integer.valueOf(25)), 
    SNOWY_BEACH("SNOWY_BEACH", 26, "snowy_beach", Integer.valueOf(26)), 
    BIRCH_FOREST("BIRCH_FOREST", 27, "birch_forest", Integer.valueOf(27)), 
    BIRCH_FOREST_HILLS("BIRCH_FOREST_HILLS", 28, "birch_forest_hills", Integer.valueOf(28)), 
    DARK_FOREST("DARK_FOREST", 29, "dark_forest", Integer.valueOf(29)), 
    SNOWY_TAIGA("SNOWY_TAIGA", 30, "snowy_taiga", Integer.valueOf(30)), 
    SNOWY_TAIGA_HILLS("SNOWY_TAIGA_HILLS", 31, "snowy_taiga_hills", Integer.valueOf(31)), 
    GIANT_TREE_TAIGA("GIANT_TREE_TAIGA", 32, "giant_tree_taiga", Integer.valueOf(32)), 
    GIANT_TREE_TAIGA_HILLS("GIANT_TREE_TAIGA_HILLS", 33, "giant_tree_taiga_hills", Integer.valueOf(33)), 
    WOODED_MOUNTAINS("WOODED_MOUNTAINS", 34, "wooded_mountains", Integer.valueOf(34)), 
    SAVANNA("SAVANNA", 35, "savanna", Integer.valueOf(35)), 
    SAVANNA_PLATEAU("SAVANNA_PLATEAU", 36, "savanna_plateau", Integer.valueOf(36)), 
    BADLANDS("BADLANDS", 37, "badlands", Integer.valueOf(37)), 
    WOODED_BADLANDS_PLATEAU("WOODED_BADLANDS_PLATEAU", 38, "wooded_badlands_plateau", Integer.valueOf(38)), 
    BADLANDS_PLATEAU("BADLANDS_PLATEAU", 39, "badlands_plateau", Integer.valueOf(39)), 
    SMALL_END_ISLANDS("SMALL_END_ISLANDS", 40, "small_end_islands", Integer.valueOf(40)), 
    END_MIDLANDS("END_MIDLANDS", 41, "end_midlands", Integer.valueOf(41)), 
    END_HIGHLANDS("END_HIGHLANDS", 42, "end_highlands", Integer.valueOf(42)), 
    END_BARRENS("END_BARRENS", 43, "end_barrens", Integer.valueOf(43)), 
    WARM_OCEAN("WARM_OCEAN", 44, "warm_ocean", Integer.valueOf(44)), 
    LUKEWARM_OCEAN("LUKEWARM_OCEAN", 45, "lukewarm_ocean", Integer.valueOf(45)), 
    COLD_OCEAN("COLD_OCEAN", 46, "cold_ocean", Integer.valueOf(46)), 
    DEEP_WARM_OCEAN("DEEP_WARM_OCEAN", 47, "deep_warm_ocean", Integer.valueOf(47)), 
    DEEP_LUKEWARM_OCEAN("DEEP_LUKEWARM_OCEAN", 48, "deep_lukewarm_ocean", Integer.valueOf(48)), 
    DEEP_COLD_OCEAN("DEEP_COLD_OCEAN", 49, "deep_cold_ocean", Integer.valueOf(49)), 
    DEEP_FROZEN_OCEAN("DEEP_FROZEN_OCEAN", 50, "deep_frozen_ocean", Integer.valueOf(50)), 
    THE_VOID("THE_VOID", 51, "the_void", Integer.valueOf(127)), 
    SUNFLOWER_PLAINS("SUNFLOWER_PLAINS", 52, "sunflower_plains", Integer.valueOf(129)), 
    DESERT_LAKES("DESERT_LAKES", 53, "desert_lakes", Integer.valueOf(130)), 
    GRAVELLY_MOUNTAINS("GRAVELLY_MOUNTAINS", 54, "gravelly_mountains", Integer.valueOf(131)), 
    FLOWER_FOREST("FLOWER_FOREST", 55, "flower_forest", Integer.valueOf(132)), 
    TAIGA_MOUNTAINS("TAIGA_MOUNTAINS", 56, "taiga_mountains", Integer.valueOf(133)), 
    SWAMP_HILLS("SWAMP_HILLS", 57, "swamp_hills", Integer.valueOf(134)), 
    ICE_SPIKES("ICE_SPIKES", 58, "ice_spikes", Integer.valueOf(140)), 
    MODIFIED_JUNGLE("MODIFIED_JUNGLE", 59, "modified_jungle", Integer.valueOf(149)), 
    MODIFIED_JUNGLE_EDGE("MODIFIED_JUNGLE_EDGE", 60, "modified_jungle_edge", Integer.valueOf(151)), 
    TALL_BIRCH_FOREST("TALL_BIRCH_FOREST", 61, "tall_birch_forest", Integer.valueOf(155)), 
    TALL_BIRCH_HILLS("TALL_BIRCH_HILLS", 62, "tall_birch_hills", Integer.valueOf(156)), 
    DARK_FOREST_HILLS("DARK_FOREST_HILLS", 63, "dark_forest_hills", Integer.valueOf(157)), 
    SNOWY_TAIGA_MOUNTAINS("SNOWY_TAIGA_MOUNTAINS", 64, "snowy_taiga_mountains", Integer.valueOf(158)), 
    GIANT_SPRUCE_TAIGA("GIANT_SPRUCE_TAIGA", 65, "giant_spruce_taiga", Integer.valueOf(160)), 
    GIANT_SPRUCE_TAIGA_HILLS("GIANT_SPRUCE_TAIGA_HILLS", 66, "giant_spruce_taiga_hills", Integer.valueOf(161)), 
    MODIFIED_GRAVELLY_MOUNTAINS("MODIFIED_GRAVELLY_MOUNTAINS", 67, "modified_gravelly_mountains", Integer.valueOf(162)), 
    SHATTERED_SAVANNA("SHATTERED_SAVANNA", 68, "shattered_savanna", Integer.valueOf(163)), 
    SHATTERED_SAVANNA_PLATEAU("SHATTERED_SAVANNA_PLATEAU", 69, "shattered_savanna_plateau", Integer.valueOf(164)), 
    ERODED_BADLANDS("ERODED_BADLANDS", 70, "eroded_badlands", Integer.valueOf(165)), 
    MODIFIED_WOODED_BADLANDS_PLATEAU("MODIFIED_WOODED_BADLANDS_PLATEAU", 71, "modified_wooded_badlands_plateau", Integer.valueOf(166)), 
    MODIFIED_BADLANDS_PLATEAU("MODIFIED_BADLANDS_PLATEAU", 72, "modified_badlands_plateau", Integer.valueOf(167)), 
    BAMBOO_JUNGLE("BAMBOO_JUNGLE", 73, "bamboo_jungle", Integer.valueOf(168)), 
    BAMBOO_JUNGLE_HILLS("BAMBOO_JUNGLE_HILLS", 74, "bamboo_jungle_hills", Integer.valueOf(169)), 
    SOUL_SAND_VALLEY("SOUL_SAND_VALLEY", 75, "soul_sand_valley"), 
    CRIMSON_FOREST("CRIMSON_FOREST", 76, "crimson_forest"), 
    WARPED_FOREST("WARPED_FOREST", 77, "warped_forest"), 
    BASALT_DELTAS("BASALT_DELTAS", 78, "basalt_deltas");
    
    public static final Int2ObjectMap<BiomeType> lIIIlIlIlIIlIllIIIIlllIlI;
    public static final Object2IntMap<BiomeGenBase> IIllIlIIllIIlIIllllIIllII;
    public final String biomeName;
    public final String minecraftBiomeName;
    public final Integer id;
    
    static {
        lIIIlIlIlIIlIllIIIIlllIlI = new Int2ObjectArrayMap<BiomeType>(BiomeType.SOUL_SAND_VALLEY.ordinal());
        (IIllIlIIllIIlIIllllIIllII = new Object2IntArrayMap<BiomeGenBase>(values().length)).defaultReturnValue(-1);
        for (final BiomeType var4 : values()) {
            if (var4.id != null) {
                BiomeType.lIIIlIlIlIIlIllIIIIlllIlI.put(var4.id, var4);
            }
        }
    }
    
    private BiomeType(final String s, final int n, final String var3) {
        this(s, n, var3, null);
    }
    
    private BiomeType(final String name, final int ordinal, final String var3, final Integer var4) {
        this.biomeName = WordUtils.capitalize(var3.replace("_", " "), (char[])null);
        this.minecraftBiomeName = var3;
        this.id = var4;
    }
    
    public Optional<Integer> lIlIlIlIlIIlIIlIIllIIIIIl() {
        return Optional.ofNullable(this.id);
    }
    
    public static BiomeType getBiomeType(final BiomeGenBase var0) {
        if (var0 == null) {
            return BiomeType.PLAINS;
        }
        final int var = BiomeType.IIllIlIIllIIlIIllllIIllII.getInt(var0);
        if (var != -1) {
            return values()[var];
        }
        final Optional<Integer> var2 = Optional.of(var0.biomeID);
        if (var2.isPresent()) {
            final BiomeType var3 = BiomeType.lIIIlIlIlIIlIllIIIIlllIlI.get(var2.get());
            if (var3 != null) {
                BiomeType.IIllIlIIllIIlIIllllIIllII.put(var0, var3.ordinal());
                return var3;
            }
        }
        for (final BiomeType var7 : values()) {
            if (var0.biomeName.equals("minecraft:" + var7.minecraftBiomeName)) {
                BiomeType.IIllIlIIllIIlIIllllIIllII.put(var0, var7.ordinal());
                return var7;
            }
        }
        return BiomeType.PLAINS;
    }
    
    public String getBiomeName() {
        return this.biomeName;
    }
    
    public String getMinecraftBiomeName() {
        return this.minecraftBiomeName;
    }
}
