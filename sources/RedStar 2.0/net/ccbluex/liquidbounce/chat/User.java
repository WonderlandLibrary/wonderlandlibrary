package net.ccbluex.liquidbounce.chat;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\u0000\n\n\u0000\n\n\b\t\n\n\b\n\b\n\b\b\b\u000020B00¢J\t0HÆJ\t\f0HÆJ\r0\u00002\b\b02\b\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR08X¢\b\n\u0000\b\bR08X¢\b\n\u0000\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/User;", "", "name", "", "uuid", "Ljava/util/UUID;", "(Ljava/lang/String;Ljava/util/UUID;)V", "getName", "()Ljava/lang/String;", "getUuid", "()Ljava/util/UUID;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "Pride"})
public final class User {
    @SerializedName(value="name")
    @NotNull
    private final String name;
    @SerializedName(value="uuid")
    @NotNull
    private final UUID uuid;

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final UUID getUuid() {
        return this.uuid;
    }

    public User(@NotNull String name, @NotNull UUID uuid) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        this.name = name;
        this.uuid = uuid;
    }

    @NotNull
    public final String component1() {
        return this.name;
    }

    @NotNull
    public final UUID component2() {
        return this.uuid;
    }

    @NotNull
    public final User copy(@NotNull String name, @NotNull UUID uuid) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(uuid, "uuid");
        return new User(name, uuid);
    }

    public static User copy$default(User user, String string, UUID uUID, int n, Object object) {
        if ((n & 1) != 0) {
            string = user.name;
        }
        if ((n & 2) != 0) {
            uUID = user.uuid;
        }
        return user.copy(string, uUID);
    }

    @NotNull
    public String toString() {
        return "User(name=" + this.name + ", uuid=" + this.uuid + ")";
    }

    public int hashCode() {
        String string = this.name;
        UUID uUID = this.uuid;
        return (string != null ? string.hashCode() : 0) * 31 + (uUID != null ? ((Object)uUID).hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof User)) break block3;
                User user = (User)object;
                if (!Intrinsics.areEqual(this.name, user.name) || !Intrinsics.areEqual(this.uuid, user.uuid)) break block3;
            }
            return true;
        }
        return false;
    }
}
