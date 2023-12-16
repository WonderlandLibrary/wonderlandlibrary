// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.GameProfile;

public class Session
{
    private String username;
    private final String playerID;
    private final String token;
    private final Type sessionType;
    private static final String __OBFID = "CL_00000659";
    
    public Session(final String p_i1098_1_, final String p_i1098_2_, final String p_i1098_3_, final String p_i1098_4_) {
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
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public GameProfile getProfile() {
        try {
            final UUID var1 = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        }
        catch (IllegalArgumentException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException var2 = ex;
            return new GameProfile((UUID)null, this.getUsername());
        }
    }
    
    public Type getSessionType() {
        return this.sessionType;
    }
    
    public enum Type
    {
        LEGACY("LEGACY", 0, "LEGACY", 0, "legacy"), 
        MOJANG("MOJANG", 1, "MOJANG", 1, "mojang");
        
        private static final Map field_152425_c;
        private final String sessionType;
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00001851";
        
        static {
            field_152425_c = Maps.newHashMap();
            $VALUES = new Type[] { Type.LEGACY, Type.MOJANG };
            for (final Type var4 : values()) {
                Type.field_152425_c.put(var4.sessionType, var4);
            }
        }
        
        private Type(final String name, final int ordinal, final String p_i1096_1_, final int p_i1096_2_, final String p_i1096_3_) {
            this.sessionType = p_i1096_3_;
        }
        
        public static Type setSessionType(final String p_152421_0_) {
            return Type.field_152425_c.get(p_152421_0_.toLowerCase());
        }
    }
}
