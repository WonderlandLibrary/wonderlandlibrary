/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u001f\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/SerializedPacket;", "", "packetName", "", "packetContent", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;)V", "getPacketContent", "()Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "getPacketName", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "KyinoClient"})
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

    public static /* synthetic */ SerializedPacket copy$default(SerializedPacket serializedPacket, String string, Packet packet, int n, Object object) {
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

