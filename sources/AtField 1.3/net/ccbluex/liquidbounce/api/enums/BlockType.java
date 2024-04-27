/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class BlockType
extends Enum {
    public static final /* enum */ BlockType WODDEN_PRESSURE_PLATE;
    public static final /* enum */ BlockType REDSTONE_BLOCK;
    public static final /* enum */ BlockType REDSTONE_TORCH;
    public static final /* enum */ BlockType COMMAND_BLOCK;
    public static final /* enum */ BlockType LIT_FURNACE;
    public static final /* enum */ BlockType CRAFTING_TABLE;
    public static final /* enum */ BlockType STONE_PRESSURE_PLATE;
    public static final /* enum */ BlockType ENDER_CHEST;
    public static final /* enum */ BlockType DIAMOND_ORE;
    public static final /* enum */ BlockType LAPIS_BLOCK;
    public static final /* enum */ BlockType DISPENSER;
    public static final /* enum */ BlockType CHEST;
    public static final /* enum */ BlockType WATERLILY;
    public static final /* enum */ BlockType EMERALD_ORE;
    public static final /* enum */ BlockType AIR;
    public static final /* enum */ BlockType DIAMOND_BLOCK;
    public static final /* enum */ BlockType TNT;
    public static final /* enum */ BlockType LADDER;
    public static final /* enum */ BlockType GOLD_BLOCK;
    private static final BlockType[] $VALUES;
    public static final /* enum */ BlockType ENCHANTING_TABLE;
    public static final /* enum */ BlockType ANVIL;
    public static final /* enum */ BlockType MOSSY_COBBLESTONE;
    public static final /* enum */ BlockType FLOWING_LAVA;
    public static final /* enum */ BlockType SNOW_LAYER;
    public static final /* enum */ BlockType LAPIS_ORE;
    public static final /* enum */ BlockType WALL_BANNER;
    public static final /* enum */ BlockType BROWN_MUSHROOM_BLOCK;
    public static final /* enum */ BlockType QUARTZ_ORE;
    public static final /* enum */ BlockType DRAGON_EGG;
    public static final /* enum */ BlockType GOLD_ORE;
    public static final /* enum */ BlockType TRAPPED_CHEST;
    public static final /* enum */ BlockType EMERALD_BLOCK;
    public static final /* enum */ BlockType STANDING_BANNER;
    public static final /* enum */ BlockType GLOWSTONE;
    public static final /* enum */ BlockType CLAY;
    public static final /* enum */ BlockType LAVA;
    public static final /* enum */ BlockType REDSTONE_ORE;
    public static final /* enum */ BlockType DROPPER;
    public static final /* enum */ BlockType COAL_ORE;
    public static final /* enum */ BlockType IRON_ORE;
    public static final /* enum */ BlockType WEB;
    public static final /* enum */ BlockType TORCH;
    public static final /* enum */ BlockType BOOKSHELF;
    public static final /* enum */ BlockType NOTEBLOCK;
    public static final /* enum */ BlockType IRON_BLOCK;
    public static final /* enum */ BlockType WATER;
    public static final /* enum */ BlockType COAL_BLOCK;
    public static final /* enum */ BlockType ICE;
    public static final /* enum */ BlockType ICE_PACKED;
    public static final /* enum */ BlockType FLOWING_WATER;
    public static final /* enum */ BlockType SAND;
    public static final /* enum */ BlockType BARRIER;
    public static final /* enum */ BlockType FARMLAND;
    public static final /* enum */ BlockType FURNACE;
    public static final /* enum */ BlockType END_PORTAL_FRAME;
    public static final /* enum */ BlockType MOB_SPAWNER;
    public static final /* enum */ BlockType FIRE;
    public static final /* enum */ BlockType RED_MUSHROOM_BLOCK;

    public static BlockType valueOf(String string) {
        return Enum.valueOf(BlockType.class, string);
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private BlockType() {
        void var2_-1;
        void var1_-1;
    }

    static {
        BlockType[] blockTypeArray = new BlockType[58];
        BlockType[] blockTypeArray2 = blockTypeArray;
        blockTypeArray[0] = ENCHANTING_TABLE = new BlockType("ENCHANTING_TABLE", 0);
        blockTypeArray[1] = CHEST = new BlockType("CHEST", 1);
        blockTypeArray[2] = ENDER_CHEST = new BlockType("ENDER_CHEST", 2);
        blockTypeArray[3] = TRAPPED_CHEST = new BlockType("TRAPPED_CHEST", 3);
        blockTypeArray[4] = ANVIL = new BlockType("ANVIL", 4);
        blockTypeArray[5] = SAND = new BlockType("SAND", 5);
        blockTypeArray[6] = WEB = new BlockType("WEB", 6);
        blockTypeArray[7] = TORCH = new BlockType("TORCH", 7);
        blockTypeArray[8] = CRAFTING_TABLE = new BlockType("CRAFTING_TABLE", 8);
        blockTypeArray[9] = FURNACE = new BlockType("FURNACE", 9);
        blockTypeArray[10] = WATERLILY = new BlockType("WATERLILY", 10);
        blockTypeArray[11] = DISPENSER = new BlockType("DISPENSER", 11);
        blockTypeArray[12] = STONE_PRESSURE_PLATE = new BlockType("STONE_PRESSURE_PLATE", 12);
        blockTypeArray[13] = WODDEN_PRESSURE_PLATE = new BlockType("WODDEN_PRESSURE_PLATE", 13);
        blockTypeArray[14] = TNT = new BlockType("TNT", 14);
        blockTypeArray[15] = STANDING_BANNER = new BlockType("STANDING_BANNER", 15);
        blockTypeArray[16] = WALL_BANNER = new BlockType("WALL_BANNER", 16);
        blockTypeArray[17] = REDSTONE_TORCH = new BlockType("REDSTONE_TORCH", 17);
        blockTypeArray[18] = NOTEBLOCK = new BlockType("NOTEBLOCK", 18);
        blockTypeArray[19] = DROPPER = new BlockType("DROPPER", 19);
        blockTypeArray[20] = SNOW_LAYER = new BlockType("SNOW_LAYER", 20);
        blockTypeArray[21] = AIR = new BlockType("AIR", 21);
        blockTypeArray[22] = ICE_PACKED = new BlockType("ICE_PACKED", 22);
        blockTypeArray[23] = ICE = new BlockType("ICE", 23);
        blockTypeArray[24] = WATER = new BlockType("WATER", 24);
        blockTypeArray[25] = BARRIER = new BlockType("BARRIER", 25);
        blockTypeArray[26] = FLOWING_WATER = new BlockType("FLOWING_WATER", 26);
        blockTypeArray[27] = COAL_ORE = new BlockType("COAL_ORE", 27);
        blockTypeArray[28] = IRON_ORE = new BlockType("IRON_ORE", 28);
        blockTypeArray[29] = GOLD_ORE = new BlockType("GOLD_ORE", 29);
        blockTypeArray[30] = REDSTONE_ORE = new BlockType("REDSTONE_ORE", 30);
        blockTypeArray[31] = LAPIS_ORE = new BlockType("LAPIS_ORE", 31);
        blockTypeArray[32] = DIAMOND_ORE = new BlockType("DIAMOND_ORE", 32);
        blockTypeArray[33] = EMERALD_ORE = new BlockType("EMERALD_ORE", 33);
        blockTypeArray[34] = QUARTZ_ORE = new BlockType("QUARTZ_ORE", 34);
        blockTypeArray[35] = CLAY = new BlockType("CLAY", 35);
        blockTypeArray[36] = GLOWSTONE = new BlockType("GLOWSTONE", 36);
        blockTypeArray[37] = LADDER = new BlockType("LADDER", 37);
        blockTypeArray[38] = COAL_BLOCK = new BlockType("COAL_BLOCK", 38);
        blockTypeArray[39] = IRON_BLOCK = new BlockType("IRON_BLOCK", 39);
        blockTypeArray[40] = GOLD_BLOCK = new BlockType("GOLD_BLOCK", 40);
        blockTypeArray[41] = DIAMOND_BLOCK = new BlockType("DIAMOND_BLOCK", 41);
        blockTypeArray[42] = EMERALD_BLOCK = new BlockType("EMERALD_BLOCK", 42);
        blockTypeArray[43] = REDSTONE_BLOCK = new BlockType("REDSTONE_BLOCK", 43);
        blockTypeArray[44] = LAPIS_BLOCK = new BlockType("LAPIS_BLOCK", 44);
        blockTypeArray[45] = FIRE = new BlockType("FIRE", 45);
        blockTypeArray[46] = MOSSY_COBBLESTONE = new BlockType("MOSSY_COBBLESTONE", 46);
        blockTypeArray[47] = MOB_SPAWNER = new BlockType("MOB_SPAWNER", 47);
        blockTypeArray[48] = END_PORTAL_FRAME = new BlockType("END_PORTAL_FRAME", 48);
        blockTypeArray[49] = BOOKSHELF = new BlockType("BOOKSHELF", 49);
        blockTypeArray[50] = COMMAND_BLOCK = new BlockType("COMMAND_BLOCK", 50);
        blockTypeArray[51] = LAVA = new BlockType("LAVA", 51);
        blockTypeArray[52] = FLOWING_LAVA = new BlockType("FLOWING_LAVA", 52);
        blockTypeArray[53] = LIT_FURNACE = new BlockType("LIT_FURNACE", 53);
        blockTypeArray[54] = DRAGON_EGG = new BlockType("DRAGON_EGG", 54);
        blockTypeArray[55] = BROWN_MUSHROOM_BLOCK = new BlockType("BROWN_MUSHROOM_BLOCK", 55);
        blockTypeArray[56] = RED_MUSHROOM_BLOCK = new BlockType("RED_MUSHROOM_BLOCK", 56);
        blockTypeArray[57] = FARMLAND = new BlockType("FARMLAND", 57);
        $VALUES = blockTypeArray;
    }

    public static BlockType[] values() {
        return (BlockType[])$VALUES.clone();
    }
}

