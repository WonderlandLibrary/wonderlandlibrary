// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class UserListIPBansEntry extends UserListEntryBan<String>
{
    public UserListIPBansEntry(final String valueIn) {
        this(valueIn, null, null, null, null);
    }
    
    public UserListIPBansEntry(final String valueIn, final Date startDate, final String banner, final Date endDate, final String banReason) {
        super(valueIn, startDate, banner, endDate, banReason);
    }
    
    public UserListIPBansEntry(final JsonObject json) {
        super(getIPFromJson(json), json);
    }
    
    private static String getIPFromJson(final JsonObject json) {
        return json.has("ip") ? json.get("ip").getAsString() : null;
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("ip", (String)this.getValue());
            super.onSerialization(data);
        }
    }
}
