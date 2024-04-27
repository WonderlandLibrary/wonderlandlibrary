/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.TypeIntrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u00020\t2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0012\u0010\u000b\u001a\u00020\f2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\r\u001a\u00020\u000e2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R*\u0010\u0003\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/utils/PacketUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "packets", "Ljava/util/ArrayList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "Lkotlin/collections/ArrayList;", "getPacketType", "Lnet/ccbluex/liquidbounce/utils/PacketUtils$PacketType;", "packet", "handleSendPacket", "", "sendPacketNoEvent", "", "PacketType", "LiKingSense"})
public final class PacketUtils
extends MinecraftInstance {
    private static final ArrayList<Packet<INetHandlerPlayServer>> packets;
    public static final PacketUtils INSTANCE;

    public final boolean handleSendPacket(@NotNull Packet<?> packet) {
        Intrinsics.checkParameterIsNotNull(packet, (String)"packet");
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

    public final void sendPacketNoEvent(@NotNull Packet<INetHandlerPlayServer> packet) {
        Intrinsics.checkParameterIsNotNull(packet, (String)"packet");
        packets.add(packet);
        Minecraft minecraft = MinecraftInstance.mc2;
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
        minecraft.func_147114_u().func_147297_a(packet);
    }

    @NotNull
    public final PacketType getPacketType(@NotNull Packet<?> packet) {
        String className;
        Intrinsics.checkParameterIsNotNull(packet, (String)"packet");
        String string = className = packet.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"className");
        if (StringsKt.startsWith((String)string, (String)"C", (boolean)true)) {
            return PacketType.CLIENTSIDE;
        }
        if (StringsKt.startsWith((String)className, (String)"S", (boolean)true)) {
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/PacketUtils$PacketType;", "", "(Ljava/lang/String;I)V", "SERVERSIDE", "CLIENTSIDE", "UNKNOWN", "LiKingSense"})
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

