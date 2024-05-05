package com.thealtening.auth.service;

import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.FieldAdapter;
import java.net.URL;

public final class ServiceSwitcher {
    private final String MINECRAFT_SESSION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService";
    private final String MINECRAFT_AUTHENTICATION_SERVICE_CLASS = "com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication";
    private final String[] WHITELISTED_DOMAINS = new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"};
    private final FieldAdapter minecraftSessionServer = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    private final FieldAdapter userAuthentication = new FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");

    public ServiceSwitcher() {
        try {
            this.minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", (Object)this.WHITELISTED_DOMAINS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlteningServiceType switchToService(AlteningServiceType service) {
        try {
            String authServer = service.getAuthServer();
            FieldAdapter userAuth = this.userAuthentication;
            userAuth.updateFieldIfPresent("BASE_URL", (Object)authServer);
            userAuth.updateFieldIfPresent("ROUTE_AUTHENTICATE", (Object)new URL(authServer + "authenticate"));
            userAuth.updateFieldIfPresent("ROUTE_INVALIDATE", (Object)new URL(authServer + "invalidate"));
            userAuth.updateFieldIfPresent("ROUTE_REFRESH", (Object)new URL(authServer + "refresh"));
            userAuth.updateFieldIfPresent("ROUTE_VALIDATE", (Object)new URL(authServer + "validate"));
            userAuth.updateFieldIfPresent("ROUTE_SIGNOUT", (Object)new URL(authServer + "signout"));
            String sessionServer = service.getSessionServer();
            FieldAdapter userSession = this.minecraftSessionServer;
            userSession.updateFieldIfPresent("BASE_URL", (Object)(sessionServer + "session/minecraft/"));
            userSession.updateFieldIfPresent("JOIN_URL", (Object)new URL(sessionServer + "session/minecraft/join"));
            userSession.updateFieldIfPresent("CHECK_URL", (Object)new URL(sessionServer + "session/minecraft/hasJoined"));
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
            return AlteningServiceType.MOJANG;
        }
        return service;
    }
}
