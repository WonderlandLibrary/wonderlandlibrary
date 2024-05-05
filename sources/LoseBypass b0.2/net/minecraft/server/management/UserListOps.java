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
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListOpsEntry;

public class UserListOps
extends UserList<GameProfile, UserListOpsEntry> {
    public UserListOps(File saveFile) {
        super(saveFile);
    }

    @Override
    protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
        return new UserListOpsEntry(entryData);
    }

    @Override
    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        Iterator iterator = this.getValues().values().iterator();
        while (iterator.hasNext()) {
            UserListOpsEntry userlistopsentry = (UserListOpsEntry)iterator.next();
            astring[i++] = ((GameProfile)userlistopsentry.getValue()).getName();
        }
        return astring;
    }

    public boolean func_183026_b(GameProfile p_183026_1_) {
        UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.getEntry(p_183026_1_);
        if (userlistopsentry == null) return false;
        boolean bl = userlistopsentry.func_183024_b();
        return bl;
    }

    @Override
    protected String getObjectKey(GameProfile obj) {
        return obj.getId().toString();
    }

    public GameProfile getGameProfileFromName(String username) {
        UserListOpsEntry userlistopsentry;
        Iterator iterator = this.getValues().values().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!username.equalsIgnoreCase(((GameProfile)(userlistopsentry = (UserListOpsEntry)iterator.next()).getValue()).getName()));
        return (GameProfile)userlistopsentry.getValue();
    }
}

