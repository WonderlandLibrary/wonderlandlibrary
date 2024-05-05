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
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/packets/ServerLoginJWTPacket;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "token", "", "allowMessages", "", "(Ljava/lang/String;Z)V", "getAllowMessages", "()Z", "getToken", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "KyinoClient"})
public final class ServerLoginJWTPacket
implements Packet {
    @SerializedName(value="token")
    @NotNull
    private final String token;
    @SerializedName(value="allow_messages")
    private final boolean allowMessages;

    @NotNull
    public final String getToken() {
        return this.token;
    }

    public final boolean getAllowMessages() {
        return this.allowMessages;
    }

    public ServerLoginJWTPacket(@NotNull String token, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        this.token = token;
        this.allowMessages = allowMessages;
    }

    @NotNull
    public final String component1() {
        return this.token;
    }

    public final boolean component2() {
        return this.allowMessages;
    }

    @NotNull
    public final ServerLoginJWTPacket copy(@NotNull String token, boolean allowMessages) {
        Intrinsics.checkParameterIsNotNull(token, "token");
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

    @NotNull
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
                if (!Intrinsics.areEqual(this.token, serverLoginJWTPacket.token) || this.allowMessages != serverLoginJWTPacket.allowMessages) break block3;
            }
            return true;
        }
        return false;
    }
}

