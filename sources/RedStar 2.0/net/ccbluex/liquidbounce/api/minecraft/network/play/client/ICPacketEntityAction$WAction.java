package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "", "(Ljava/lang/String;I)V", "START_SNEAKING", "STOP_SNEAKING", "STOP_SLEEPING", "START_SPRINTING", "STOP_SPRINTING", "OPEN_INVENTORY", "Pride"})
public static final class ICPacketEntityAction$WAction
extends Enum<ICPacketEntityAction$WAction> {
    public static final ICPacketEntityAction$WAction START_SNEAKING;
    public static final ICPacketEntityAction$WAction STOP_SNEAKING;
    public static final ICPacketEntityAction$WAction STOP_SLEEPING;
    public static final ICPacketEntityAction$WAction START_SPRINTING;
    public static final ICPacketEntityAction$WAction STOP_SPRINTING;
    public static final ICPacketEntityAction$WAction OPEN_INVENTORY;
    private static final ICPacketEntityAction$WAction[] $VALUES;

    static {
        ICPacketEntityAction$WAction[] wActionArray = new ICPacketEntityAction$WAction[6];
        ICPacketEntityAction$WAction[] wActionArray2 = wActionArray;
        wActionArray[0] = START_SNEAKING = new ICPacketEntityAction$WAction();
        wActionArray[1] = STOP_SNEAKING = new ICPacketEntityAction$WAction();
        wActionArray[2] = STOP_SLEEPING = new ICPacketEntityAction$WAction();
        wActionArray[3] = START_SPRINTING = new ICPacketEntityAction$WAction();
        wActionArray[4] = STOP_SPRINTING = new ICPacketEntityAction$WAction();
        wActionArray[5] = OPEN_INVENTORY = new ICPacketEntityAction$WAction();
        $VALUES = wActionArray;
    }

    public static ICPacketEntityAction$WAction[] values() {
        return (ICPacketEntityAction$WAction[])$VALUES.clone();
    }

    public static ICPacketEntityAction$WAction valueOf(String string) {
        return Enum.valueOf(ICPacketEntityAction$WAction.class, string);
    }
}
