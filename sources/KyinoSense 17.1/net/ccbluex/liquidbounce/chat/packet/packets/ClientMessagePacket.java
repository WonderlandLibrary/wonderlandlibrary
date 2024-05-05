/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet.packets;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.User;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ClientMessagePacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "id", "", "user", "Lnet/ccbluex/liquidbounce/chat/User;", "content", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/chat/User;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getId", "getUser", "()Lnet/ccbluex/liquidbounce/chat/User;", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "", "toString", "KyinoClient"})
public final class ClientMessagePacket
implements Packet {
    @SerializedName(value="author_id")
    @NotNull
    private final String id;
    @SerializedName(value="author_info")
    @NotNull
    private final User user;
    @SerializedName(value="content")
    @NotNull
    private final String content;

    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final User getUser() {
        return this.user;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public ClientMessagePacket(@NotNull String id, @NotNull User user, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(user, "user");
        Intrinsics.checkParameterIsNotNull(content, "content");
        this.id = id;
        this.user = user;
        this.content = content;
    }

    @NotNull
    public final String component1() {
        return this.id;
    }

    @NotNull
    public final User component2() {
        return this.user;
    }

    @NotNull
    public final String component3() {
        return this.content;
    }

    @NotNull
    public final ClientMessagePacket copy(@NotNull String id, @NotNull User user, @NotNull String content) {
        Intrinsics.checkParameterIsNotNull(id, "id");
        Intrinsics.checkParameterIsNotNull(user, "user");
        Intrinsics.checkParameterIsNotNull(content, "content");
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

    @NotNull
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
                if (!Intrinsics.areEqual(this.id, clientMessagePacket.id) || !Intrinsics.areEqual(this.user, clientMessagePacket.user) || !Intrinsics.areEqual(this.content, clientMessagePacket.content)) break block3;
            }
            return true;
        }
        return false;
    }
}

