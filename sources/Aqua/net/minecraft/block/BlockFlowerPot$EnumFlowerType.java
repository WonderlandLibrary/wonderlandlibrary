package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockFlowerPot.EnumFlowerType implements IStringSerializable
{
    EMPTY("empty"),
    POPPY("rose"),
    BLUE_ORCHID("blue_orchid"),
    ALLIUM("allium"),
    HOUSTONIA("houstonia"),
    RED_TULIP("red_tulip"),
    ORANGE_TULIP("orange_tulip"),
    WHITE_TULIP("white_tulip"),
    PINK_TULIP("pink_tulip"),
    OXEYE_DAISY("oxeye_daisy"),
    DANDELION("dandelion"),
    OAK_SAPLING("oak_sapling"),
    SPRUCE_SAPLING("spruce_sapling"),
    BIRCH_SAPLING("birch_sapling"),
    JUNGLE_SAPLING("jungle_sapling"),
    ACACIA_SAPLING("acacia_sapling"),
    DARK_OAK_SAPLING("dark_oak_sapling"),
    MUSHROOM_RED("mushroom_red"),
    MUSHROOM_BROWN("mushroom_brown"),
    DEAD_BUSH("dead_bush"),
    FERN("fern"),
    CACTUS("cactus");

    private final String name;

    private BlockFlowerPot.EnumFlowerType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
