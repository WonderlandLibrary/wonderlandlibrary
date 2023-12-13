// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.mojang.authlib.GameProfile;
import com.google.gson.JsonObject;
import java.io.File;

public class UserListOps extends UserList
{
    private static final String __OBFID = "CL_00001879";
    
    public UserListOps(final File p_i1152_1_) {
        super(p_i1152_1_);
    }
    
    @Override
    protected UserListEntry createEntry(final JsonObject entryData) {
        return new UserListOpsEntry(entryData);
    }
    
    @Override
    public String[] getKeys() {
        final String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        for (final UserListOpsEntry var4 : this.getValues().values()) {
            var1[var2++] = ((GameProfile)var4.getValue()).getName();
        }
        return var1;
    }
    
    protected String func_152699_b(final GameProfile p_152699_1_) {
        return p_152699_1_.getId().toString();
    }
    
    public GameProfile getGameProfileFromName(final String p_152700_1_) {
        for (final UserListOpsEntry var3 : this.getValues().values()) {
            if (p_152700_1_.equalsIgnoreCase(((GameProfile)var3.getValue()).getName())) {
                return (GameProfile)var3.getValue();
            }
        }
        return null;
    }
    
    @Override
    protected String getObjectKey(final Object obj) {
        return this.func_152699_b((GameProfile)obj);
    }
}
