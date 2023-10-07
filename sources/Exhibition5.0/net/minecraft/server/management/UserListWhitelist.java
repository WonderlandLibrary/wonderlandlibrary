// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.mojang.authlib.GameProfile;
import com.google.gson.JsonObject;
import java.io.File;

public class UserListWhitelist extends UserList
{
    private static final String __OBFID = "CL_00001871";
    
    public UserListWhitelist(final File p_i1132_1_) {
        super(p_i1132_1_);
    }
    
    @Override
    protected UserListEntry createEntry(final JsonObject entryData) {
        return new UserListWhitelistEntry(entryData);
    }
    
    @Override
    public String[] getKeys() {
        final String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (final UserListWhitelistEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }
    
    protected String func_152704_b(final GameProfile p_152704_1_) {
        return p_152704_1_.getId().toString();
    }
    
    public GameProfile func_152706_a(final String p_152706_1_) {
        for (final UserListWhitelistEntry var3 : this.getValues().values()) {
            if (p_152706_1_.equalsIgnoreCase(((GameProfile)var3.getValue()).getName())) {
                return (GameProfile)var3.getValue();
            }
        }
        return null;
    }
    
    @Override
    protected String getObjectKey(final Object obj) {
        return this.func_152704_b((GameProfile)obj);
    }
}
