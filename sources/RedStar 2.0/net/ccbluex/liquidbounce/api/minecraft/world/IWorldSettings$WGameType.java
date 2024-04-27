package net.ccbluex.liquidbounce.api.minecraft.world;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "", "(Ljava/lang/String;I)V", "NOT_SET", "SURVIVAL", "CREATIVE", "ADVENTUR", "SPECTATOR", "Pride"})
public static final class IWorldSettings$WGameType
extends Enum<IWorldSettings$WGameType> {
    public static final IWorldSettings$WGameType NOT_SET;
    public static final IWorldSettings$WGameType SURVIVAL;
    public static final IWorldSettings$WGameType CREATIVE;
    public static final IWorldSettings$WGameType ADVENTUR;
    public static final IWorldSettings$WGameType SPECTATOR;
    private static final IWorldSettings$WGameType[] $VALUES;

    static {
        IWorldSettings$WGameType[] wGameTypeArray = new IWorldSettings$WGameType[5];
        IWorldSettings$WGameType[] wGameTypeArray2 = wGameTypeArray;
        wGameTypeArray[0] = NOT_SET = new IWorldSettings$WGameType();
        wGameTypeArray[1] = SURVIVAL = new IWorldSettings$WGameType();
        wGameTypeArray[2] = CREATIVE = new IWorldSettings$WGameType();
        wGameTypeArray[3] = ADVENTUR = new IWorldSettings$WGameType();
        wGameTypeArray[4] = SPECTATOR = new IWorldSettings$WGameType();
        $VALUES = wGameTypeArray;
    }

    public static IWorldSettings$WGameType[] values() {
        return (IWorldSettings$WGameType[])$VALUES.clone();
    }

    public static IWorldSettings$WGameType valueOf(String string) {
        return Enum.valueOf(IWorldSettings$WGameType.class, string);
    }
}
