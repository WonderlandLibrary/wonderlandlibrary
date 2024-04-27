/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0003\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003\u00a8\u0006\u0004"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/StatType;", "", "(Ljava/lang/String;I)V", "JUMP_STAT", "LiKingSense"})
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

