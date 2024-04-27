package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\b\n\b\b\b\u000020B00¢J\t0HÆJ\t\f0HÆJ\r0\u00002\b\b02\b\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\b\bR08X¢\b\n\u0000\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerLoginJWTPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "token", "", "allowMessages", "", "(Ljava/lang/String;Z)V", "getAllowMessages", "()Z", "getToken", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "Pride"})
public final class ServerLoginJWTPacket
implements Packet {
    @SerializedName(value="token")
    @NotNull
    private final String token;
    @SerializedName(value="allow_messages")
    private final boolean allowMessages;

    @NotNull
    public final String getToken() {
        return this.token;
    }

    public final boolean getAllowMessages() {
        return this.allowMessages;
    }

    public ServerLoginJWTPacket(@NotNull String token, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        this.token = token;
        this.allowMessages = allowMessages;
    }

    @NotNull
    public final String component1() {
        return this.token;
    }

    public final boolean component2() {
        return this.allowMessages;
    }

    @NotNull
    public final ServerLoginJWTPacket copy(@NotNull String token, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        return new ServerLoginJWTPacket(token, allowMessages);
    }

    public static ServerLoginJWTPacket copy$default(ServerLoginJWTPacket serverLoginJWTPacket, String string, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverLoginJWTPacket.token;
        }
        if ((n & 2) != 0) {
            bl = serverLoginJWTPacket.allowMessages;
        }
        return serverLoginJWTPacket.copy(string, bl);
    }

    @NotNull
    public String toString() {
        return "ServerLoginJWTPacket(token=" + this.token + ", allowMessages=" + this.allowMessages + ")";
    }

    public int hashCode() {
        String string = this.token;
        int n = (string != null ? string.hashCode() : 0) * 31;
        int n2 = this.allowMessages ? 1 : 0;
        if (n2 != 0) {
            n2 = 1;
        }
        return n + n2;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerLoginJWTPacket)) break block3;
                ServerLoginJWTPacket serverLoginJWTPacket = (ServerLoginJWTPacket)object;
                if (!Intrinsics.areEqual(this.token, serverLoginJWTPacket.token) || this.allowMessages != serverLoginJWTPacket.allowMessages) break block3;
            }
            return true;
        }
        return false;
    }
}
