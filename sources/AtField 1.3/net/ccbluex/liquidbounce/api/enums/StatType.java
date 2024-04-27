/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class StatType
extends Enum {
    private static final StatType[] $VALUES;
    public static final /* enum */ StatType JUMP_STAT;

    public static StatType[] values() {
        return (StatType[])$VALUES.clone();
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private StatType() {
        void var2_-1;
        void var1_-1;
    }

    public static StatType valueOf(String string) {
        return Enum.valueOf(StatType.class, string);
    }

    static {
        StatType[] statTypeArray = new StatType[1];
        StatType[] statTypeArray2 = statTypeArray;
        statTypeArray[0] = JUMP_STAT = new StatType("JUMP_STAT", 0);
        $VALUES = statTypeArray;
    }
}

