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

public final class ServerMessagePacket
implements Packet {
    @SerializedName(value="content")
    private final String content;

    public final String getContent() {
        return this.content;
    }

    public ServerMessagePacket(String content) {
        this.content = content;
    }

    public final String component1() {
        return this.content;
    }

    public final ServerMessagePacket copy(String content) {
        return new ServerMessagePacket(content);
    }

    public static /* synthetic */ ServerMessagePacket copy$default(ServerMessagePacket serverMessagePacket, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverMessagePacket.content;
        }
        return serverMessagePacket.copy(string);
    }

    public String toString() {
        return "ServerMessagePacket(content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.content;
        return string != null ? string.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerMessagePacket)) break block3;
                ServerMessagePacket serverMessagePacket = (ServerMessagePacket)object;
                if (!this.content.equals(serverMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}

