package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\t\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "", "(Ljava/lang/String;I)V", "START_DESTROY_BLOCK", "ABORT_DESTROY_BLOCK", "STOP_DESTROY_BLOCK", "DROP_ALL_ITEMS", "DROP_ITEM", "RELEASE_USE_ITEM", "SWAP_HELD_ITEMS", "Pride"})
public static final class ICPacketPlayerDigging$WAction
extends Enum<ICPacketPlayerDigging$WAction> {
    public static final ICPacketPlayerDigging$WAction START_DESTROY_BLOCK;
    public static final ICPacketPlayerDigging$WAction ABORT_DESTROY_BLOCK;
    public static final ICPacketPlayerDigging$WAction STOP_DESTROY_BLOCK;
    public static final ICPacketPlayerDigging$WAction DROP_ALL_ITEMS;
    public static final ICPacketPlayerDigging$WAction DROP_ITEM;
    public static final ICPacketPlayerDigging$WAction RELEASE_USE_ITEM;
    public static final ICPacketPlayerDigging$WAction SWAP_HELD_ITEMS;
    private static final ICPacketPlayerDigging$WAction[] $VALUES;

    static {
        ICPacketPlayerDigging$WAction[] wActionArray = new ICPacketPlayerDigging$WAction[7];
        ICPacketPlayerDigging$WAction[] wActionArray2 = wActionArray;
        wActionArray[0] = START_DESTROY_BLOCK = new ICPacketPlayerDigging$WAction();
        wActionArray[1] = ABORT_DESTROY_BLOCK = new ICPacketPlayerDigging$WAction();
        wActionArray[2] = STOP_DESTROY_BLOCK = new ICPacketPlayerDigging$WAction();
        wActionArray[3] = DROP_ALL_ITEMS = new ICPacketPlayerDigging$WAction();
        wActionArray[4] = DROP_ITEM = new ICPacketPlayerDigging$WAction();
        wActionArray[5] = RELEASE_USE_ITEM = new ICPacketPlayerDigging$WAction();
        wActionArray[6] = SWAP_HELD_ITEMS = new ICPacketPlayerDigging$WAction();
        $VALUES = wActionArray;
    }

    public static ICPacketPlayerDigging$WAction[] values() {
        return (ICPacketPlayerDigging$WAction[])$VALUES.clone();
    }

    public static ICPacketPlayerDigging$WAction valueOf(String string) {
        return Enum.valueOf(ICPacketPlayerDigging$WAction.class, string);
    }
}
