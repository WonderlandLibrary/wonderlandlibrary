/*
 * Decompiled with CFR 0.152.
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

public class UserListOpsEntry
extends UserListEntry<GameProfile> {
    private final boolean field_183025_b;
    private final int field_152645_a;

    private static GameProfile func_152643_b(JsonObject jsonObject) {
        if (jsonObject.has("uuid") && jsonObject.has("name")) {
            UUID uUID;
            String string = jsonObject.get("uuid").getAsString();
            try {
                uUID = UUID.fromString(string);
            }
            catch (Throwable throwable) {
                return null;
            }
            return new GameProfile(uUID, jsonObject.get("name").getAsString());
        }
        return null;
    }

    public int getPermissionLevel() {
        return this.field_152645_a;
    }

    public UserListOpsEntry(GameProfile gameProfile, int n, boolean bl) {
        super(gameProfile);
        this.field_152645_a = n;
        this.field_183025_b = bl;
    }

    public UserListOpsEntry(JsonObject jsonObject) {
        super(UserListOpsEntry.func_152643_b(jsonObject), jsonObject);
        this.field_152645_a = jsonObject.has("level") ? jsonObject.get("level").getAsInt() : 0;
        this.field_183025_b = jsonObject.has("bypassesPlayerLimit") && jsonObject.get("bypassesPlayerLimit").getAsBoolean();
    }

    public boolean func_183024_b() {
        return this.field_183025_b;
    }

    @Override
    protected void onSerialization(JsonObject jsonObject) {
        if (this.getValue() != null) {
            jsonObject.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
            jsonObject.addProperty("name", ((GameProfile)this.getValue()).getName());
            super.onSerialization(jsonObject);
            jsonObject.addProperty("level", (Number)this.field_152645_a);
            jsonObject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.field_183025_b));
        }
    }
}

