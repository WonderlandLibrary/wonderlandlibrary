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

public final class ClientNewJWTPacket
implements Packet {
    @SerializedName(value="token")
    private final String token;

    public final String getToken() {
        return this.token;
    }

    public ClientNewJWTPacket(String token) {
        this.token = token;
    }

    public final String component1() {
        return this.token;
    }

    public final ClientNewJWTPacket copy(String token) {
        return new ClientNewJWTPacket(token);
    }

    public static /* synthetic */ ClientNewJWTPacket copy$default(ClientNewJWTPacket clientNewJWTPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientNewJWTPacket.token;
        }
        return clientNewJWTPacket.copy(string);
    }

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
                if (!this.token.equals(clientNewJWTPacket.token)) break block3;
            }
            return true;
        }
        return false;
    }
}

