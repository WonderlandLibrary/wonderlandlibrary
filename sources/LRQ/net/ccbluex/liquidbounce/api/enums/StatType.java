/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class StatType
extends Enum<StatType> {
    public static final /* enum */ StatType JUMP_STAT;
    private static final /* synthetic */ StatType[] $VALUES;

    static {
        StatType[] statTypeArray = new StatType[1];
        StatType[] statTypeArray2 = statTypeArray;
        statTypeArray[0] = JUMP_STAT = new StatType();
        $VALUES = statTypeArray;
    }

    public static StatType[] values() {
        return (StatType[])$VALUES.clone();
    }

    public static StatType valueOf(String string) {
        return Enum.valueOf(StatType.class, string);
    }
}

