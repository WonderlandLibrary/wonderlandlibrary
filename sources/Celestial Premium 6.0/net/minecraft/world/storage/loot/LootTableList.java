/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;

public class LootTableList {
    private static final Set<ResourceLocation> LOOT_TABLES = Sets.newHashSet();
    private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES = Collections.unmodifiableSet(LOOT_TABLES);
    public static final ResourceLocation EMPTY = LootTableList.register("empty");
    public static final ResourceLocation CHESTS_SPAWN_BONUS_CHEST = LootTableList.register("chests/spawn_bonus_chest");
    public static final ResourceLocation CHESTS_END_CITY_TREASURE = LootTableList.register("chests/end_city_treasure");
    public static final ResourceLocation CHESTS_SIMPLE_DUNGEON = LootTableList.register("chests/simple_dungeon");
    public static final ResourceLocation CHESTS_VILLAGE_BLACKSMITH = LootTableList.register("chests/village_blacksmith");
    public static final ResourceLocation CHESTS_ABANDONED_MINESHAFT = LootTableList.register("chests/abandoned_mineshaft");
    public static final ResourceLocation CHESTS_NETHER_BRIDGE = LootTableList.register("chests/nether_bridge");
    public static final ResourceLocation CHESTS_STRONGHOLD_LIBRARY = LootTableList.register("chests/stronghold_library");
    public static final ResourceLocation CHESTS_STRONGHOLD_CROSSING = LootTableList.register("chests/stronghold_crossing");
    public static final ResourceLocation CHESTS_STRONGHOLD_CORRIDOR = LootTableList.register("chests/stronghold_corridor");
    public static final ResourceLocation CHESTS_DESERT_PYRAMID = LootTableList.register("chests/desert_pyramid");
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE = LootTableList.register("chests/jungle_temple");
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE_DISPENSER = LootTableList.register("chests/jungle_temple_dispenser");
    public static final ResourceLocation CHESTS_IGLOO_CHEST = LootTableList.register("chests/igloo_chest");
    public static final ResourceLocation field_191192_o = LootTableList.register("chests/woodland_mansion");
    public static final ResourceLocation ENTITIES_WITCH = LootTableList.register("entities/witch");
    public static final ResourceLocation ENTITIES_BLAZE = LootTableList.register("entities/blaze");
    public static final ResourceLocation ENTITIES_CREEPER = LootTableList.register("entities/creeper");
    public static final ResourceLocation ENTITIES_SPIDER = LootTableList.register("entities/spider");
    public static final ResourceLocation ENTITIES_CAVE_SPIDER = LootTableList.register("entities/cave_spider");
    public static final ResourceLocation ENTITIES_GIANT = LootTableList.register("entities/giant");
    public static final ResourceLocation ENTITIES_SILVERFISH = LootTableList.register("entities/silverfish");
    public static final ResourceLocation ENTITIES_ENDERMAN = LootTableList.register("entities/enderman");
    public static final ResourceLocation ENTITIES_GUARDIAN = LootTableList.register("entities/guardian");
    public static final ResourceLocation ENTITIES_ELDER_GUARDIAN = LootTableList.register("entities/elder_guardian");
    public static final ResourceLocation ENTITIES_SHULKER = LootTableList.register("entities/shulker");
    public static final ResourceLocation ENTITIES_IRON_GOLEM = LootTableList.register("entities/iron_golem");
    public static final ResourceLocation ENTITIES_SNOWMAN = LootTableList.register("entities/snowman");
    public static final ResourceLocation ENTITIES_RABBIT = LootTableList.register("entities/rabbit");
    public static final ResourceLocation ENTITIES_CHICKEN = LootTableList.register("entities/chicken");
    public static final ResourceLocation ENTITIES_PIG = LootTableList.register("entities/pig");
    public static final ResourceLocation ENTITIES_POLAR_BEAR = LootTableList.register("entities/polar_bear");
    public static final ResourceLocation ENTITIES_HORSE = LootTableList.register("entities/horse");
    public static final ResourceLocation field_191190_H = LootTableList.register("entities/donkey");
    public static final ResourceLocation field_191191_I = LootTableList.register("entities/mule");
    public static final ResourceLocation ENTITIES_ZOMBIE_HORSE = LootTableList.register("entities/zombie_horse");
    public static final ResourceLocation ENTITIES_SKELETON_HORSE = LootTableList.register("entities/skeleton_horse");
    public static final ResourceLocation ENTITIES_COW = LootTableList.register("entities/cow");
    public static final ResourceLocation ENTITIES_MUSHROOM_COW = LootTableList.register("entities/mushroom_cow");
    public static final ResourceLocation ENTITIES_WOLF = LootTableList.register("entities/wolf");
    public static final ResourceLocation ENTITIES_OCELOT = LootTableList.register("entities/ocelot");
    public static final ResourceLocation ENTITIES_SHEEP = LootTableList.register("entities/sheep");
    public static final ResourceLocation ENTITIES_SHEEP_WHITE = LootTableList.register("entities/sheep/white");
    public static final ResourceLocation ENTITIES_SHEEP_ORANGE = LootTableList.register("entities/sheep/orange");
    public static final ResourceLocation ENTITIES_SHEEP_MAGENTA = LootTableList.register("entities/sheep/magenta");
    public static final ResourceLocation ENTITIES_SHEEP_LIGHT_BLUE = LootTableList.register("entities/sheep/light_blue");
    public static final ResourceLocation ENTITIES_SHEEP_YELLOW = LootTableList.register("entities/sheep/yellow");
    public static final ResourceLocation ENTITIES_SHEEP_LIME = LootTableList.register("entities/sheep/lime");
    public static final ResourceLocation ENTITIES_SHEEP_PINK = LootTableList.register("entities/sheep/pink");
    public static final ResourceLocation ENTITIES_SHEEP_GRAY = LootTableList.register("entities/sheep/gray");
    public static final ResourceLocation ENTITIES_SHEEP_SILVER = LootTableList.register("entities/sheep/silver");
    public static final ResourceLocation ENTITIES_SHEEP_CYAN = LootTableList.register("entities/sheep/cyan");
    public static final ResourceLocation ENTITIES_SHEEP_PURPLE = LootTableList.register("entities/sheep/purple");
    public static final ResourceLocation ENTITIES_SHEEP_BLUE = LootTableList.register("entities/sheep/blue");
    public static final ResourceLocation ENTITIES_SHEEP_BROWN = LootTableList.register("entities/sheep/brown");
    public static final ResourceLocation ENTITIES_SHEEP_GREEN = LootTableList.register("entities/sheep/green");
    public static final ResourceLocation ENTITIES_SHEEP_RED = LootTableList.register("entities/sheep/red");
    public static final ResourceLocation ENTITIES_SHEEP_BLACK = LootTableList.register("entities/sheep/black");
    public static final ResourceLocation ENTITIES_BAT = LootTableList.register("entities/bat");
    public static final ResourceLocation ENTITIES_SLIME = LootTableList.register("entities/slime");
    public static final ResourceLocation ENTITIES_MAGMA_CUBE = LootTableList.register("entities/magma_cube");
    public static final ResourceLocation ENTITIES_GHAST = LootTableList.register("entities/ghast");
    public static final ResourceLocation ENTITIES_SQUID = LootTableList.register("entities/squid");
    public static final ResourceLocation ENTITIES_ENDERMITE = LootTableList.register("entities/endermite");
    public static final ResourceLocation ENTITIES_ZOMBIE = LootTableList.register("entities/zombie");
    public static final ResourceLocation ENTITIES_ZOMBIE_PIGMAN = LootTableList.register("entities/zombie_pigman");
    public static final ResourceLocation ENTITIES_SKELETON = LootTableList.register("entities/skeleton");
    public static final ResourceLocation ENTITIES_WITHER_SKELETON = LootTableList.register("entities/wither_skeleton");
    public static final ResourceLocation ENTITIES_STRAY = LootTableList.register("entities/stray");
    public static final ResourceLocation field_191182_ar = LootTableList.register("entities/husk");
    public static final ResourceLocation field_191183_as = LootTableList.register("entities/zombie_villager");
    public static final ResourceLocation field_191184_at = LootTableList.register("entities/villager");
    public static final ResourceLocation field_191185_au = LootTableList.register("entities/evocation_illager");
    public static final ResourceLocation field_191186_av = LootTableList.register("entities/vindication_illager");
    public static final ResourceLocation field_191187_aw = LootTableList.register("entities/llama");
    public static final ResourceLocation field_192561_ax = LootTableList.register("entities/parrot");
    public static final ResourceLocation field_191188_ax = LootTableList.register("entities/vex");
    public static final ResourceLocation field_191189_ay = LootTableList.register("entities/ender_dragon");
    public static final ResourceLocation GAMEPLAY_FISHING = LootTableList.register("gameplay/fishing");
    public static final ResourceLocation GAMEPLAY_FISHING_JUNK = LootTableList.register("gameplay/fishing/junk");
    public static final ResourceLocation GAMEPLAY_FISHING_TREASURE = LootTableList.register("gameplay/fishing/treasure");
    public static final ResourceLocation GAMEPLAY_FISHING_FISH = LootTableList.register("gameplay/fishing/fish");

    private static ResourceLocation register(String id) {
        return LootTableList.register(new ResourceLocation("minecraft", id));
    }

    public static ResourceLocation register(ResourceLocation id) {
        if (LOOT_TABLES.add(id)) {
            return id;
        }
        throw new IllegalArgumentException(id + " is already a registered built-in loot table");
    }

    public static Set<ResourceLocation> getAll() {
        return READ_ONLY_LOOT_TABLES;
    }

    public static boolean func_193579_b() {
        LootTableManager loottablemanager = new LootTableManager(null);
        for (ResourceLocation resourcelocation : READ_ONLY_LOOT_TABLES) {
            if (loottablemanager.getLootTableFromLocation(resourcelocation) != LootTable.EMPTY_LOOT_TABLE) continue;
            return false;
        }
        return true;
    }
}

