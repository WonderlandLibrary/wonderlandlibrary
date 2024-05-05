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

public final class ServerPrivateMessagePacket
implements Packet {
    @SerializedName(value="receiver")
    private final String receiver;
    @SerializedName(value="content")
    private final String content;

    public final String getReceiver() {
        return this.receiver;
    }

    public final String getContent() {
        return this.content;
    }

    public ServerPrivateMessagePacket(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;
    }

    public final String component1() {
        return this.receiver;
    }

    public final String component2() {
        return this.content;
    }

    public final ServerPrivateMessagePacket copy(String receiver, String content) {
        return new ServerPrivateMessagePacket(receiver, content);
    }

    public static /* synthetic */ ServerPrivateMessagePacket copy$default(ServerPrivateMessagePacket serverPrivateMessagePacket, String string, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string = serverPrivateMessagePacket.receiver;
        }
        if ((n & 2) != 0) {
            string2 = serverPrivateMessagePacket.content;
        }
        return serverPrivateMessagePacket.copy(string, string2);
    }

    public String toString() {
        return "ServerPrivateMessagePacket(receiver=" + this.receiver + ", content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.receiver;
        String string2 = this.content;
        return (string != null ? string.hashCode() : 0) * 31 + (string2 != null ? string2.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ServerPrivateMessagePacket)) break block3;
                ServerPrivateMessagePacket serverPrivateMessagePacket = (ServerPrivateMessagePacket)object;
                if (!this.receiver.equals(serverPrivateMessagePacket.receiver) || !this.content.equals(serverPrivateMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}

