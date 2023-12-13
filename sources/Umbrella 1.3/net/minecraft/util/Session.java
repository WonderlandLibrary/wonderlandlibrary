/*
 * Decompiled with CFR 0.150.
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
    private final String token;
    private final Type sessionType;
    private static final String __OBFID = "CL_00000659";

    public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_, String p_i1098_4_) {
        this.username = p_i1098_1_;
        this.playerID = p_i1098_2_;
        this.token = p_i1098_3_;
        this.sessionType = Type.setSessionType(p_i1098_4_);
    }

    public String getSessionID() {
        return "token:" + this.token + ":" + this.playerID;
    }

    public String getPlayerID() {
        return this.playerID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getToken() {
        return this.token;
    }

    public GameProfile getProfile() {
        try {
            UUID var1 = UUIDTypeAdapter.fromString((String)this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        }
        catch (IllegalArgumentException var2) {
            return new GameProfile(null, this.getUsername());
        }
    }

    public Type getSessionType() {
        return this.sessionType;
    }

    public static enum Type {
        LEGACY("LEGACY", 0, "legacy"),
        MOJANG("MOJANG", 1, "mojang");

        private static final Map field_152425_c;
        private final String sessionType;
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00001851";

        static {
            field_152425_c = Maps.newHashMap();
            $VALUES = new Type[]{LEGACY, MOJANG};
            for (Type var3 : Type.values()) {
                field_152425_c.put(var3.sessionType, var3);
            }
        }

        private Type(String p_i1096_1_, int p_i1096_2_, String p_i1096_3_) {
            this.sessionType = p_i1096_3_;
        }

        public static Type setSessionType(String p_152421_0_) {
            return (Type)((Object)field_152425_c.get(p_152421_0_.toLowerCase()));
        }
    }
}

