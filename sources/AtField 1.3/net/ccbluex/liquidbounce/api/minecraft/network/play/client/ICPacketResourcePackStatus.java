/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WAction", "AtField"})
public interface ICPacketResourcePackStatus
extends IPacket {

    public static final class WAction
    extends Enum {
        public static final /* enum */ WAction SUCCESSFULLY_LOADED;
        public static final /* enum */ WAction FAILED_DOWNLOAD;
        private static final WAction[] $VALUES;
        public static final /* enum */ WAction DECLINED;
        public static final /* enum */ WAction ACCEPTED;

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WAction() {
            void var2_-1;
            void var1_-1;
        }

        static {
            WAction[] wActionArray = new WAction[4];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = SUCCESSFULLY_LOADED = new WAction("SUCCESSFULLY_LOADED", 0);
            wActionArray[1] = DECLINED = new WAction("DECLINED", 1);
            wActionArray[2] = FAILED_DOWNLOAD = new WAction("FAILED_DOWNLOAD", 2);
            wActionArray[3] = ACCEPTED = new WAction("ACCEPTED", 3);
            $VALUES = wActionArray;
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }
    }
}

