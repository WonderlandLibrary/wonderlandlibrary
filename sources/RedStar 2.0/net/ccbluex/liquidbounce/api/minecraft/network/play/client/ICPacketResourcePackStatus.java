package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\bf\u000020:¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WAction", "Pride"})
public interface ICPacketResourcePackStatus
extends IPacket {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "", "(Ljava/lang/String;I)V", "SUCCESSFULLY_LOADED", "DECLINED", "FAILED_DOWNLOAD", "ACCEPTED", "Pride"})
    public static final class WAction
    extends Enum<WAction> {
        public static final WAction SUCCESSFULLY_LOADED;
        public static final WAction DECLINED;
        public static final WAction FAILED_DOWNLOAD;
        public static final WAction ACCEPTED;
        private static final WAction[] $VALUES;

        static {
            WAction[] wActionArray = new WAction[4];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = SUCCESSFULLY_LOADED = new WAction();
            wActionArray[1] = DECLINED = new WAction();
            wActionArray[2] = FAILED_DOWNLOAD = new WAction();
            wActionArray[3] = ACCEPTED = new WAction();
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
