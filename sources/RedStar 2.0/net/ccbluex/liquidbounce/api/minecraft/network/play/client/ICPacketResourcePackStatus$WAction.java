package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "", "(Ljava/lang/String;I)V", "SUCCESSFULLY_LOADED", "DECLINED", "FAILED_DOWNLOAD", "ACCEPTED", "Pride"})
public static final class ICPacketResourcePackStatus$WAction
extends Enum<ICPacketResourcePackStatus$WAction> {
    public static final ICPacketResourcePackStatus$WAction SUCCESSFULLY_LOADED;
    public static final ICPacketResourcePackStatus$WAction DECLINED;
    public static final ICPacketResourcePackStatus$WAction FAILED_DOWNLOAD;
    public static final ICPacketResourcePackStatus$WAction ACCEPTED;
    private static final ICPacketResourcePackStatus$WAction[] $VALUES;

    static {
        ICPacketResourcePackStatus$WAction[] wActionArray = new ICPacketResourcePackStatus$WAction[4];
        ICPacketResourcePackStatus$WAction[] wActionArray2 = wActionArray;
        wActionArray[0] = SUCCESSFULLY_LOADED = new ICPacketResourcePackStatus$WAction();
        wActionArray[1] = DECLINED = new ICPacketResourcePackStatus$WAction();
        wActionArray[2] = FAILED_DOWNLOAD = new ICPacketResourcePackStatus$WAction();
        wActionArray[3] = ACCEPTED = new ICPacketResourcePackStatus$WAction();
        $VALUES = wActionArray;
    }

    public static ICPacketResourcePackStatus$WAction[] values() {
        return (ICPacketResourcePackStatus$WAction[])$VALUES.clone();
    }

    public static ICPacketResourcePackStatus$WAction valueOf(String string) {
        return Enum.valueOf(ICPacketResourcePackStatus$WAction.class, string);
    }
}
