package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientNewJWTPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "token", "", "(Ljava/lang/String;)V", "getToken", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ClientNewJWTPacket
implements Packet {
    @SerializedName(value="token")
    @NotNull
    private final String token;

    @NotNull
    public final String getToken() {
        return this.token;
    }

    public ClientNewJWTPacket(@NotNull String token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        this.token = token;
    }

    @NotNull
    public final String component1() {
        return this.token;
    }

    @NotNull
    public final ClientNewJWTPacket copy(@NotNull String token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        return new ClientNewJWTPacket(token);
    }

    public static ClientNewJWTPacket copy$default(ClientNewJWTPacket clientNewJWTPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientNewJWTPacket.token;
        }
        return clientNewJWTPacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ClientNewJWTPacket(token=" + this.token + ")";
    }

    public int hashCode() {
        String string = this.token;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientNewJWTPacket)) break block3;
                ClientNewJWTPacket clientNewJWTPacket = (ClientNewJWTPacket)object;
                if (!Intrinsics.areEqual(this.token, clientNewJWTPacket.token)) break block3;
            }
            return true;
        }
        return false;
    }
}
