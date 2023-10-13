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
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListWhitelistEntry;

public class UserListWhitelist
extends UserList {
    private static final String __OBFID = "CL_00001871";

    public UserListWhitelist(File p_i1132_1_) {
        super(p_i1132_1_);
    }

    @Override
    protected UserListEntry createEntry(JsonObject entryData) {
        return new UserListWhitelistEntry(entryData);
    }

    @Override
    public String[] getKeys() {
        String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (UserListWhitelistEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }

    protected String func_152704_b(GameProfile p_152704_1_) {
        return p_152704_1_.getId().toString();
    }

    public GameProfile func_152706_a(String p_152706_1_) {
        UserListWhitelistEntry var3;
        Iterator var2 = this.getValues().values().iterator();
        do {
            if (var2.hasNext()) continue;
            return null;
        } while (!p_152706_1_.equalsIgnoreCase(((GameProfile)(var3 = (UserListWhitelistEntry)var2.next()).getValue()).getName()));
        return (GameProfile)var3.getValue();
    }

    @Override
    protected String getObjectKey(Object obj) {
        return this.func_152704_b((GameProfile)obj);
    }
}

