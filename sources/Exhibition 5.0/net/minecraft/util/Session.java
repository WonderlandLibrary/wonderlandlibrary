// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.util.UUID;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.GameProfile;

public class Session
{
    private final String username;
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
    
    public String getToken() {
        return this.token;
    }
    
    public GameProfile getProfile() {
        try {
            final UUID var1 = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        }
        catch (IllegalArgumentException var2) {
            return new GameProfile((UUID)null, this.getUsername());
        }
    }
    
    public Type getSessionType() {
        return this.sessionType;
    }
    
    public static Session loginPassword(final String username, final String password, final Proxy p) {
        if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
            return null;
        }
        final YggdrasilAuthenticationService a = new YggdrasilAuthenticationService(p, "");
        final YggdrasilUserAuthentication b = (YggdrasilUserAuthentication)a.createUserAuthentication(Agent.MINECRAFT);
        b.setUsername(username);
        b.setPassword(password);
        try {
            b.logIn();
            return new Session(b.getSelectedProfile().getName(), b.getSelectedProfile().getId().toString(), b.getAuthenticatedToken(), "legacy");
        }
        catch (InvalidCredentialsException e) {
            e.printStackTrace();
        }
        catch (AuthenticationException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    public enum Type
    {
        LEGACY("LEGACY", 0, "legacy"), 
        MOJANG("MOJANG", 1, "mojang");
        
        private static final Map field_152425_c;
        private final String sessionType;
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00001851";
        
        private Type(final String p_i1096_1_, final int p_i1096_2_, final String p_i1096_3_) {
            this.sessionType = p_i1096_3_;
        }
        
        public static Type setSessionType(final String p_152421_0_) {
            return Type.field_152425_c.get(p_152421_0_.toLowerCase());
        }
        
        static {
            field_152425_c = Maps.newHashMap();
            $VALUES = new Type[] { Type.LEGACY, Type.MOJANG };
            for (final Type var4 : values()) {
                Type.field_152425_c.put(var4.sessionType, var4);
            }
        }
    }
}
