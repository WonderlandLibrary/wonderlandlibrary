package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020:R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\r¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "getAction", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "WAction", "Pride"})
public interface ICPacketPlayerDigging
extends IPacket {
    @NotNull
    public WBlockPos getPosition();

    @NotNull
    public IEnumFacing getFacing();

    @NotNull
    public WAction getAction();

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\t\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "", "(Ljava/lang/String;I)V", "START_DESTROY_BLOCK", "ABORT_DESTROY_BLOCK", "STOP_DESTROY_BLOCK", "DROP_ALL_ITEMS", "DROP_ITEM", "RELEASE_USE_ITEM", "SWAP_HELD_ITEMS", "Pride"})
    public static final class WAction
    extends Enum<WAction> {
        public static final WAction START_DESTROY_BLOCK;
        public static final WAction ABORT_DESTROY_BLOCK;
        public static final WAction STOP_DESTROY_BLOCK;
        public static final WAction DROP_ALL_ITEMS;
        public static final WAction DROP_ITEM;
        public static final WAction RELEASE_USE_ITEM;
        public static final WAction SWAP_HELD_ITEMS;
        private static final WAction[] $VALUES;

        static {
            WAction[] wActionArray = new WAction[7];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = START_DESTROY_BLOCK = new WAction();
            wActionArray[1] = ABORT_DESTROY_BLOCK = new WAction();
            wActionArray[2] = STOP_DESTROY_BLOCK = new WAction();
            wActionArray[3] = DROP_ALL_ITEMS = new WAction();
            wActionArray[4] = DROP_ITEM = new WAction();
            wActionArray[5] = RELEASE_USE_ITEM = new WAction();
            wActionArray[6] = SWAP_HELD_ITEMS = new WAction();
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
