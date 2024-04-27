package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\bf\u000020:¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WAction", "Pride"})
public interface ICPacketEntityAction
extends IPacket {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "", "(Ljava/lang/String;I)V", "START_SNEAKING", "STOP_SNEAKING", "STOP_SLEEPING", "START_SPRINTING", "STOP_SPRINTING", "OPEN_INVENTORY", "Pride"})
    public static final class WAction
    extends Enum<WAction> {
        public static final WAction START_SNEAKING;
        public static final WAction STOP_SNEAKING;
        public static final WAction STOP_SLEEPING;
        public static final WAction START_SPRINTING;
        public static final WAction STOP_SPRINTING;
        public static final WAction OPEN_INVENTORY;
        private static final WAction[] $VALUES;

        static {
            WAction[] wActionArray = new WAction[6];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = START_SNEAKING = new WAction();
            wActionArray[1] = STOP_SNEAKING = new WAction();
            wActionArray[2] = STOP_SLEEPING = new WAction();
            wActionArray[3] = START_SPRINTING = new WAction();
            wActionArray[4] = STOP_SPRINTING = new WAction();
            wActionArray[5] = OPEN_INVENTORY = new WAction();
            $VALUES = wActionArray;
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }
    }
}
