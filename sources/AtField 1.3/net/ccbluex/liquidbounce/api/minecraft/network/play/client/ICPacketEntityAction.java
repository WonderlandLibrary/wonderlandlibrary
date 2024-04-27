/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WAction", "AtField"})
public interface ICPacketEntityAction
extends IPacket {

    public static final class WAction
    extends Enum {
        private static final WAction[] $VALUES;
        public static final /* enum */ WAction STOP_SPRINTING;
        public static final /* enum */ WAction STOP_SLEEPING;
        public static final /* enum */ WAction OPEN_INVENTORY;
        public static final /* enum */ WAction START_SNEAKING;
        public static final /* enum */ WAction STOP_SNEAKING;
        public static final /* enum */ WAction START_SPRINTING;

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WAction() {
            void var2_-1;
            void var1_-1;
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }

        static {
            WAction[] wActionArray = new WAction[6];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = START_SNEAKING = new WAction("START_SNEAKING", 0);
            wActionArray[1] = STOP_SNEAKING = new WAction("STOP_SNEAKING", 1);
            wActionArray[2] = STOP_SLEEPING = new WAction("STOP_SLEEPING", 2);
            wActionArray[3] = START_SPRINTING = new WAction("START_SPRINTING", 3);
            wActionArray[4] = STOP_SPRINTING = new WAction("STOP_SPRINTING", 4);
            wActionArray[5] = OPEN_INVENTORY = new WAction("OPEN_INVENTORY", 5);
            $VALUES = wActionArray;
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }
    }
}

