package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\b\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR08X¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientErrorPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Pride"})
public final class ClientErrorPacket
implements Packet {
    @SerializedName(value="message")
    @NotNull
    private final String message;

    @NotNull
    public final String getMessage() {
        return this.message;
    }

    public ClientErrorPacket(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        this.message = message;
    }

    @NotNull
    public final String component1() {
        return this.message;
    }

    @NotNull
    public final ClientErrorPacket copy(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        return new ClientErrorPacket(message);
    }

    public static ClientErrorPacket copy$default(ClientErrorPacket clientErrorPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientErrorPacket.message;
        }
        return clientErrorPacket.copy(string);
    }

    @NotNull
    public String toString() {
        return "ClientErrorPacket(message=" + this.message + ")";
    }

    public int hashCode() {
        String string = this.message;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientErrorPacket)) break block3;
                ClientErrorPacket clientErrorPacket = (ClientErrorPacket)object;
                if (!Intrinsics.areEqual(this.message, clientErrorPacket.message)) break block3;
            }
            return true;
        }
        return false;
    }
}
