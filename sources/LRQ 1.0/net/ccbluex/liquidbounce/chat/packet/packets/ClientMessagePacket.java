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

public final class ClientMessagePacket
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

    public ClientMessagePacket(String id, User user, String content) {
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

    public final ClientMessagePacket copy(String id, User user, String content) {
        return new ClientMessagePacket(id, user, content);
    }

    public static /* synthetic */ ClientMessagePacket copy$default(ClientMessagePacket clientMessagePacket, String string, User user, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string = clientMessagePacket.id;
        }
        if ((n & 2) != 0) {
            user = clientMessagePacket.user;
        }
        if ((n & 4) != 0) {
            string2 = clientMessagePacket.content;
        }
        return clientMessagePacket.copy(string, user, string2);
    }

    public String toString() {
        return "ClientMessagePacket(id=" + this.id + ", user=" + this.user + ", content=" + this.content + ")";
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
                if (!(object instanceof ClientMessagePacket)) break block3;
                ClientMessagePacket clientMessagePacket = (ClientMessagePacket)object;
                if (!this.id.equals(clientMessagePacket.id) || !((Object)this.user).equals(clientMessagePacket.user) || !this.content.equals(clientMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}

