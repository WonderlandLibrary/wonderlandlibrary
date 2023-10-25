/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;

public class ServerActivity {
    public String profileUuid;
    public long joinTime;
    public long leaveTime;

    public static ServerActivity parse(JsonObject element) {
        ServerActivity sa = new ServerActivity();
        try {
            sa.profileUuid = JsonUtils.getStringOr("profileUuid", element, null);
            sa.joinTime = JsonUtils.getLongOr("joinTime", element, Long.MIN_VALUE);
            sa.leaveTime = JsonUtils.getLongOr("leaveTime", element, Long.MIN_VALUE);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return sa;
    }
}

