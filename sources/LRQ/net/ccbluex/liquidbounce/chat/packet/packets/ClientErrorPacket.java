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

public final class ClientErrorPacket
implements Packet {
    @SerializedName(value="message")
    private final String message;

    public final String getMessage() {
        return this.message;
    }

    public ClientErrorPacket(String message) {
        this.message = message;
    }

    public final String component1() {
        return this.message;
    }

    public final ClientErrorPacket copy(String message) {
        return new ClientErrorPacket(message);
    }

    public static /* synthetic */ ClientErrorPacket copy$default(ClientErrorPacket clientErrorPacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientErrorPacket.message;
        }
        return clientErrorPacket.copy(string);
    }

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
                if (!this.message.equals(clientErrorPacket.message)) break block3;
            }
            return true;
        }
        return false;
    }
}

