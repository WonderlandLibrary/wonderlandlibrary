/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.UUID;
import net.minecraft.server.management.BanEntry;

public class UserListBansEntry
extends BanEntry<GameProfile> {
    public UserListBansEntry(GameProfile profile) {
        this(profile, (Date)null, (String)null, (Date)null, (String)null);
    }

    public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason) {
        super(profile, endDate, banner, endDate, banReason);
    }

    public UserListBansEntry(JsonObject p_i1136_1_) {
        super(UserListBansEntry.func_152648_b(p_i1136_1_), p_i1136_1_);
    }

    @Override
    protected void onSerialization(JsonObject data) {
        if (this.getValue() == null) return;
        data.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
        data.addProperty("name", ((GameProfile)this.getValue()).getName());
        super.onSerialization(data);
    }

    private static GameProfile func_152648_b(JsonObject p_152648_0_) {
        if (!p_152648_0_.has("uuid")) return null;
        if (!p_152648_0_.has("name")) return null;
        String s = p_152648_0_.get("uuid").getAsString();
        try {
            UUID uuid = UUID.fromString(s);
            return new GameProfile(uuid, p_152648_0_.get("name").getAsString());
        }
        catch (Throwable var4) {
            return null;
        }
    }
}

