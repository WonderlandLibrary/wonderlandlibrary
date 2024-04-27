/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.world;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings;", "", "WGameType", "AtField"})
public interface IWorldSettings {

    public static final class WGameType
    extends Enum {
        public static final /* enum */ WGameType NOT_SET;
        public static final /* enum */ WGameType SURVIVAL;
        private static final WGameType[] $VALUES;
        public static final /* enum */ WGameType SPECTATOR;
        public static final /* enum */ WGameType CREATIVE;
        public static final /* enum */ WGameType ADVENTUR;

        static {
            WGameType[] wGameTypeArray = new WGameType[5];
            WGameType[] wGameTypeArray2 = wGameTypeArray;
            wGameTypeArray[0] = NOT_SET = new WGameType("NOT_SET", 0);
            wGameTypeArray[1] = SURVIVAL = new WGameType("SURVIVAL", 1);
            wGameTypeArray[2] = CREATIVE = new WGameType("CREATIVE", 2);
            wGameTypeArray[3] = ADVENTUR = new WGameType("ADVENTUR", 3);
            wGameTypeArray[4] = SPECTATOR = new WGameType("SPECTATOR", 4);
            $VALUES = wGameTypeArray;
        }

        public static WGameType valueOf(String string) {
            return Enum.valueOf(WGameType.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WGameType() {
            void var2_-1;
            void var1_-1;
        }

        public static WGameType[] values() {
            return (WGameType[])$VALUES.clone();
        }
    }
}

