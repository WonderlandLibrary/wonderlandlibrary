/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.TypeIntrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 */
package liying.utils;

import java.util.ArrayList;
import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public final class PacketUtils
extends MinecraftInstance {
    private static final ArrayList packets;
    public static final PacketUtils INSTANCE;

    @JvmStatic
    public static final PacketType getPacketType(Packet packet) {
        String string = packet.getClass().getSimpleName();
        if (StringsKt.startsWith((String)string, (String)"C", (boolean)true)) {
            return PacketType.CLIENTSIDE;
        }
        if (StringsKt.startsWith((String)string, (String)"S", (boolean)true)) {
            return PacketType.SERVERSIDE;
        }
        return PacketType.UNKNOWN;
    }

    private PacketUtils() {
    }

    static {
        PacketUtils packetUtils;
        INSTANCE = packetUtils = new PacketUtils();
        packets = new ArrayList();
    }

    @JvmStatic
    public static final boolean handleSendPacket(Packet packet) {
        if (CollectionsKt.contains((Iterable)packets, (Object)packet)) {
            Collection collection = packets;
            boolean bl = false;
            Collection collection2 = collection;
            if (collection2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
            }
            TypeIntrinsics.asMutableCollection((Object)collection2).remove(packet);
            return true;
        }
        return false;
    }

    @JvmStatic
    public static final void sendPacketNoEvent(Packet packet) {
        packets.add(packet);
        NetHandlerPlayClient netHandlerPlayClient = MinecraftInstance.mc2.func_147114_u();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        netHandlerPlayClient.func_147297_a(packet);
    }

    public static final class PacketType
    extends Enum {
        public static final /* enum */ PacketType SERVERSIDE;
        public static final /* enum */ PacketType CLIENTSIDE;
        private static final PacketType[] $VALUES;
        public static final /* enum */ PacketType UNKNOWN;

        public static PacketType[] values() {
            return (PacketType[])$VALUES.clone();
        }

        public static PacketType valueOf(String string) {
            return Enum.valueOf(PacketType.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private PacketType() {
            void var2_-1;
            void var1_-1;
        }

        static {
            PacketType[] packetTypeArray = new PacketType[3];
            PacketType[] packetTypeArray2 = packetTypeArray;
            packetTypeArray[0] = SERVERSIDE = new PacketType("SERVERSIDE", 0);
            packetTypeArray[1] = CLIENTSIDE = new PacketType("CLIENTSIDE", 1);
            packetTypeArray[2] = UNKNOWN = new PacketType("UNKNOWN", 2);
            $VALUES = packetTypeArray;
        }
    }
}

