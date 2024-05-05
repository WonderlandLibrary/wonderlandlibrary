/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.server.management.UserListEntry;

public class UserListWhitelistEntry
extends UserListEntry<GameProfile> {
    public UserListWhitelistEntry(GameProfile profile) {
        super(profile);
    }

    public UserListWhitelistEntry(JsonObject p_i1130_1_) {
        super(UserListWhitelistEntry.gameProfileFromJsonObject(p_i1130_1_), p_i1130_1_);
    }

    @Override
    protected void onSerialization(JsonObject data) {
        if (this.getValue() == null) return;
        data.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
        data.addProperty("name", ((GameProfile)this.getValue()).getName());
        super.onSerialization(data);
    }

    private static GameProfile gameProfileFromJsonObject(JsonObject p_152646_0_) {
        if (!p_152646_0_.has("uuid")) return null;
        if (!p_152646_0_.has("name")) return null;
        String s = p_152646_0_.get("uuid").getAsString();
        try {
            UUID uuid = UUID.fromString(s);
            return new GameProfile(uuid, p_152646_0_.get("name").getAsString());
        }
        catch (Throwable var4) {
            return null;
        }
    }
}

