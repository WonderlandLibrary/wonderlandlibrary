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
import java.io.File;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListWhitelistEntry;

public class UserListWhitelist
extends UserList<GameProfile, UserListWhitelistEntry> {
    public GameProfile func_152706_a(String string) {
        for (UserListWhitelistEntry userListWhitelistEntry : this.getValues().values()) {
            if (!string.equalsIgnoreCase(((GameProfile)userListWhitelistEntry.getValue()).getName())) continue;
            return (GameProfile)userListWhitelistEntry.getValue();
        }
        return null;
    }

    @Override
    protected String getObjectKey(GameProfile gameProfile) {
        return gameProfile.getId().toString();
    }

    public UserListWhitelist(File file) {
        super(file);
    }

    @Override
    protected UserListEntry<GameProfile> createEntry(JsonObject jsonObject) {
        return new UserListWhitelistEntry(jsonObject);
    }

    @Override
    public String[] getKeys() {
        String[] stringArray = new String[this.getValues().size()];
        int n = 0;
        for (UserListWhitelistEntry userListWhitelistEntry : this.getValues().values()) {
            stringArray[n++] = ((GameProfile)userListWhitelistEntry.getValue()).getName();
        }
        return stringArray;
    }
}

