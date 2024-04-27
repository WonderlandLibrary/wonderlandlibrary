/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\f\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/ItemType;", "", "(Ljava/lang/String;I)V", "MUSHROOM_STEW", "BOWL", "FLINT_AND_STEEL", "LAVA_BUCKET", "WRITABLE_BOOK", "WATER_BUCKET", "COMMAND_BLOCK_MINECART", "POTION_ITEM", "SKULL", "ARMOR_STAND", "LiKingSense"})
public final class ItemType
extends Enum<ItemType> {
    public static final /* enum */ ItemType MUSHROOM_STEW;
    public static final /* enum */ ItemType BOWL;
    public static final /* enum */ ItemType FLINT_AND_STEEL;
    public static final /* enum */ ItemType LAVA_BUCKET;
    public static final /* enum */ ItemType WRITABLE_BOOK;
    public static final /* enum */ ItemType WATER_BUCKET;
    public static final /* enum */ ItemType COMMAND_BLOCK_MINECART;
    public static final /* enum */ ItemType POTION_ITEM;
    public static final /* enum */ ItemType SKULL;
    public static final /* enum */ ItemType ARMOR_STAND;
    private static final /* synthetic */ ItemType[] $VALUES;

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

