package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerMessagePacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "content", "", "(Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ServerMessagePacket
implements Packet {
    @SerializedName(value="content")
    @NotNull
    private final String content;

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public ServerMessagePacket(@NotNull String content) {
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.content = content;
    }

    @NotNull
    public final String component1() {
        return this.content;
    }

    @NotNull
    public final ServerMessagePacket copy(@NotNull String content) {
        Intrinsics.checkParameterIsNotNull(content, "content");
        return new ServerMessagePacket(content);
    }

    public static ServerMessagePacket copy$default(ServerMessagePacket serverMessagePacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverMessagePacket.content;
        }
        return serverMessagePacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ServerMessagePacket(content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.content;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerMessagePacket)) break block3;
                ServerMessagePacket serverMessagePacket = (ServerMessagePacket)object;
                if (!Intrinsics.areEqual(this.content, serverMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}
