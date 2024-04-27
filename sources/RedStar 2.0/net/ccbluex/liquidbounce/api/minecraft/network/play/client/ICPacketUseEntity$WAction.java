package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "", "(Ljava/lang/String;I)V", "INTERACT", "ATTACK", "INTERACT_AT", "Pride"})
public static final class ICPacketUseEntity$WAction
extends Enum<ICPacketUseEntity$WAction> {
    public static final ICPacketUseEntity$WAction INTERACT;
    public static final ICPacketUseEntity$WAction ATTACK;
    public static final ICPacketUseEntity$WAction INTERACT_AT;
    private static final ICPacketUseEntity$WAction[] $VALUES;

    static {
        ICPacketUseEntity$WAction[] wActionArray = new ICPacketUseEntity$WAction[3];
        ICPacketUseEntity$WAction[] wActionArray2 = wActionArray;
        wActionArray[0] = INTERACT = new ICPacketUseEntity$WAction();
        wActionArray[1] = ATTACK = new ICPacketUseEntity$WAction();
        wActionArray[2] = INTERACT_AT = new ICPacketUseEntity$WAction();
        $VALUES = wActionArray;
    }

    public static ICPacketUseEntity$WAction[] values() {
        return (ICPacketUseEntity$WAction[])$VALUES.clone();
    }

    public static ICPacketUseEntity$WAction valueOf(String string) {
        return Enum.valueOf(ICPacketUseEntity$WAction.class, string);
    }
}
