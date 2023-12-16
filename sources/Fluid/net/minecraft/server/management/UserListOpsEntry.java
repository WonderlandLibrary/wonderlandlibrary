// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOpsEntry extends UserListEntry<GameProfile>
{
    private final int permissionLevel;
    private final boolean bypassesPlayerLimit;
    
    public UserListOpsEntry(final GameProfile player, final int permissionLevelIn, final boolean bypassesPlayerLimitIn) {
        super(player);
        this.permissionLevel = permissionLevelIn;
        this.bypassesPlayerLimit = bypassesPlayerLimitIn;
    }
    
    public UserListOpsEntry(final JsonObject p_i1150_1_) {
        super(constructProfile(p_i1150_1_), p_i1150_1_);
        this.permissionLevel = (p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0);
        this.bypassesPlayerLimit = (p_i1150_1_.has("bypassesPlayerLimit") && p_i1150_1_.get("bypassesPlayerLimit").getAsBoolean());
    }
    
    public int getPermissionLevel() {
        return this.permissionLevel;
    }
    
    public boolean bypassesPlayerLimit() {
        return this.bypassesPlayerLimit;
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (this.getValue().getId() == null) ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
            data.addProperty("level", (Number)this.permissionLevel);
            data.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
        }
    }
    
    private static GameProfile constructProfile(final JsonObject p_152643_0_) {
        if (p_152643_0_.has("uuid") && p_152643_0_.has("name")) {
            final String s = p_152643_0_.get("uuid").getAsString();
            UUID uuid;
            try {
                uuid = UUID.fromString(s);
            }
            catch (final Throwable var4) {
                return null;
            }
            return new GameProfile(uuid, p_152643_0_.get("name").getAsString());
        }
        return null;
    }
}
