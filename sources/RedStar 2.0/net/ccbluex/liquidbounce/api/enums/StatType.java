package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/StatType;", "", "(Ljava/lang/String;I)V", "JUMP_STAT", "Pride"})
public final class StatType
extends Enum<StatType> {
    public static final StatType JUMP_STAT;
    private static final StatType[] $VALUES;

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
