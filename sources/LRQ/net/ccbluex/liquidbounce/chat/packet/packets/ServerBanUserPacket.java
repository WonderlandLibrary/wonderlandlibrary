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

public final class ServerBanUserPacket
implements Packet {
    @SerializedName(value="user")
    private final String user;

    public final String getUser() {
        return this.user;
    }

    public ServerBanUserPacket(String user) {
        this.user = user;
    }

    public final String component1() {
        return this.user;
    }

    public final ServerBanUserPacket copy(String user) {
        return new ServerBanUserPacket(user);
    }

    public static /* synthetic */ ServerBanUserPacket copy$default(ServerBanUserPacket serverBanUserPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverBanUserPacket.user;
        }
        return serverBanUserPacket.copy(string);
    }

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
                if (!this.user.equals(serverBanUserPacket.user)) break block3;
            }
            return true;
        }
        return false;
    }
}

