package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientSuccessPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "reason", "", "(Ljava/lang/String;)V", "getReason", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ClientSuccessPacket
implements Packet {
    @SerializedName(value="reason")
    @NotNull
    private final String reason;

    @NotNull
    public final String getReason() {
        return this.reason;
    }

    public ClientSuccessPacket(@NotNull String reason) {
        Intrinsics.checkParameterIsNotNull(reason, "reason");
        this.reason = reason;
    }

    @NotNull
    public final String component1() {
        return this.reason;
    }

    @NotNull
    public final ClientSuccessPacket copy(@NotNull String reason) {
        Intrinsics.checkParameterIsNotNull(reason, "reason");
        return new ClientSuccessPacket(reason);
    }

    public static ClientSuccessPacket copy$default(ClientSuccessPacket clientSuccessPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientSuccessPacket.reason;
        }
        return clientSuccessPacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ClientSuccessPacket(reason=" + this.reason + ")";
    }

    public int hashCode() {
        String string = this.reason;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientSuccessPacket)) break block3;
                ClientSuccessPacket clientSuccessPacket = (ClientSuccessPacket)object;
                if (!Intrinsics.areEqual(this.reason, clientSuccessPacket.reason)) break block3;
            }
            return true;
        }
        return false;
    }
}
