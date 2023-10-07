// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOpsEntry extends UserListEntry
{
    private final int field_152645_a;
    private static final String __OBFID = "CL_00001878";
    
    public UserListOpsEntry(final GameProfile p_i46328_1_, final int p_i46328_2_) {
        super(p_i46328_1_);
        this.field_152645_a = p_i46328_2_;
    }
    
    public UserListOpsEntry(final JsonObject p_i1150_1_) {
        super(func_152643_b(p_i1150_1_), p_i1150_1_);
        this.field_152645_a = (p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0);
    }
    
    public int func_152644_a() {
        return this.field_152645_a;
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (((GameProfile)this.getValue()).getId() == null) ? "" : ((GameProfile)this.getValue()).getId().toString());
            data.addProperty("name", ((GameProfile)this.getValue()).getName());
            super.onSerialization(data);
            data.addProperty("level", (Number)this.field_152645_a);
        }
    }
    
    private static GameProfile func_152643_b(final JsonObject p_152643_0_) {
        if (p_152643_0_.has("uuid") && p_152643_0_.has("name")) {
            final String var1 = p_152643_0_.get("uuid").getAsString();
            UUID var2;
            try {
                var2 = UUID.fromString(var1);
            }
            catch (Throwable var3) {
                return null;
            }
            return new GameProfile(var2, p_152643_0_.get("name").getAsString());
        }
        return null;
    }
}
