package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerBanUserPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "user", "", "(Ljava/lang/String;)V", "getUser", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ServerBanUserPacket
implements Packet {
    @SerializedName(value="user")
    @NotNull
    private final String user;

    @NotNull
    public final String getUser() {
        return this.user;
    }

    public ServerBanUserPacket(@NotNull String user) {
        Intrinsics.checkParameterIsNotNull(user, "user");
        this.user = user;
    }

    @NotNull
    public final String component1() {
        return this.user;
    }

    @NotNull
    public final ServerBanUserPacket copy(@NotNull String user) {
        Intrinsics.checkParameterIsNotNull(user, "user");
        return new ServerBanUserPacket(user);
    }

    public static ServerBanUserPacket copy$default(ServerBanUserPacket serverBanUserPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverBanUserPacket.user;
        }
        return serverBanUserPacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ServerBanUserPacket(user=" + this.user + ")";
    }

    public int hashCode() {
        String string = this.user;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerBanUserPacket)) break block3;
                ServerBanUserPacket serverBanUserPacket = (ServerBanUserPacket)object;
                if (!Intrinsics.areEqual(this.user, serverBanUserPacket.user)) break block3;
            }
            return true;
        }
        return false;
    }
}
