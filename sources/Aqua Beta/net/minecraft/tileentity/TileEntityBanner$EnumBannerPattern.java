package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public static enum TileEntityBanner.EnumBannerPattern {
    BASE("base", "b"),
    SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "),
    SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"),
    SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "),
    SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "),
    STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"),
    STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "),
    STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "),
    STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"),
    STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "),
    STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "),
    STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"),
    STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "),
    STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "),
    CROSS("cross", "cr", "# #", " # ", "# #"),
    STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "),
    TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"),
    TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "),
    TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "),
    TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "),
    DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "),
    DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"),
    DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "),
    DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "),
    CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "),
    RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "),
    HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "),
    HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "),
    HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"),
    HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"),
    BORDER("border", "bo", "###", "# #", "###"),
    CURLY_BORDER("curly_border", "cbo", new ItemStack(Blocks.vine)),
    CREEPER("creeper", "cre", new ItemStack(Items.skull, 1, 4)),
    GRADIENT("gradient", "gra", "# #", " # ", " # "),
    GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
    BRICKS("bricks", "bri", new ItemStack(Blocks.brick_block)),
    SKULL("skull", "sku", new ItemStack(Items.skull, 1, 1)),
    FLOWER("flower", "flo", new ItemStack((Block)Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
    MOJANG("mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));

    private String patternName;
    private String patternID;
    private String[] craftingLayers = new String[3];
    private ItemStack patternCraftingStack;

    private TileEntityBanner.EnumBannerPattern(String name, String id) {
        this.patternName = name;
        this.patternID = id;
    }

    private TileEntityBanner.EnumBannerPattern(String name, String id, ItemStack craftingItem) {
        this(name, id);
        this.patternCraftingStack = craftingItem;
    }

    private TileEntityBanner.EnumBannerPattern(String name, String id, String craftingTop, String craftingMid, String craftingBot) {
        this(name, id);
        this.craftingLayers[0] = craftingTop;
        this.craftingLayers[1] = craftingMid;
        this.craftingLayers[2] = craftingBot;
    }

    public String getPatternName() {
        return this.patternName;
    }

    public String getPatternID() {
        return this.patternID;
    }

    public String[] getCraftingLayers() {
        return this.craftingLayers;
    }

    public boolean hasValidCrafting() {
        return this.patternCraftingStack != null || this.craftingLayers[0] != null;
    }

    public boolean hasCraftingStack() {
        return this.patternCraftingStack != null;
    }

    public ItemStack getCraftingStack() {
        return this.patternCraftingStack;
    }

    public static TileEntityBanner.EnumBannerPattern getPatternByID(String id) {
        for (TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : TileEntityBanner.EnumBannerPattern.values()) {
            if (!tileentitybanner$enumbannerpattern.patternID.equals((Object)id)) continue;
            return tileentitybanner$enumbannerpattern;
        }
        return null;
    }
}
