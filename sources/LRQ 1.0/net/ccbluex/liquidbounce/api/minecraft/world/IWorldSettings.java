/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.world;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings;", "", "WGameType", "LiquidSense"})
public interface IWorldSettings {

    public static final class WGameType
    extends Enum<WGameType> {
        public static final /* enum */ WGameType NOT_SET;
        public static final /* enum */ WGameType SURVIVAL;
        public static final /* enum */ WGameType CREATIVE;
        public static final /* enum */ WGameType ADVENTUR;
        public static final /* enum */ WGameType SPECTATOR;
        private static final /* synthetic */ WGameType[] $VALUES;

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

