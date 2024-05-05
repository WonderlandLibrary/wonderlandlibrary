/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.Nullable;

public final class ClientMojangInfoPacket
implements Packet {
    @SerializedName(value="session_hash")
    private final String sessionHash;

    public final String getSessionHash() {
        return this.sessionHash;
    }

    public ClientMojangInfoPacket(String sessionHash) {
        this.sessionHash = sessionHash;
    }

    public final String component1() {
        return this.sessionHash;
    }

    public final ClientMojangInfoPacket copy(String sessionHash) {
        return new ClientMojangInfoPacket(sessionHash);
    }

    public static /* synthetic */ ClientMojangInfoPacket copy$default(ClientMojangInfoPacket clientMojangInfoPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientMojangInfoPacket.sessionHash;
        }
        return clientMojangInfoPacket.copy(string);
    }

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
                if (!this.sessionHash.equals(clientMojangInfoPacket.sessionHash)) break block3;
            }
            return true;
        }
        return false;
    }
}

