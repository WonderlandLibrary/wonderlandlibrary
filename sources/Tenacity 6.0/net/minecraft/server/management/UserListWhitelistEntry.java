// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListWhitelistEntry extends UserListEntry<GameProfile>
{
    public UserListWhitelistEntry(final GameProfile profile) {
        super(profile);
    }
    
    public UserListWhitelistEntry(final JsonObject json) {
        super(gameProfileFromJsonObject(json), json);
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (this.getValue().getId() == null) ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
        }
    }
    
    private static GameProfile gameProfileFromJsonObject(final JsonObject json) {
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
