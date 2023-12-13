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
import java.io.File;
import java.util.Iterator;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;

public class UserListBans
extends UserList {
    private static final String __OBFID = "CL_00001873";

    public UserListBans(File bansFile) {
        super(bansFile);
    }

    @Override
    protected UserListEntry createEntry(JsonObject entryData) {
        return new UserListBansEntry(entryData);
    }

    public boolean isBanned(GameProfile profile) {
        return this.hasEntry((Object)profile);
    }

    @Override
    public String[] getKeys() {
        String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (UserListBansEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }

    protected String getProfileId(GameProfile profile) {
        return profile.getId().toString();
    }

    public GameProfile isUsernameBanned(String username) {
        UserListBansEntry var3;
        Iterator var2 = this.getValues().values().iterator();
        do {
            if (var2.hasNext()) continue;
            return null;
        } while (!username.equalsIgnoreCase(((GameProfile)(var3 = (UserListBansEntry)var2.next()).getValue()).getName()));
        return (GameProfile)var3.getValue();
    }

    @Override
    protected String getObjectKey(Object obj) {
        return this.getProfileId((GameProfile)obj);
    }
}

