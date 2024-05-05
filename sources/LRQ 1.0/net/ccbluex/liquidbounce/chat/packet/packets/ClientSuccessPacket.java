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

public final class ClientSuccessPacket
implements Packet {
    @SerializedName(value="reason")
    private final String reason;

    public final String getReason() {
        return this.reason;
    }

    public ClientSuccessPacket(String reason) {
        this.reason = reason;
    }

    public final String component1() {
        return this.reason;
    }

    public final ClientSuccessPacket copy(String reason) {
        return new ClientSuccessPacket(reason);
    }

    public static /* synthetic */ ClientSuccessPacket copy$default(ClientSuccessPacket clientSuccessPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientSuccessPacket.reason;
        }
        return clientSuccessPacket.copy(string);
    }

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
                if (!this.reason.equals(clientSuccessPacket.reason)) break block3;
            }
            return true;
        }
        return false;
    }
}

