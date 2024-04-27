package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "", "(Ljava/lang/String;I)V", "PERFORM_RESPAWN", "REQUEST_STATS", "OPEN_INVENTORY_ACHIEVEMENT", "Pride"})
public static final class ICPacketClientStatus$WEnumState
extends Enum<ICPacketClientStatus$WEnumState> {
    public static final ICPacketClientStatus$WEnumState PERFORM_RESPAWN;
    public static final ICPacketClientStatus$WEnumState REQUEST_STATS;
    public static final ICPacketClientStatus$WEnumState OPEN_INVENTORY_ACHIEVEMENT;
    private static final ICPacketClientStatus$WEnumState[] $VALUES;

    static {
        ICPacketClientStatus$WEnumState[] wEnumStateArray = new ICPacketClientStatus$WEnumState[3];
        ICPacketClientStatus$WEnumState[] wEnumStateArray2 = wEnumStateArray;
        wEnumStateArray[0] = PERFORM_RESPAWN = new ICPacketClientStatus$WEnumState();
        wEnumStateArray[1] = REQUEST_STATS = new ICPacketClientStatus$WEnumState();
        wEnumStateArray[2] = OPEN_INVENTORY_ACHIEVEMENT = new ICPacketClientStatus$WEnumState();
        $VALUES = wEnumStateArray;
    }

    public static ICPacketClientStatus$WEnumState[] values() {
        return (ICPacketClientStatus$WEnumState[])$VALUES.clone();
    }

    public static ICPacketClientStatus$WEnumState valueOf(String string) {
        return Enum.valueOf(ICPacketClientStatus$WEnumState.class, string);
    }
}
