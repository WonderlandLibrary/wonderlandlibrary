/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.authlib.GameProfile
 *  com.mojang.util.UUIDTypeAdapter
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Map;
import java.util.UUID;

public class Session {
    private final String username;
    private final String playerID;
    private final Type sessionType;
    private final String token;

    public String getToken() {
        return this.token;
    }

    public String getSessionID() {
        return "token:" + this.token + ":" + this.playerID;
    }

    public GameProfile getProfile() {
        try {
            UUID uUID = UUIDTypeAdapter.fromString((String)this.getPlayerID());
            return new GameProfile(uUID, this.getUsername());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return new GameProfile(null, this.getUsername());
        }
    }

    public Session(String string, String string2, String string3, String string4) {
        this.username = string;
        this.playerID = string2;
        this.token = string3;
        this.sessionType = Type.setSessionType(string4);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPlayerID() {
        return this.playerID;
    }

    public Type getSessionType() {
        return this.sessionType;
    }

    public static enum Type {
        LEGACY("legacy"),
        MOJANG("mojang");

        private static final Map<String, Type> SESSION_TYPES = Maps.newHashMap();
        private final String sessionType;

        public static Type setSessionType(String string) {
            return SESSION_TYPES.get(string.toLowerCase());
        }

        static {
            Type[] typeArray = Type.values();
            int n = typeArray.length;
            int n2 = 0;
            while (n2 < n) {
                Type type = typeArray[n2];
                SESSION_TYPES.put(type.sessionType, type);
                ++n2;
            }
        }

        private Type(String string2) {
            this.sessionType = string2;
        }
    }
}

