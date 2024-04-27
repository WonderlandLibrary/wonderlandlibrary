package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\bf\u000020:¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WEnumState", "Pride"})
public interface ICPacketClientStatus
extends IPacket {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "", "(Ljava/lang/String;I)V", "PERFORM_RESPAWN", "REQUEST_STATS", "OPEN_INVENTORY_ACHIEVEMENT", "Pride"})
    public static final class WEnumState
    extends Enum<WEnumState> {
        public static final WEnumState PERFORM_RESPAWN;
        public static final WEnumState REQUEST_STATS;
        public static final WEnumState OPEN_INVENTORY_ACHIEVEMENT;
        private static final WEnumState[] $VALUES;

        static {
            WEnumState[] wEnumStateArray = new WEnumState[3];
            WEnumState[] wEnumStateArray2 = wEnumStateArray;
            wEnumStateArray[0] = PERFORM_RESPAWN = new WEnumState();
            wEnumStateArray[1] = REQUEST_STATS = new WEnumState();
            wEnumStateArray[2] = OPEN_INVENTORY_ACHIEVEMENT = new WEnumState();
            $VALUES = wEnumStateArray;
        }

        public static WEnumState[] values() {
            return (WEnumState[])$VALUES.clone();
        }

        public static WEnumState valueOf(String string) {
            return Enum.valueOf(WEnumState.class, string);
        }
    }
}
