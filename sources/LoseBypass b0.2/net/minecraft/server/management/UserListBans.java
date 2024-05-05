/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
extends UserList<GameProfile, UserListBansEntry> {
    public UserListBans(File bansFile) {
        super(bansFile);
    }

    @Override
    protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
        return new UserListBansEntry(entryData);
    }

    public boolean isBanned(GameProfile profile) {
        return this.hasEntry(profile);
    }

    @Override
    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        Iterator iterator = this.getValues().values().iterator();
        while (iterator.hasNext()) {
            UserListBansEntry userlistbansentry = (UserListBansEntry)iterator.next();
            astring[i++] = ((GameProfile)userlistbansentry.getValue()).getName();
        }
        return astring;
    }

    @Override
    protected String getObjectKey(GameProfile obj) {
        return obj.getId().toString();
    }

    public GameProfile isUsernameBanned(String username) {
        UserListBansEntry userlistbansentry;
        Iterator iterator = this.getValues().values().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!username.equalsIgnoreCase(((GameProfile)(userlistbansentry = (UserListBansEntry)iterator.next()).getValue()).getName()));
        return (GameProfile)userlistbansentry.getValue();
    }
}

