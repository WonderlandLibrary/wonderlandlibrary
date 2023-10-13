/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;
import net.minecraft.server.management.BanEntry;

public class IPBanEntry
extends BanEntry {
    private static final String __OBFID = "CL_00001883";

    public IPBanEntry(String p_i46330_1_) {
        this(p_i46330_1_, null, null, null, null);
    }

    public IPBanEntry(String p_i1159_1_, Date p_i1159_2_, String p_i1159_3_, Date p_i1159_4_, String p_i1159_5_) {
        super(p_i1159_1_, p_i1159_2_, p_i1159_3_, p_i1159_4_, p_i1159_5_);
    }

    public IPBanEntry(JsonObject p_i46331_1_) {
        super(IPBanEntry.func_152647_b(p_i46331_1_), p_i46331_1_);
    }

    private static String func_152647_b(JsonObject p_152647_0_) {
        return p_152647_0_.has("ip") ? p_152647_0_.get("ip").getAsString() : null;
    }

    @Override
    protected void onSerialization(JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("ip", (String)this.getValue());
            super.onSerialization(data);
        }
    }
}

