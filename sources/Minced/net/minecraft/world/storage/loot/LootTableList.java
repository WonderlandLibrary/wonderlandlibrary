// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import java.util.Collections;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public class LootTableList
{
    private static final Set<ResourceLocation> LOOT_TABLES;
    private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES;
    public static final ResourceLocation EMPTY;
    public static final ResourceLocation CHESTS_SPAWN_BONUS_CHEST;
    public static final ResourceLocation CHESTS_END_CITY_TREASURE;
    public static final ResourceLocation CHESTS_SIMPLE_DUNGEON;
    public static final ResourceLocation CHESTS_VILLAGE_BLACKSMITH;
    public static final ResourceLocation CHESTS_ABANDONED_MINESHAFT;
    public static final ResourceLocation CHESTS_NETHER_BRIDGE;
    public static final ResourceLocation CHESTS_STRONGHOLD_LIBRARY;
    public static final ResourceLocation CHESTS_STRONGHOLD_CROSSING;
    public static final ResourceLocation CHESTS_STRONGHOLD_CORRIDOR;
    public static final ResourceLocation CHESTS_DESERT_PYRAMID;
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE;
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE_DISPENSER;
    public static final ResourceLocation CHESTS_IGLOO_CHEST;
    public static final ResourceLocation CHESTS_WOODLAND_MANSION;
    public static final ResourceLocation ENTITIES_WITCH;
    public static final ResourceLocation ENTITIES_BLAZE;
    public static final ResourceLocation ENTITIES_CREEPER;
    public static final ResourceLocation ENTITIES_SPIDER;
    public static final ResourceLocation ENTITIES_CAVE_SPIDER;
    public static final ResourceLocation ENTITIES_GIANT;
    public static final ResourceLocation ENTITIES_SILVERFISH;
    public static final ResourceLocation ENTITIES_ENDERMAN;
    public static final ResourceLocation ENTITIES_GUARDIAN;
    public static final ResourceLocation ENTITIES_ELDER_GUARDIAN;
    public static final ResourceLocation ENTITIES_SHULKER;
    public static final ResourceLocation ENTITIES_IRON_GOLEM;
    public static final ResourceLocation ENTITIES_SNOWMAN;
    public static final ResourceLocation ENTITIES_RABBIT;
    public static final ResourceLocation ENTITIES_CHICKEN;
    public static final ResourceLocation ENTITIES_PIG;
    public static final ResourceLocation ENTITIES_POLAR_BEAR;
    public static final ResourceLocation ENTITIES_HORSE;
    public static final ResourceLocation ENTITIES_DONKEY;
    public static final ResourceLocation ENTITIES_MULE;
    public static final ResourceLocation ENTITIES_ZOMBIE_HORSE;
    public static final ResourceLocation ENTITIES_SKELETON_HORSE;
    public static final ResourceLocation ENTITIES_COW;
    public static final ResourceLocation ENTITIES_MUSHROOM_COW;
    public static final ResourceLocation ENTITIES_WOLF;
    public static final ResourceLocation ENTITIES_OCELOT;
    public static final ResourceLocation ENTITIES_SHEEP;
    public static final ResourceLocation ENTITIES_SHEEP_WHITE;
    public static final ResourceLocation ENTITIES_SHEEP_ORANGE;
    public static final ResourceLocation ENTITIES_SHEEP_MAGENTA;
    public static final ResourceLocation ENTITIES_SHEEP_LIGHT_BLUE;
    public static final ResourceLocation ENTITIES_SHEEP_YELLOW;
    public static final ResourceLocation ENTITIES_SHEEP_LIME;
    public static final ResourceLocation ENTITIES_SHEEP_PINK;
    public static final ResourceLocation ENTITIES_SHEEP_GRAY;
    public static final ResourceLocation ENTITIES_SHEEP_SILVER;
    public static final ResourceLocation ENTITIES_SHEEP_CYAN;
    public static final ResourceLocation ENTITIES_SHEEP_PURPLE;
    public static final ResourceLocation ENTITIES_SHEEP_BLUE;
    public static final ResourceLocation ENTITIES_SHEEP_BROWN;
    public static final ResourceLocation ENTITIES_SHEEP_GREEN;
    public static final ResourceLocation ENTITIES_SHEEP_RED;
    public static final ResourceLocation ENTITIES_SHEEP_BLACK;
    public static final ResourceLocation ENTITIES_BAT;
    public static final ResourceLocation ENTITIES_SLIME;
    public static final ResourceLocation ENTITIES_MAGMA_CUBE;
    public static final ResourceLocation ENTITIES_GHAST;
    public static final ResourceLocation ENTITIES_SQUID;
    public static final ResourceLocation ENTITIES_ENDERMITE;
    public static final ResourceLocation ENTITIES_ZOMBIE;
    public static final ResourceLocation ENTITIES_ZOMBIE_PIGMAN;
    public static final ResourceLocation ENTITIES_SKELETON;
    public static final ResourceLocation ENTITIES_WITHER_SKELETON;
    public static final ResourceLocation ENTITIES_STRAY;
    public static final ResourceLocation ENTITIES_HUSK;
    public static final ResourceLocation ENTITIES_ZOMBIE_VILLAGER;
    public static final ResourceLocation ENTITIES_VILLAGER;
    public static final ResourceLocation ENTITIES_EVOCATION_ILLAGER;
    public static final ResourceLocation ENTITIES_VINDICATION_ILLAGER;
    public static final ResourceLocation ENTITIES_LLAMA;
    public static final ResourceLocation ENTITIES_PARROT;
    public static final ResourceLocation ENTITIES_VEX;
    public static final ResourceLocation ENTITIES_ENDER_DRAGON;
    public static final ResourceLocation GAMEPLAY_FISHING;
    public static final ResourceLocation GAMEPLAY_FISHING_JUNK;
    public static final ResourceLocation GAMEPLAY_FISHING_TREASURE;
    public static final ResourceLocation GAMEPLAY_FISHING_FISH;
    
    private static ResourceLocation register(final String id) {
        return register(new ResourceLocation("minecraft", id));
    }
    
    public static ResourceLocation register(final ResourceLocation id) {
        if (LootTableList.LOOT_TABLES.add(id)) {
            return id;
        }
        throw new IllegalArgumentException(id + " is already a registered built-in loot table");
    }
    
    public static Set<ResourceLocation> getAll() {
        return LootTableList.READ_ONLY_LOOT_TABLES;
    }
    
    public static boolean test() {
        final LootTableManager loottablemanager = new LootTableManager(null);
        for (final ResourceLocation resourcelocation : LootTableList.READ_ONLY_LOOT_TABLES) {
            if (loottablemanager.getLootTableFromLocation(resourcelocation) == LootTable.EMPTY_LOOT_TABLE) {
                return false;
            }
        }
        return true;
    }
    
    static {
        LOOT_TABLES = Sets.newHashSet();
        READ_ONLY_LOOT_TABLES = Collections.unmodifiableSet((Set<? extends ResourceLocation>)LootTableList.LOOT_TABLES);
        EMPTY = register("empty");
        CHESTS_SPAWN_BONUS_CHEST = register("chests/spawn_bonus_chest");
        CHESTS_END_CITY_TREASURE = register("chests/end_city_treasure");
        CHESTS_SIMPLE_DUNGEON = register("chests/simple_dungeon");
        CHESTS_VILLAGE_BLACKSMITH = register("chests/village_blacksmith");
        CHESTS_ABANDONED_MINESHAFT = register("chests/abandoned_mineshaft");
        CHESTS_NETHER_BRIDGE = register("chests/nether_bridge");
        CHESTS_STRONGHOLD_LIBRARY = register("chests/stronghold_library");
        CHESTS_STRONGHOLD_CROSSING = register("chests/stronghold_crossing");
        CHESTS_STRONGHOLD_CORRIDOR = register("chests/stronghold_corridor");
        CHESTS_DESERT_PYRAMID = register("chests/desert_pyramid");
        CHESTS_JUNGLE_TEMPLE = register("chests/jungle_temple");
        CHESTS_JUNGLE_TEMPLE_DISPENSER = register("chests/jungle_temple_dispenser");
        CHESTS_IGLOO_CHEST = register("chests/igloo_chest");
        CHESTS_WOODLAND_MANSION = register("chests/woodland_mansion");
        ENTITIES_WITCH = register("entities/witch");
        ENTITIES_BLAZE = register("entities/blaze");
        ENTITIES_CREEPER = register("entities/creeper");
        ENTITIES_SPIDER = register("entities/spider");
        ENTITIES_CAVE_SPIDER = register("entities/cave_spider");
        ENTITIES_GIANT = register("entities/giant");
        ENTITIES_SILVERFISH = register("entities/silverfish");
        ENTITIES_ENDERMAN = register("entities/enderman");
        ENTITIES_GUARDIAN = register("entities/guardian");
        ENTITIES_ELDER_GUARDIAN = register("entities/elder_guardian");
        ENTITIES_SHULKER = register("entities/shulker");
        ENTITIES_IRON_GOLEM = register("entities/iron_golem");
        ENTITIES_SNOWMAN = register("entities/snowman");
        ENTITIES_RABBIT = register("entities/rabbit");
        ENTITIES_CHICKEN = register("entities/chicken");
        ENTITIES_PIG = register("entities/pig");
        ENTITIES_POLAR_BEAR = register("entities/polar_bear");
        ENTITIES_HORSE = register("entities/horse");
        ENTITIES_DONKEY = register("entities/donkey");
        ENTITIES_MULE = register("entities/mule");
        ENTITIES_ZOMBIE_HORSE = register("entities/zombie_horse");
        ENTITIES_SKELETON_HORSE = register("entities/skeleton_horse");
        ENTITIES_COW = register("entities/cow");
        ENTITIES_MUSHROOM_COW = register("entities/mushroom_cow");
        ENTITIES_WOLF = register("entities/wolf");
        ENTITIES_OCELOT = register("entities/ocelot");
        ENTITIES_SHEEP = register("entities/sheep");
        ENTITIES_SHEEP_WHITE = register("entities/sheep/white");
        ENTITIES_SHEEP_ORANGE = register("entities/sheep/orange");
        ENTITIES_SHEEP_MAGENTA = register("entities/sheep/magenta");
        ENTITIES_SHEEP_LIGHT_BLUE = register("entities/sheep/light_blue");
        ENTITIES_SHEEP_YELLOW = register("entities/sheep/yellow");
        ENTITIES_SHEEP_LIME = register("entities/sheep/lime");
        ENTITIES_SHEEP_PINK = register("entities/sheep/pink");
        ENTITIES_SHEEP_GRAY = register("entities/sheep/gray");
        ENTITIES_SHEEP_SILVER = register("entities/sheep/silver");
        ENTITIES_SHEEP_CYAN = register("entities/sheep/cyan");
        ENTITIES_SHEEP_PURPLE = register("entities/sheep/purple");
        ENTITIES_SHEEP_BLUE = register("entities/sheep/blue");
        ENTITIES_SHEEP_BROWN = register("entities/sheep/brown");
        ENTITIES_SHEEP_GREEN = register("entities/sheep/green");
        ENTITIES_SHEEP_RED = register("entities/sheep/red");
        ENTITIES_SHEEP_BLACK = register("entities/sheep/black");
        ENTITIES_BAT = register("entities/bat");
        ENTITIES_SLIME = register("entities/slime");
        ENTITIES_MAGMA_CUBE = register("entities/magma_cube");
        ENTITIES_GHAST = register("entities/ghast");
        ENTITIES_SQUID = register("entities/squid");
        ENTITIES_ENDERMITE = register("entities/endermite");
        ENTITIES_ZOMBIE = register("entities/zombie");
        ENTITIES_ZOMBIE_PIGMAN = register("entities/zombie_pigman");
        ENTITIES_SKELETON = register("entities/skeleton");
        ENTITIES_WITHER_SKELETON = register("entities/wither_skeleton");
        ENTITIES_STRAY = register("entities/stray");
        ENTITIES_HUSK = register("entities/husk");
        ENTITIES_ZOMBIE_VILLAGER = register("entities/zombie_villager");
        ENTITIES_VILLAGER = register("entities/villager");
        ENTITIES_EVOCATION_ILLAGER = register("entities/evocation_illager");
        ENTITIES_VINDICATION_ILLAGER = register("entities/vindication_illager");
        ENTITIES_LLAMA = register("entities/llama");
        ENTITIES_PARROT = register("entities/parrot");
        ENTITIES_VEX = register("entities/vex");
        ENTITIES_ENDER_DRAGON = register("entities/ender_dragon");
        GAMEPLAY_FISHING = register("gameplay/fishing");
        GAMEPLAY_FISHING_JUNK = register("gameplay/fishing/junk");
        GAMEPLAY_FISHING_TREASURE = register("gameplay/fishing/treasure");
        GAMEPLAY_FISHING_FISH = register("gameplay/fishing/fish");
    }
}
