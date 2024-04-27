/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.event;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "", "WAction", "AtField"})
public interface IClickEvent {

    public static final class WAction
    extends Enum {
        public static final /* enum */ WAction OPEN_URL;
        private static final WAction[] $VALUES;

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WAction() {
            void var2_-1;
            void var1_-1;
        }

        static {
            WAction[] wActionArray = new WAction[1];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = OPEN_URL = new WAction("OPEN_URL", 0);
            $VALUES = wActionArray;
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }
    }
}

