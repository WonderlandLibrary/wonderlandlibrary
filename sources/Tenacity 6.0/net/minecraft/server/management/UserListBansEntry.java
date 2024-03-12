// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import java.util.Date;
import com.mojang.authlib.GameProfile;

public class UserListBansEntry extends BanEntry<GameProfile>
{
    public UserListBansEntry(final GameProfile profile) {
        this(profile, null, null, null, null);
    }
    
    public UserListBansEntry(final GameProfile profile, final Date startDate, final String banner, final Date endDate, final String banReason) {
        super(profile, endDate, banner, endDate, banReason);
    }
    
    public UserListBansEntry(final JsonObject json) {
        super(toGameProfile(json), json);
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (this.getValue().getId() == null) ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
        }
    }
    
    private static GameProfile toGameProfile(final JsonObject json) {
        if (json.has("uuid") && json.has("name")) {
            final String s = json.get("uuid").getAsString();
            UUID uuid;
            try {
                uuid = UUID.fromString(s);
            }
            catch (Throwable var4) {
                return null;
            }
            return new GameProfile(uuid, json.get("name").getAsString());
        }
        return null;
    }
}
