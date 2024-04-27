/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.liuli.elixir.compat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u0018"}, d2={"Lme/liuli/elixir/compat/Session;", "", "username", "", "uuid", "token", "type", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getToken", "()Ljava/lang/String;", "getType", "getUsername", "getUuid", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "Elixir"})
public final class Session {
    @NotNull
    private final String username;
    @NotNull
    private final String uuid;
    @NotNull
    private final String token;
    @NotNull
    private final String type;

    public Session(@NotNull String username, @NotNull String uuid, @NotNull String token, @NotNull String type) {
        Intrinsics.checkNotNullParameter(username, "username");
        Intrinsics.checkNotNullParameter(uuid, "uuid");
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(type, "type");
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        this.type = type;
    }

    @NotNull
    public final String getUsername() {
        return this.username;
    }

    @NotNull
    public final String getUuid() {
        return this.uuid;
    }

    @NotNull
    public final String getToken() {
        return this.token;
    }

    @NotNull
    public final String getType() {
        return this.type;
    }

    @NotNull
    public final String component1() {
        return this.username;
    }

    @NotNull
    public final String component2() {
        return this.uuid;
    }

    @NotNull
    public final String component3() {
        return this.token;
    }

    @NotNull
    public final String component4() {
        return this.type;
    }

    @NotNull
    public final Session copy(@NotNull String username, @NotNull String uuid, @NotNull String token, @NotNull String type) {
        Intrinsics.checkNotNullParameter(username, "username");
        Intrinsics.checkNotNullParameter(uuid, "uuid");
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(type, "type");
        return new Session(username, uuid, token, type);
    }

    public static /* synthetic */ Session copy$default(Session session, String string, String string2, String string3, String string4, int n, Object object) {
        if ((n & 1) != 0) {
            string = session.username;
        }
        if ((n & 2) != 0) {
            string2 = session.uuid;
        }
        if ((n & 4) != 0) {
            string3 = session.token;
        }
        if ((n & 8) != 0) {
            string4 = session.type;
        }
        return session.copy(string, string2, string3, string4);
    }

    @NotNull
    public String toString() {
        return "Session(username=" + this.username + ", uuid=" + this.uuid + ", token=" + this.token + ", type=" + this.type + ')';
    }

    public int hashCode() {
        int result = this.username.hashCode();
        result = result * 31 + this.uuid.hashCode();
        result = result * 31 + this.token.hashCode();
        result = result * 31 + this.type.hashCode();
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Session)) {
            return false;
        }
        Session session = (Session)other;
        if (!Intrinsics.areEqual(this.username, session.username)) {
            return false;
        }
        if (!Intrinsics.areEqual(this.uuid, session.uuid)) {
            return false;
        }
        if (!Intrinsics.areEqual(this.token, session.token)) {
            return false;
        }
        return Intrinsics.areEqual(this.type, session.type);
    }
}

