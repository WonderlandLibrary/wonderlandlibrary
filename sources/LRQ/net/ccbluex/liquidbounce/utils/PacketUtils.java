/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.TypeIntrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;

public final class PacketUtils
extends MinecraftInstance {
    private static final ArrayList<Packet<INetHandlerPlayServer>> packets;
    public static final PacketUtils INSTANCE;

    @JvmStatic
    public static final boolean handleSendPacket(Packet<?> packet) {
        if (CollectionsKt.contains((Iterable)packets, packet)) {
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
    public static final void sendPacketNoEvent(Packet<INetHandlerPlayServer> packet) {
        packets.add(packet);
        MinecraftInstance.mc.getNetHandler().addToSendQueue((IPacket)packet);
    }

    @JvmStatic
    public static final PacketType getPacketType(Packet<?> packet) {
        String className = packet.getClass().getSimpleName();
        if (StringsKt.startsWith((String)className, (String)"C", (boolean)true)) {
            return PacketType.CLIENTSIDE;
        }
        if (StringsKt.startsWith((String)className, (String)"S", (boolean)true)) {
            return PacketType.SERVERSIDE;
        }
        return PacketType.UNKNOWN;
    }

    public final void send(CPacketKeepAlive cPacketKeepAlive) {
    }

    public final void send(CPacketConfirmTransaction cPacketConfirmTransaction) {
    }

    private PacketUtils() {
    }

    static {
        PacketUtils packetUtils;
        INSTANCE = packetUtils = new PacketUtils();
        packets = new ArrayList();
    }

    public static final class PacketType
    extends Enum<PacketType> {
        public static final /* enum */ PacketType SERVERSIDE;
        public static final /* enum */ PacketType CLIENTSIDE;
        public static final /* enum */ PacketType UNKNOWN;
        private static final /* synthetic */ PacketType[] $VALUES;

        static {
            PacketType[] packetTypeArray = new PacketType[3];
            PacketType[] packetTypeArray2 = packetTypeArray;
            packetTypeArray[0] = SERVERSIDE = new PacketType();
            packetTypeArray[1] = CLIENTSIDE = new PacketType();
            packetTypeArray[2] = UNKNOWN = new PacketType();
            $VALUES = packetTypeArray;
        }

        public static PacketType[] values() {
            return (PacketType[])$VALUES.clone();
        }

        public static PacketType valueOf(String string) {
            return Enum.valueOf(PacketType.class, string);
        }
    }
}

