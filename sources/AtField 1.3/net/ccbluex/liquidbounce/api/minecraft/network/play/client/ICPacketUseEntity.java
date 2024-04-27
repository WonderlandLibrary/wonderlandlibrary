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
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001\u0006R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "getAction", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "WAction", "AtField"})
public interface ICPacketUseEntity
extends IPacket {
    @NotNull
    public WAction getAction();

    public static final class WAction
    extends Enum {
        public static final /* enum */ WAction ATTACK;
        private static final WAction[] $VALUES;
        public static final /* enum */ WAction INTERACT;
        public static final /* enum */ WAction INTERACT_AT;

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WAction() {
            void var2_-1;
            void var1_-1;
        }

        static {
            WAction[] wActionArray = new WAction[3];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = INTERACT = new WAction("INTERACT", 0);
            wActionArray[1] = ATTACK = new WAction("ATTACK", 1);
            wActionArray[2] = INTERACT_AT = new WAction("INTERACT_AT", 2);
            $VALUES = wActionArray;
        }
    }
}

