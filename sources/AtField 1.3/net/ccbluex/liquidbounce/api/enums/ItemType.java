/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class ItemType
extends Enum {
    public static final /* enum */ ItemType POTION_ITEM;
    public static final /* enum */ ItemType ARMOR_STAND;
    public static final /* enum */ ItemType BOWL;
    public static final /* enum */ ItemType LAVA_BUCKET;
    public static final /* enum */ ItemType MUSHROOM_STEW;
    private static final ItemType[] $VALUES;
    public static final /* enum */ ItemType WATER_BUCKET;
    public static final /* enum */ ItemType COMMAND_BLOCK_MINECART;
    public static final /* enum */ ItemType WRITABLE_BOOK;
    public static final /* enum */ ItemType SKULL;
    public static final /* enum */ ItemType FLINT_AND_STEEL;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private ItemType() {
        void var2_-1;
        void var1_-1;
    }

    public static ItemType valueOf(String string) {
        return Enum.valueOf(ItemType.class, string);
    }

    static {
        ItemType[] itemTypeArray = new ItemType[10];
        ItemType[] itemTypeArray2 = itemTypeArray;
        itemTypeArray[0] = MUSHROOM_STEW = new ItemType("MUSHROOM_STEW", 0);
        itemTypeArray[1] = BOWL = new ItemType("BOWL", 1);
        itemTypeArray[2] = FLINT_AND_STEEL = new ItemType("FLINT_AND_STEEL", 2);
        itemTypeArray[3] = LAVA_BUCKET = new ItemType("LAVA_BUCKET", 3);
        itemTypeArray[4] = WRITABLE_BOOK = new ItemType("WRITABLE_BOOK", 4);
        itemTypeArray[5] = WATER_BUCKET = new ItemType("WATER_BUCKET", 5);
        itemTypeArray[6] = COMMAND_BLOCK_MINECART = new ItemType("COMMAND_BLOCK_MINECART", 6);
        itemTypeArray[7] = POTION_ITEM = new ItemType("POTION_ITEM", 7);
        itemTypeArray[8] = SKULL = new ItemType("SKULL", 8);
        itemTypeArray[9] = ARMOR_STAND = new ItemType("ARMOR_STAND", 9);
        $VALUES = itemTypeArray;
    }

    public static ItemType[] values() {
        return (ItemType[])$VALUES.clone();
    }
}

