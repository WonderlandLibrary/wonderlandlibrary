// 
// Decompiled by Procyon v0.5.36
// 

package de.wazed.wrapper.licensing;

import java.net.URL;
import de.wazed.wrapper.utils.FieldAdapter;

public class SwitchService
{
    private final String MINECRAFT_SESSION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
    private final String MINECRAFT_AUTHENTICATION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
    private final String[] WHITELISTED_DOMAINS;
    private final FieldAdapter minecraftSessionServer;
    private final FieldAdapter userAuthentication;
    private static SwitchService instance;
    
    public SwitchService() {
        this.WHITELISTED_DOMAINS = new String[] { ".minecraft.net", ".mojang.com", ".thealtening.com" };
        this.minecraftSessionServer = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
        this.userAuthentication = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        SwitchService.instance = this;
        try {
            this.minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", this.WHITELISTED_DOMAINS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void switchService(final String authBase, final String sessionBase) {
        try {
            final String authServer = authBase;
            final FieldAdapter userAuth = this.getUserAuthentication();
            userAuth.updateFieldIfPresent("BASE_URL", authServer);
            userAuth.updateFieldIfPresent("ROUTE_AUTHENTICATE", new URL(authServer + "authenticate"));
            userAuth.updateFieldIfPresent("ROUTE_INVALIDATE", new URL(authServer + "invalidate"));
            userAuth.updateFieldIfPresent("ROUTE_REFRESH", new URL(authServer + "refresh"));
            userAuth.updateFieldIfPresent("ROUTE_VALIDATE", new URL(authServer + "validate"));
            userAuth.updateFieldIfPresent("ROUTE_SIGNOUT", new URL(authServer + "signout"));
            final String sessionServer = sessionBase;
            final FieldAdapter userSession = this.getMinecraftSessionServer();
            userSession.updateFieldIfPresent("BASE_URL", sessionServer + "session/minecraft/");
            userSession.updateFieldIfPresent("JOIN_URL", new URL(sessionServer + "session/minecraft/join"));
            userSession.updateFieldIfPresent("CHECK_URL", new URL(sessionServer + "session/minecraft/hasJoined"));
        }
        catch (Exception ex) {}
    }
    
    public FieldAdapter getMinecraftSessionServer() {
        return this.minecraftSessionServer;
    }
    
    public FieldAdapter getUserAuthentication() {
        return this.userAuthentication;
    }
    
    public String getMINECRAFT_AUTHENTICATION_SERVICE_CLASS() {
        return "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
    }
    
    public String getMINECRAFT_SESSION_SERVICE_CLASS() {
        return "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
    }
    
    public static SwitchService getInstance() {
        return SwitchService.instance;
    }
    
    public String[] getWHITELISTED_DOMAINS() {
        return this.WHITELISTED_DOMAINS;
    }
}
