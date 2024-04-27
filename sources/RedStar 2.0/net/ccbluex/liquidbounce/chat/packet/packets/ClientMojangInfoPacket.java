package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientMojangInfoPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "sessionHash", "", "(Ljava/lang/String;)V", "getSessionHash", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ClientMojangInfoPacket
implements Packet {
    @SerializedName(value="session_hash")
    @NotNull
    private final String sessionHash;

    @NotNull
    public final String getSessionHash() {
        return this.sessionHash;
    }

    public ClientMojangInfoPacket(@NotNull String sessionHash) {
        Intrinsics.checkParameterIsNotNull(sessionHash, "sessionHash");
        this.sessionHash = sessionHash;
    }

    @NotNull
    public final String component1() {
        return this.sessionHash;
    }

    @NotNull
    public final ClientMojangInfoPacket copy(@NotNull String sessionHash) {
        Intrinsics.checkParameterIsNotNull(sessionHash, "sessionHash");
        return new ClientMojangInfoPacket(sessionHash);
    }

    public static ClientMojangInfoPacket copy$default(ClientMojangInfoPacket clientMojangInfoPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientMojangInfoPacket.sessionHash;
        }
        return clientMojangInfoPacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ClientMojangInfoPacket(sessionHash=" + this.sessionHash + ")";
    }

    public int hashCode() {
        String string = this.sessionHash;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientMojangInfoPacket)) break block3;
                ClientMojangInfoPacket clientMojangInfoPacket = (ClientMojangInfoPacket)object;
                if (!Intrinsics.areEqual(this.sessionHash, clientMojangInfoPacket.sessionHash)) break block3;
            }
            return true;
        }
        return false;
    }
}
