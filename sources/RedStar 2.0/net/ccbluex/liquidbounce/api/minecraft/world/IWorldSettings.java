package net.ccbluex.liquidbounce.api.minecraft.world;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\u0000\n\b\bf\u000020:¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings;", "", "WGameType", "Pride"})
public interface IWorldSettings {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "", "(Ljava/lang/String;I)V", "NOT_SET", "SURVIVAL", "CREATIVE", "ADVENTUR", "SPECTATOR", "Pride"})
    public static final class WGameType
    extends Enum<WGameType> {
        public static final WGameType NOT_SET;
        public static final WGameType SURVIVAL;
        public static final WGameType CREATIVE;
        public static final WGameType ADVENTUR;
        public static final WGameType SPECTATOR;
        private static final WGameType[] $VALUES;

        static {
            WGameType[] wGameTypeArray = new WGameType[5];
            WGameType[] wGameTypeArray2 = wGameTypeArray;
            wGameTypeArray[0] = NOT_SET = new WGameType();
            wGameTypeArray[1] = SURVIVAL = new WGameType();
            wGameTypeArray[2] = CREATIVE = new WGameType();
            wGameTypeArray[3] = ADVENTUR = new WGameType();
            wGameTypeArray[4] = SPECTATOR = new WGameType();
            $VALUES = wGameTypeArray;
        }

        public static WGameType[] values() {
            return (WGameType[])$VALUES.clone();
        }

        public static WGameType valueOf(String string) {
            return Enum.valueOf(WGameType.class, string);
        }
    }
}
