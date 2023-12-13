/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.server.management.UserListEntry;

public class UserListWhitelistEntry
extends UserListEntry {
    private static final String __OBFID = "CL_00001870";

    public UserListWhitelistEntry(GameProfile p_i1129_1_) {
        super((Object)p_i1129_1_);
    }

    public UserListWhitelistEntry(JsonObject p_i1130_1_) {
        super((Object)UserListWhitelistEntry.func_152646_b(p_i1130_1_), p_i1130_1_);
    }

    @Override
    protected void onSerialization(JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
            data.addProperty("name", ((GameProfile)this.getValue()).getName());
            super.onSerialization(data);
        }
    }

    private static GameProfile func_152646_b(JsonObject p_152646_0_) {
        if (p_152646_0_.has("uuid") && p_152646_0_.has("name")) {
            UUID var2;
            String var1 = p_152646_0_.get("uuid").getAsString();
            try {
                var2 = UUID.fromString(var1);
            }
            catch (Throwable var4) {
                return null;
            }
            return new GameProfile(var2, p_152646_0_.get("name").getAsString());
        }
        return null;
    }
}

