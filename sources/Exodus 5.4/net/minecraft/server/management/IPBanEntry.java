/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;
import net.minecraft.server.management.BanEntry;

public class IPBanEntry
extends BanEntry<String> {
    private static String getIPFromJson(JsonObject jsonObject) {
        return jsonObject.has("ip") ? jsonObject.get("ip").getAsString() : null;
    }

    public IPBanEntry(JsonObject jsonObject) {
        super(IPBanEntry.getIPFromJson(jsonObject), jsonObject);
    }

    @Override
    protected void onSerialization(JsonObject jsonObject) {
        if (this.getValue() != null) {
            jsonObject.addProperty("ip", (String)this.getValue());
            super.onSerialization(jsonObject);
        }
    }

    public IPBanEntry(String string, Date date, String string2, Date date2, String string3) {
        super(string, date, string2, date2, string3);
    }

    public IPBanEntry(String string) {
        this(string, (Date)null, (String)null, (Date)null, (String)null);
    }
}

