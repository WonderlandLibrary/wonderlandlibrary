/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001\u000eR\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "getAction", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "WAction", "AtField"})
public interface ICPacketPlayerDigging
extends IPacket {
    @NotNull
    public WBlockPos getPosition();

    @NotNull
    public IEnumFacing getFacing();

    @NotNull
    public WAction getAction();

    public static final class WAction
    extends Enum {
        public static final /* enum */ WAction DROP_ALL_ITEMS;
        public static final /* enum */ WAction SWAP_HELD_ITEMS;
        public static final /* enum */ WAction START_DESTROY_BLOCK;
        public static final /* enum */ WAction ABORT_DESTROY_BLOCK;
        public static final /* enum */ WAction DROP_ITEM;
        public static final /* enum */ WAction STOP_DESTROY_BLOCK;
        private static final WAction[] $VALUES;
        public static final /* enum */ WAction RELEASE_USE_ITEM;

        static {
            WAction[] wActionArray = new WAction[7];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = START_DESTROY_BLOCK = new WAction("START_DESTROY_BLOCK", 0);
            wActionArray[1] = ABORT_DESTROY_BLOCK = new WAction("ABORT_DESTROY_BLOCK", 1);
            wActionArray[2] = STOP_DESTROY_BLOCK = new WAction("STOP_DESTROY_BLOCK", 2);
            wActionArray[3] = DROP_ALL_ITEMS = new WAction("DROP_ALL_ITEMS", 3);
            wActionArray[4] = DROP_ITEM = new WAction("DROP_ITEM", 4);
            wActionArray[5] = RELEASE_USE_ITEM = new WAction("RELEASE_USE_ITEM", 5);
            wActionArray[6] = SWAP_HELD_ITEMS = new WAction("SWAP_HELD_ITEMS", 6);
            $VALUES = wActionArray;
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WAction() {
            void var2_-1;
            void var1_-1;
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }
    }
}

