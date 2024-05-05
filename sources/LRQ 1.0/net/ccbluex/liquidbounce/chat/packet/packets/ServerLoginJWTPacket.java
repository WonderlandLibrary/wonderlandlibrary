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

public final class ServerLoginJWTPacket
implements Packet {
    @SerializedName(value="token")
    private final String token;
    @SerializedName(value="allow_messages")
    private final boolean allowMessages;

    public final String getToken() {
        return this.token;
    }

    public final boolean getAllowMessages() {
        return this.allowMessages;
    }

    public ServerLoginJWTPacket(String token, boolean allowMessages) {
        this.token = token;
        this.allowMessages = allowMessages;
    }

    public final String component1() {
        return this.token;
    }

    public final boolean component2() {
        return this.allowMessages;
    }

    public final ServerLoginJWTPacket copy(String token, boolean allowMessages) {
        return new ServerLoginJWTPacket(token, allowMessages);
    }

    public static /* synthetic */ ServerLoginJWTPacket copy$default(ServerLoginJWTPacket serverLoginJWTPacket, String string, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverLoginJWTPacket.token;
        }
        if ((n & 2) != 0) {
            bl = serverLoginJWTPacket.allowMessages;
        }
        return serverLoginJWTPacket.copy(string, bl);
    }

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
                if (!this.token.equals(serverLoginJWTPacket.token) || this.allowMessages != serverLoginJWTPacket.allowMessages) break block3;
            }
            return true;
        }
        return false;
    }
}

