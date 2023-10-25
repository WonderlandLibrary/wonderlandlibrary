/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListWhitelistEntry;

public class UserListWhitelist
extends UserList<GameProfile, UserListWhitelistEntry> {
    public UserListWhitelist(File p_i1132_1_) {
        super(p_i1132_1_);
    }

    @Override
    protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
        return new UserListWhitelistEntry(entryData);
    }

    @Override
    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        for (UserListWhitelistEntry userlistwhitelistentry : this.getValues().values()) {
            astring[i++] = ((GameProfile)userlistwhitelistentry.getValue()).getName();
        }
        return astring;
    }

    @Override
    protected String getObjectKey(GameProfile obj) {
        return obj.getId().toString();
    }

    public GameProfile getByName(String profileName) {
        for (UserListWhitelistEntry userlistwhitelistentry : this.getValues().values()) {
            if (!profileName.equalsIgnoreCase(((GameProfile)userlistwhitelistentry.getValue()).getName())) continue;
            return (GameProfile)userlistwhitelistentry.getValue();
        }
        return null;
    }
}

