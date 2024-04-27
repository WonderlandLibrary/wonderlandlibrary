package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\u0000\n\n\u0000\n\n\b\t\n\n\b\n\b\n\b\b\b\u000020B0\b0¢J\t0HÆJ\f0HÆJ\r0\u00002\b\b02\n\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\b\bR08X¢\b\n\u0000\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/SerializedPacket;", "", "packetName", "", "packetContent", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;)V", "getPacketContent", "()Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "getPacketName", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "Pride"})
public final class SerializedPacket {
    @SerializedName(value="m")
    @NotNull
    private final String packetName;
    @SerializedName(value="c")
    @Nullable
    private final Packet packetContent;

    @NotNull
    public final String getPacketName() {
        return this.packetName;
    }

    @Nullable
    public final Packet getPacketContent() {
        return this.packetContent;
    }

    public SerializedPacket(@NotNull String packetName, @Nullable Packet packetContent) {
        Intrinsics.checkParameterIsNotNull(packetName, "packetName");
        this.packetName = packetName;
        this.packetContent = packetContent;
    }

    @NotNull
    public final String component1() {
        return this.packetName;
    }

    @Nullable
    public final Packet component2() {
        return this.packetContent;
    }

    @NotNull
    public final SerializedPacket copy(@NotNull String packetName, @Nullable Packet packetContent) {
        Intrinsics.checkParameterIsNotNull(packetName, "packetName");
        return new SerializedPacket(packetName, packetContent);
    }

    public static SerializedPacket copy$default(SerializedPacket serializedPacket, String string, Packet packet, int n, Object object) {
        if ((n & 1) != 0) {
            string = serializedPacket.packetName;
        }
        if ((n & 2) != 0) {
            packet = serializedPacket.packetContent;
        }
        return serializedPacket.copy(string, packet);
    }

    @NotNull
    public String toString() {
        return "SerializedPacket(packetName=" + this.packetName + ", packetContent=" + this.packetContent + ")";
    }

    public int hashCode() {
        String string = this.packetName;
        Packet packet = this.packetContent;
        return (string != null ? string.hashCode() : 0) * 31 + (packet != null ? packet.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof SerializedPacket)) break block3;
                SerializedPacket serializedPacket = (SerializedPacket)object;
                if (!Intrinsics.areEqual(this.packetName, serializedPacket.packetName) || !Intrinsics.areEqual(this.packetContent, serializedPacket.packetContent)) break block3;
            }
            return true;
        }
        return false;
    }
}
