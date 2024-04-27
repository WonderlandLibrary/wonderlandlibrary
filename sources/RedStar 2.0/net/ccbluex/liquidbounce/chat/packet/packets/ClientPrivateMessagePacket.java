package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.User;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\u0000\n\n\u0000\n\n\b\f\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B000¢J\t\r0HÆJ\t0HÆJ\t0HÆJ'0\u00002\b\b02\b\b02\b\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\b\b\tR08X¢\b\n\u0000\b\n\tR08X¢\b\n\u0000\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientPrivateMessagePacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "id", "", "user", "Lnet/ccbluex/liquidbounce/chat/User;", "content", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/chat/User;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getId", "getUser", "()Lnet/ccbluex/liquidbounce/chat/User;", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ClientPrivateMessagePacket
implements Packet {
    @SerializedName(value="author_id")
    @NotNull
    private final String id;
    @SerializedName(value="author_info")
    @NotNull
    private final User user;
    @SerializedName(value="content")
    @NotNull
    private final String content;

    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final User getUser() {
        return this.user;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public ClientPrivateMessagePacket(@NotNull String id, @NotNull User user, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(user, "user");
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.id = id;
        this.user = user;
        this.content = content;
    }

    @NotNull
    public final String component1() {
        return this.id;
    }

    @NotNull
    public final User component2() {
        return this.user;
    }

    @NotNull
    public final String component3() {
        return this.content;
    }

    @NotNull
    public final ClientPrivateMessagePacket copy(@NotNull String id, @NotNull User user, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(user, "user");
        Intrinsics.checkParameterIsNotNull(content, "content");
        return new ClientPrivateMessagePacket(id, user, content);
    }

    public static ClientPrivateMessagePacket copy$default(ClientPrivateMessagePacket clientPrivateMessagePacket, String string, User user, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientPrivateMessagePacket.id;
        }
        if ((n & 2) != 0) {
            user = clientPrivateMessagePacket.user;
        }
        if ((n & 4) != 0) {
            string2 = clientPrivateMessagePacket.content;
        }
        return clientPrivateMessagePacket.copy(string, user, string2);
    }

    @NotNull
    public String toString() {
        return "ClientPrivateMessagePacket(id=" + this.id + ", user=" + this.user + ", content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.id;
        User user = this.user;
        String string2 = this.content;
        return ((string != null ? string.hashCode() : 0) * 31 + (user != null ? ((Object)user).hashCode() : 0)) * 31 + (string2 != null ? string2.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientPrivateMessagePacket)) break block3;
                ClientPrivateMessagePacket clientPrivateMessagePacket = (ClientPrivateMessagePacket)object;
                if (!Intrinsics.areEqual(this.id, clientPrivateMessagePacket.id) || !Intrinsics.areEqual(this.user, clientPrivateMessagePacket.user) || !Intrinsics.areEqual(this.content, clientPrivateMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}
