/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import net.ccbluex.liquidbounce.chat.User;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.Nullable;

public final class ClientPrivateMessagePacket
implements Packet {
    @SerializedName(value="author_id")
    private final String id;
    @SerializedName(value="author_info")
    private final User user;
    @SerializedName(value="content")
    private final String content;

    public final String getId() {
        return this.id;
    }

    public final User getUser() {
        return this.user;
    }

    public final String getContent() {
        return this.content;
    }

    public ClientPrivateMessagePacket(String id, User user, String content) {
        this.id = id;
        this.user = user;
        this.content = content;
    }

    public final String component1() {
        return this.id;
    }

    public final User component2() {
        return this.user;
    }

    public final String component3() {
        return this.content;
    }

    public final ClientPrivateMessagePacket copy(String id, User user, String content) {
        return new ClientPrivateMessagePacket(id, user, content);
    }

    public static /* synthetic */ ClientPrivateMessagePacket copy$default(ClientPrivateMessagePacket clientPrivateMessagePacket, String string, User user, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientPrivateMessagePacket.id;
        }
        if ((n & 2) != 0) {
            user = clientPrivateMessagePacket.user;
        }
        if ((n & 4) != 0) {
            string2 = clientPrivateMessagePacket.content;
        }
        return clientPrivateMessagePacket.copy(string, user, string2);
    }

    public String toString() {
        return "ClientPrivateMessagePacket(id=" + this.id + ", user=" + this.user + ", content=" + this.content + ")";
    }

    public int hashCode() {
        String string = this.id;
        User user = this.user;
        String string2 = this.content;
        return ((string != null ? string.hashCode() : 0) * 31 + (user != null ? ((Object)user).hashCode() : 0)) * 31 + (string2 != null ? string2.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ClientPrivateMessagePacket)) break block3;
                ClientPrivateMessagePacket clientPrivateMessagePacket = (ClientPrivateMessagePacket)object;
                if (!this.id.equals(clientPrivateMessagePacket.id) || !((Object)this.user).equals(clientPrivateMessagePacket.user) || !this.content.equals(clientPrivateMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}

