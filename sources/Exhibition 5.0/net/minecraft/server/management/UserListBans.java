// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.mojang.authlib.GameProfile;
import com.google.gson.JsonObject;
import java.io.File;

public class UserListBans extends UserList
{
    private static final String __OBFID = "CL_00001873";
    
    public UserListBans(final File bansFile) {
        super(bansFile);
    }
    
    @Override
    protected UserListEntry createEntry(final JsonObject entryData) {
        return new UserListBansEntry(entryData);
    }
    
    public boolean isBanned(final GameProfile profile) {
        return this.hasEntry(profile);
    }
    
    @Override
    public String[] getKeys() {
        final String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (final UserListBansEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }
    
    protected String getProfileId(final GameProfile profile) {
        return profile.getId().toString();
    }
    
    public GameProfile isUsernameBanned(final String username) {
        for (final UserListBansEntry var3 : this.getValues().values()) {
            if (username.equalsIgnoreCase(((GameProfile)var3.getValue()).getName())) {
                return (GameProfile)var3.getValue();
            }
        }
        return null;
    }
    
    @Override
    protected String getObjectKey(final Object obj) {
        return this.getProfileId((GameProfile)obj);
    }
}
