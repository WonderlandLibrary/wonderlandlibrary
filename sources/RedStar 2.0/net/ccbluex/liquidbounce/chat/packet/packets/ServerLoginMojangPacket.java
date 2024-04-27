package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\r\n\u0000\n\u0000\n\b\n\b\b\b\u000020B000¢\bJ\t0HÆJ\t0HÆJ\t0HÆJ'0\u00002\b\b02\b\b02\b\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\b\t\nR08X¢\b\n\u0000\b\fR08X¢\b\n\u0000\b\r¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerLoginMojangPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "name", "", "uuid", "Ljava/util/UUID;", "allowMessages", "", "(Ljava/lang/String;Ljava/util/UUID;Z)V", "getAllowMessages", "()Z", "getName", "()Ljava/lang/String;", "getUuid", "()Ljava/util/UUID;", "component1", "component2", "component3", "copy", "equals", "other", "", "hashCode", "", "toString", "Pride"})
public final class ServerLoginMojangPacket
implements Packet {
    @SerializedName(value="name")
    @NotNull
    private final String name;
    @SerializedName(value="uuid")
    @NotNull
    private final UUID uuid;
    @SerializedName(value="allow_messages")
    private final boolean allowMessages;

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final UUID getUuid() {
        return this.uuid;
    }

    public final boolean getAllowMessages() {
        return this.allowMessages;
    }

    public ServerLoginMojangPacket(@NotNull String name, @NotNull UUID uuid, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        this.name = name;
        this.uuid = uuid;
        this.allowMessages = allowMessages;
    }

    @NotNull
    public final String component1() {
        return this.name;
    }

    @NotNull
    public final UUID component2() {
        return this.uuid;
    }

    public final boolean component3() {
        return this.allowMessages;
    }

    @NotNull
    public final ServerLoginMojangPacket copy(@NotNull String name, @NotNull UUID uuid, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        return new ServerLoginMojangPacket(name, uuid, allowMessages);
    }

    public static ServerLoginMojangPacket copy$default(ServerLoginMojangPacket serverLoginMojangPacket, String string, UUID uUID, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverLoginMojangPacket.name;
        }
        if ((n & 2) != 0) {
            uUID = serverLoginMojangPacket.uuid;
        }
        if ((n & 4) != 0) {
            bl = serverLoginMojangPacket.allowMessages;
        }
        return serverLoginMojangPacket.copy(string, uUID, bl);
    }

    @NotNull
    public String toString() {
        return "ServerLoginMojangPacket(name=" + this.name + ", uuid=" + this.uuid + ", allowMessages=" + this.allowMessages + ")";
    }

    public int hashCode() {
        String string = this.name;
        UUID uUID = this.uuid;
        int n = ((string != null ? string.hashCode() : 0) * 31 + (uUID != null ? ((Object)uUID).hashCode() : 0)) * 31;
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
                if (!(object instanceof ServerLoginMojangPacket)) break block3;
                ServerLoginMojangPacket serverLoginMojangPacket = (ServerLoginMojangPacket)object;
                if (!Intrinsics.areEqual(this.name, serverLoginMojangPacket.name) || !Intrinsics.areEqual(this.uuid, serverLoginMojangPacket.uuid) || this.allowMessages != serverLoginMojangPacket.allowMessages) break block3;
            }
            return true;
        }
        return false;
    }
}
