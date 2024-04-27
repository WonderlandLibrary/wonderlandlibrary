package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\f\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\tj\b\nj\bj\b\f¨\r"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/ItemType;", "", "(Ljava/lang/String;I)V", "MUSHROOM_STEW", "BOWL", "FLINT_AND_STEEL", "LAVA_BUCKET", "WRITABLE_BOOK", "WATER_BUCKET", "COMMAND_BLOCK_MINECART", "POTION_ITEM", "SKULL", "ARMOR_STAND", "Pride"})
public final class ItemType
extends Enum<ItemType> {
    public static final ItemType MUSHROOM_STEW;
    public static final ItemType BOWL;
    public static final ItemType FLINT_AND_STEEL;
    public static final ItemType LAVA_BUCKET;
    public static final ItemType WRITABLE_BOOK;
    public static final ItemType WATER_BUCKET;
    public static final ItemType COMMAND_BLOCK_MINECART;
    public static final ItemType POTION_ITEM;
    public static final ItemType SKULL;
    public static final ItemType ARMOR_STAND;
    private static final ItemType[] $VALUES;

    static {
        ItemType[] itemTypeArray = new ItemType[10];
        ItemType[] itemTypeArray2 = itemTypeArray;
        itemTypeArray[0] = MUSHROOM_STEW = new ItemType();
        itemTypeArray[1] = BOWL = new ItemType();
        itemTypeArray[2] = FLINT_AND_STEEL = new ItemType();
        itemTypeArray[3] = LAVA_BUCKET = new ItemType();
        itemTypeArray[4] = WRITABLE_BOOK = new ItemType();
        itemTypeArray[5] = WATER_BUCKET = new ItemType();
        itemTypeArray[6] = COMMAND_BLOCK_MINECART = new ItemType();
        itemTypeArray[7] = POTION_ITEM = new ItemType();
        itemTypeArray[8] = SKULL = new ItemType();
        itemTypeArray[9] = ARMOR_STAND = new ItemType();
        $VALUES = itemTypeArray;
    }

    public static ItemType[] values() {
        return (ItemType[])$VALUES.clone();
    }

    public static ItemType valueOf(String string) {
        return Enum.valueOf(ItemType.class, string);
    }
}
