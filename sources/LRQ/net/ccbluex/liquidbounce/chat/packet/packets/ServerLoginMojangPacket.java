/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.Nullable;

public final class ServerLoginMojangPacket
implements Packet {
    @SerializedName(value="name")
    private final String name;
    @SerializedName(value="uuid")
    private final UUID uuid;
    @SerializedName(value="allow_messages")
    private final boolean allowMessages;

    public final String getName() {
        return this.name;
    }

    public final UUID getUuid() {
        return this.uuid;
    }

    public final boolean getAllowMessages() {
        return this.allowMessages;
    }

    public ServerLoginMojangPacket(String name, UUID uuid, boolean allowMessages) {
        this.name = name;
        this.uuid = uuid;
        this.allowMessages = allowMessages;
    }

    public final String component1() {
        return this.name;
    }

    public final UUID component2() {
        return this.uuid;
    }

    public final boolean component3() {
        return this.allowMessages;
    }

    public final ServerLoginMojangPacket copy(String name, UUID uuid, boolean allowMessages) {
        return new ServerLoginMojangPacket(name, uuid, allowMessages);
    }

    public static /* synthetic */ ServerLoginMojangPacket copy$default(ServerLoginMojangPacket serverLoginMojangPacket, String string, UUID uUID, boolean bl, int n, Object object) {
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
                if (!this.name.equals(serverLoginMojangPacket.name) || !((Object)this.uuid).equals(serverLoginMojangPacket.uuid) || this.allowMessages != serverLoginMojangPacket.allowMessages) break block3;
            }
            return true;
        }
        return false;
    }
}

