package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.IStringSerializable;

/*
 * Exception performing whole class analysis ignored.
 */
public static enum BlockFlower.EnumFlowerType implements IStringSerializable
{
    DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
    POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
    BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
    ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
    HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
    RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
    ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
    WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
    PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
    OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

    private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK;
    private final BlockFlower.EnumFlowerColor blockType;
    private final int meta;
    private final String name;
    private final String unlocalizedName;

    private BlockFlower.EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name) {
        this(blockType, meta, name, name);
    }

    private BlockFlower.EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
        this.blockType = blockType;
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public BlockFlower.EnumFlowerColor getBlockType() {
        return this.blockType;
    }

    public int getMeta() {
        return this.meta;
    }

    public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta) {
        BlockFlower.EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
        if (meta < 0 || meta >= ablockflower$enumflowertype.length) {
            meta = 0;
        }
        return ablockflower$enumflowertype[meta];
    }

    public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor) {
        return TYPES_FOR_BLOCK[flowerColor.ordinal()];
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    static {
        TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
        for (BlockFlower.EnumFlowerColor blockflower$enumflowercolor : BlockFlower.EnumFlowerColor.values()) {
            Collection collection = Collections2.filter((Collection)Lists.newArrayList((Object[])BlockFlower.EnumFlowerType.values()), (Predicate)new /* Unavailable Anonymous Inner Class!! */);
            BlockFlower.EnumFlowerType.TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = (BlockFlower.EnumFlowerType[])collection.toArray((Object[])new BlockFlower.EnumFlowerType[collection.size()]);
        }
    }
}
