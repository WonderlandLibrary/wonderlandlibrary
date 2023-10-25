/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager.althening.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.celestial.client.ui.components.altmanager.althening.api.AltHelper;

public class AltService {
    private final AltHelper userAuthentication = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
    private final AltHelper minecraftSession = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    private EnumAltService currentService;

    public void switchService(EnumAltService v1) throws NoSuchFieldException, IllegalAccessException {
        if (this.currentService == v1) {
            return;
        }
        this.reflectionFields(v1.hostname);
        this.currentService = v1;
    }

    private void reflectionFields(String v666) throws NoSuchFieldException, IllegalAccessException {
        HashMap<String, URL> v2 = new HashMap<String, URL>();
        String v3 = v666.contains("thealtening") ? "http" : "https";
        v2.put("ROUTE_AUTHENTICATE", this.constantURL(v3 + "://authserver." + v666 + ".com/authenticate"));
        v2.put("ROUTE_INVALIDATE", this.constantURL(v3 + "://authserver" + v666 + "com/invalidate"));
        v2.put("ROUTE_REFRESH", this.constantURL(v3 + "://authserver." + v666 + ".com/refresh"));
        v2.put("ROUTE_VALIDATE", this.constantURL(v3 + "://authserver." + v666 + ".com/validate"));
        v2.put("ROUTE_SIGNOUT", this.constantURL(v3 + "://authserver." + v666 + ".com/signout"));
        v2.forEach((a2, v1) -> {
            try {
                this.userAuthentication.setStaticField((String)a2, v1);
            }
            catch (Exception v4) {
                v4.printStackTrace();
            }
        });
        this.userAuthentication.setStaticField("BASE_URL", v3 + "://authserver." + v666 + ".com/");
        this.minecraftSession.setStaticField("BASE_URL", v3 + "://sessionserver." + v666 + ".com/session/minecraft/");
        this.minecraftSession.setStaticField("JOIN_URL", this.constantURL(v3 + "://sessionserver." + v666 + ".com/session/minecraft/join"));
        this.minecraftSession.setStaticField("CHECK_URL", this.constantURL(v3 + "://sessionserver." + v666 + ".com/session/minecraft/hasJoined"));
        this.minecraftSession.setStaticField("WHITELISTED_DOMAINS", new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"});
    }

    private URL constantURL(String v1) {
        try {
            return new URL(v1);
        }
        catch (MalformedURLException v2) {
            throw new Error("Couldn't create constant for " + v1, v2);
        }
    }

    public EnumAltService getCurrentService() {
        if (this.currentService == null) {
            this.currentService = EnumAltService.MOJANG;
        }
        return this.currentService;
    }

    public static enum EnumAltService {
        MOJANG("MOJANG", 0, "mojang"),
        THEALTENING("THEALTENING", 1, "thealtening");

        String hostname;

        private EnumAltService(String s, int n2, String a2) {
            this.hostname = a2;
        }
    }
}

