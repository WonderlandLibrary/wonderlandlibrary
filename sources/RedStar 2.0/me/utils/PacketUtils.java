package me.utils;

import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\n\n\b\n\n\n\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\bÆ\u000020:B\b¢J\b0\t2\n\n\b0HJ0\f2\n\n\b0HJ\r020J\r020J02\f\n\b00HR*\n\b000j\n\b00`X¢\n\u0000¨"}, d2={"Lme/utils/PacketUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "packets", "Ljava/util/ArrayList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "Lkotlin/collections/ArrayList;", "getPacketType", "Lme/utils/PacketUtils$PacketType;", "packet", "handleSendPacket", "", "send", "", "cPacketConfirmTransaction", "Lnet/minecraft/network/play/client/CPacketConfirmTransaction;", "cPacketKeepAlive", "Lnet/minecraft/network/play/client/CPacketKeepAlive;", "sendPacketNoEvent", "PacketType", "Pride"})
public final class PacketUtils
extends MinecraftInstance {
    private static final ArrayList<Packet<INetHandlerPlayServer>> packets;
    public static final PacketUtils INSTANCE;

    @JvmStatic
    public static final boolean handleSendPacket(@NotNull Packet<?> packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        if (CollectionsKt.contains((Iterable)packets, packet)) {
            Collection collection = packets;
            boolean bl = false;
            Collection collection2 = collection;
            if (collection2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
            }
            TypeIntrinsics.asMutableCollection(collection2).remove(packet);
            return true;
        }
        return false;
    }

    @JvmStatic
    public static final void sendPacketNoEvent(@NotNull Packet<INetHandlerPlayServer> packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        packets.add(packet);
        Minecraft minecraft = MinecraftInstance.mc2;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc2");
        NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        netHandlerPlayClient.sendPacket(packet);
    }

    @JvmStatic
    @NotNull
    public static final PacketType getPacketType(@NotNull Packet<?> packet) {
        String className;
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        String string = className = packet.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(string, "className");
        if (StringsKt.startsWith(string, "C", true)) {
            return PacketType.CLIENTSIDE;
        }
        if (StringsKt.startsWith(className, "S", true)) {
            return PacketType.SERVERSIDE;
        }
        return PacketType.UNKNOWN;
    }

    public final void send(@NotNull CPacketKeepAlive cPacketKeepAlive) {
        Intrinsics.checkParameterIsNotNull(cPacketKeepAlive, "cPacketKeepAlive");
    }

    public final void send(@NotNull CPacketConfirmTransaction cPacketConfirmTransaction) {
        Intrinsics.checkParameterIsNotNull(cPacketConfirmTransaction, "cPacketConfirmTransaction");
    }

    private PacketUtils() {
    }

    static {
        PacketUtils packetUtils;
        INSTANCE = packetUtils = new PacketUtils();
        packets = new ArrayList();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lme/utils/PacketUtils$PacketType;", "", "(Ljava/lang/String;I)V", "SERVERSIDE", "CLIENTSIDE", "UNKNOWN", "Pride"})
    public static final class PacketType
    extends Enum<PacketType> {
        public static final PacketType SERVERSIDE;
        public static final PacketType CLIENTSIDE;
        public static final PacketType UNKNOWN;
        private static final PacketType[] $VALUES;

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
