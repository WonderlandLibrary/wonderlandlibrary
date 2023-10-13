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
import net.minecraft.server.management.UserListOpsEntry;

public class UserListOps
extends UserList {
    private static final String __OBFID = "CL_00001879";

    public UserListOps(File p_i1152_1_) {
        super(p_i1152_1_);
    }

    @Override
    protected UserListEntry createEntry(JsonObject entryData) {
        return new UserListOpsEntry(entryData);
    }

    @Override
    public String[] getKeys() {
        String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (UserListOpsEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }

    protected String func_152699_b(GameProfile p_152699_1_) {
        return p_152699_1_.getId().toString();
    }

    public GameProfile getGameProfileFromName(String p_152700_1_) {
        UserListOpsEntry var3;
        Iterator var2 = this.getValues().values().iterator();
        do {
            if (var2.hasNext()) continue;
            return null;
        } while (!p_152700_1_.equalsIgnoreCase(((GameProfile)(var3 = (UserListOpsEntry)var2.next()).getValue()).getName()));
        return (GameProfile)var3.getValue();
    }

    @Override
    protected String getObjectKey(Object obj) {
        return this.func_152699_b((GameProfile)obj);
    }
}

