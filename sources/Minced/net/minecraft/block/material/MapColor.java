// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.material;

import net.minecraft.item.EnumDyeColor;

public class MapColor
{
    public static final MapColor[] COLORS;
    public static final MapColor[] BLOCK_COLORS;
    public static final MapColor AIR;
    public static final MapColor GRASS;
    public static final MapColor SAND;
    public static final MapColor CLOTH;
    public static final MapColor TNT;
    public static final MapColor ICE;
    public static final MapColor IRON;
    public static final MapColor FOLIAGE;
    public static final MapColor SNOW;
    public static final MapColor CLAY;
    public static final MapColor DIRT;
    public static final MapColor STONE;
    public static final MapColor WATER;
    public static final MapColor WOOD;
    public static final MapColor QUARTZ;
    public static final MapColor ADOBE;
    public static final MapColor MAGENTA;
    public static final MapColor LIGHT_BLUE;
    public static final MapColor YELLOW;
    public static final MapColor LIME;
    public static final MapColor PINK;
    public static final MapColor GRAY;
    public static final MapColor SILVER;
    public static final MapColor CYAN;
    public static final MapColor PURPLE;
    public static final MapColor BLUE;
    public static final MapColor BROWN;
    public static final MapColor GREEN;
    public static final MapColor RED;
    public static final MapColor BLACK;
    public static final MapColor GOLD;
    public static final MapColor DIAMOND;
    public static final MapColor LAPIS;
    public static final MapColor EMERALD;
    public static final MapColor OBSIDIAN;
    public static final MapColor NETHERRACK;
    public static final MapColor WHITE_STAINED_HARDENED_CLAY;
    public static final MapColor ORANGE_STAINED_HARDENED_CLAY;
    public static final MapColor MAGENTA_STAINED_HARDENED_CLAY;
    public static final MapColor LIGHT_BLUE_STAINED_HARDENED_CLAY;
    public static final MapColor YELLOW_STAINED_HARDENED_CLAY;
    public static final MapColor LIME_STAINED_HARDENED_CLAY;
    public static final MapColor PINK_STAINED_HARDENED_CLAY;
    public static final MapColor GRAY_STAINED_HARDENED_CLAY;
    public static final MapColor SILVER_STAINED_HARDENED_CLAY;
    public static final MapColor CYAN_STAINED_HARDENED_CLAY;
    public static final MapColor PURPLE_STAINED_HARDENED_CLAY;
    public static final MapColor BLUE_STAINED_HARDENED_CLAY;
    public static final MapColor BROWN_STAINED_HARDENED_CLAY;
    public static final MapColor GREEN_STAINED_HARDENED_CLAY;
    public static final MapColor RED_STAINED_HARDENED_CLAY;
    public static final MapColor BLACK_STAINED_HARDENED_CLAY;
    public int colorValue;
    public final int colorIndex;
    
    private MapColor(final int index, final int color) {
        if (index >= 0 && index <= 63) {
            this.colorIndex = index;
            this.colorValue = color;
            MapColor.COLORS[index] = this;
            return;
        }
        throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
    }
    
    public int getMapColor(final int index) {
        int i = 220;
        if (index == 3) {
            i = 135;
        }
        if (index == 2) {
            i = 255;
        }
        if (index == 1) {
            i = 220;
        }
        if (index == 0) {
            i = 180;
        }
        final int j = (this.colorValue >> 16 & 0xFF) * i / 255;
        final int k = (this.colorValue >> 8 & 0xFF) * i / 255;
        final int l = (this.colorValue & 0xFF) * i / 255;
        return 0xFF000000 | j << 16 | k << 8 | l;
    }
    
    public static MapColor getBlockColor(final EnumDyeColor dyeColorIn) {
        return MapColor.BLOCK_COLORS[dyeColorIn.getMetadata()];
    }
    
    static {
        COLORS = new MapColor[64];
        BLOCK_COLORS = new MapColor[16];
        AIR = new MapColor(0, 0);
        GRASS = new MapColor(1, 8368696);
        SAND = new MapColor(2, 16247203);
        CLOTH = new MapColor(3, 13092807);
        TNT = new MapColor(4, 16711680);
        ICE = new MapColor(5, 10526975);
        IRON = new MapColor(6, 10987431);
        FOLIAGE = new MapColor(7, 31744);
        SNOW = new MapColor(8, 16777215);
        CLAY = new MapColor(9, 10791096);
        DIRT = new MapColor(10, 9923917);
        STONE = new MapColor(11, 7368816);
        WATER = new MapColor(12, 4210943);
        WOOD = new MapColor(13, 9402184);
        QUARTZ = new MapColor(14, 16776437);
        ADOBE = new MapColor(15, 14188339);
        MAGENTA = new MapColor(16, 11685080);
        LIGHT_BLUE = new MapColor(17, 6724056);
        YELLOW = new MapColor(18, 15066419);
        LIME = new MapColor(19, 8375321);
        PINK = new MapColor(20, 15892389);
        GRAY = new MapColor(21, 5000268);
        SILVER = new MapColor(22, 10066329);
        CYAN = new MapColor(23, 5013401);
        PURPLE = new MapColor(24, 8339378);
        BLUE = new MapColor(25, 3361970);
        BROWN = new MapColor(26, 6704179);
        GREEN = new MapColor(27, 6717235);
        RED = new MapColor(28, 10040115);
        BLACK = new MapColor(29, 1644825);
        GOLD = new MapColor(30, 16445005);
        DIAMOND = new MapColor(31, 6085589);
        LAPIS = new MapColor(32, 4882687);
        EMERALD = new MapColor(33, 55610);
        OBSIDIAN = new MapColor(34, 8476209);
        NETHERRACK = new MapColor(35, 7340544);
        WHITE_STAINED_HARDENED_CLAY = new MapColor(36, 13742497);
        ORANGE_STAINED_HARDENED_CLAY = new MapColor(37, 10441252);
        MAGENTA_STAINED_HARDENED_CLAY = new MapColor(38, 9787244);
        LIGHT_BLUE_STAINED_HARDENED_CLAY = new MapColor(39, 7367818);
        YELLOW_STAINED_HARDENED_CLAY = new MapColor(40, 12223780);
        LIME_STAINED_HARDENED_CLAY = new MapColor(41, 6780213);
        PINK_STAINED_HARDENED_CLAY = new MapColor(42, 10505550);
        GRAY_STAINED_HARDENED_CLAY = new MapColor(43, 3746083);
        SILVER_STAINED_HARDENED_CLAY = new MapColor(44, 8874850);
        CYAN_STAINED_HARDENED_CLAY = new MapColor(45, 5725276);
        PURPLE_STAINED_HARDENED_CLAY = new MapColor(46, 8014168);
        BLUE_STAINED_HARDENED_CLAY = new MapColor(47, 4996700);
        BROWN_STAINED_HARDENED_CLAY = new MapColor(48, 4993571);
        GREEN_STAINED_HARDENED_CLAY = new MapColor(49, 5001770);
        RED_STAINED_HARDENED_CLAY = new MapColor(50, 9321518);
        BLACK_STAINED_HARDENED_CLAY = new MapColor(51, 2430480);
        MapColor.BLOCK_COLORS[EnumDyeColor.WHITE.getMetadata()] = MapColor.SNOW;
        MapColor.BLOCK_COLORS[EnumDyeColor.ORANGE.getMetadata()] = MapColor.ADOBE;
        MapColor.BLOCK_COLORS[EnumDyeColor.MAGENTA.getMetadata()] = MapColor.MAGENTA;
        MapColor.BLOCK_COLORS[EnumDyeColor.LIGHT_BLUE.getMetadata()] = MapColor.LIGHT_BLUE;
        MapColor.BLOCK_COLORS[EnumDyeColor.YELLOW.getMetadata()] = MapColor.YELLOW;
        MapColor.BLOCK_COLORS[EnumDyeColor.LIME.getMetadata()] = MapColor.LIME;
        MapColor.BLOCK_COLORS[EnumDyeColor.PINK.getMetadata()] = MapColor.PINK;
        MapColor.BLOCK_COLORS[EnumDyeColor.GRAY.getMetadata()] = MapColor.GRAY;
        MapColor.BLOCK_COLORS[EnumDyeColor.SILVER.getMetadata()] = MapColor.SILVER;
        MapColor.BLOCK_COLORS[EnumDyeColor.CYAN.getMetadata()] = MapColor.CYAN;
        MapColor.BLOCK_COLORS[EnumDyeColor.PURPLE.getMetadata()] = MapColor.PURPLE;
        MapColor.BLOCK_COLORS[EnumDyeColor.BLUE.getMetadata()] = MapColor.BLUE;
        MapColor.BLOCK_COLORS[EnumDyeColor.BROWN.getMetadata()] = MapColor.BROWN;
        MapColor.BLOCK_COLORS[EnumDyeColor.GREEN.getMetadata()] = MapColor.GREEN;
        MapColor.BLOCK_COLORS[EnumDyeColor.RED.getMetadata()] = MapColor.RED;
        MapColor.BLOCK_COLORS[EnumDyeColor.BLACK.getMetadata()] = MapColor.BLACK;
    }
}
