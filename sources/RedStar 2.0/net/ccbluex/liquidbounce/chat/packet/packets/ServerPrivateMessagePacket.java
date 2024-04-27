package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\t\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B00¢J\t\t0HÆJ\t\n0HÆJ0\u00002\b\b02\b\b0HÆJ\f0\r2\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\bR08X¢\b\n\u0000\b\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerPrivateMessagePacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "receiver", "", "content", "(Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getReceiver", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ServerPrivateMessagePacket
implements Packet {
    @SerializedName(value="receiver")
    @NotNull
    private final String receiver;
    @SerializedName(value="content")
    @NotNull
    private final String content;

    @NotNull
    public final String getReceiver() {
        return this.receiver;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public ServerPrivateMessagePacket(@NotNull String receiver, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(receiver, "receiver");
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.receiver = receiver;
        this.content = content;
    }

    @NotNull
    public final String component1() {
        return this.receiver;
    }

    @NotNull
    public final String component2() {
        return this.content;
    }

    @NotNull
    public final ServerPrivateMessagePacket copy(@NotNull String receiver, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(receiver, "receiver");
        Intrinsics.checkParameterIsNotNull(content, "content");
        return new ServerPrivateMessagePacket(receiver, content);
    }

    public static ServerPrivateMessagePacket copy$default(ServerPrivateMessagePacket serverPrivateMessagePacket, String string, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverPrivateMessagePacket.receiver;
        }
        if ((n & 2) != 0) {
            string2 = serverPrivateMessagePacket.content;
        }
        return serverPrivateMessagePacket.copy(string, string2);
    }

    @NotNull
    public String toString() {
        return "ServerPrivateMessagePacket(receiver=" + this.receiver + ", content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.receiver;
        String string2 = this.content;
        return (string != null ? string.hashCode() : 0) * 31 + (string2 != null ? string2.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerPrivateMessagePacket)) break block3;
                ServerPrivateMessagePacket serverPrivateMessagePacket = (ServerPrivateMessagePacket)object;
                if (!Intrinsics.areEqual(this.receiver, serverPrivateMessagePacket.receiver) || !Intrinsics.areEqual(this.content, serverPrivateMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}
