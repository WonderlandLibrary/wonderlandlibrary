/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "WAction", "LiKingSense"})
public interface ICPacketResourcePackStatus
extends IPacket {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "", "(Ljava/lang/String;I)V", "SUCCESSFULLY_LOADED", "DECLINED", "FAILED_DOWNLOAD", "ACCEPTED", "LiKingSense"})
    public static final class WAction
    extends Enum<WAction> {
        public static final /* enum */ WAction SUCCESSFULLY_LOADED;
        public static final /* enum */ WAction DECLINED;
        public static final /* enum */ WAction FAILED_DOWNLOAD;
        public static final /* enum */ WAction ACCEPTED;
        private static final /* synthetic */ WAction[] $VALUES;

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

