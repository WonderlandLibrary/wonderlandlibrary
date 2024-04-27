/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public final class User {
    @SerializedName(value="name")
    private final String name;
    @SerializedName(value="uuid")
    private final UUID uuid;

    public final String getName() {
        return this.name;
    }

    public final UUID getUuid() {
        return this.uuid;
    }

    public User(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public final String component1() {
        return this.name;
    }

    public final UUID component2() {
        return this.uuid;
    }

    public final User copy(String name, UUID uuid) {
        return new User(name, uuid);
    }

    public static /* synthetic */ User copy$default(User user, String string, UUID uUID, int n, Object object) {
        if ((n & 1) != 0) {
            string = user.name;
        }
        if ((n & 2) != 0) {
            uUID = user.uuid;
        }
        return user.copy(string, uUID);
    }

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
                if (!this.name.equals(user.name) || !((Object)this.uuid).equals(user.uuid)) break block3;
            }
            return true;
        }
        return false;
    }
}

