// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AltService
{
    private final AltHelper userAuthentication;
    private final AltHelper minecraftSession;
    private EnumAltService currentService;
    
    public AltService() {
        this.userAuthentication = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        this.minecraftSession = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
    }
    
    public void switchService(final EnumAltService v1) throws NoSuchFieldException, IllegalAccessException {
        if (this.currentService == v1) {
            return;
        }
        this.reflectionFields(v1.hostname);
        this.currentService = v1;
    }
    
    private void reflectionFields(final String v666) throws NoSuchFieldException, IllegalAccessException {
        final HashMap<String, URL> v667 = new HashMap<String, URL>();
        final String v668 = v666.contains("thealtening") ? "http" : "https";
        v667.put("ROUTE_AUTHENTICATE", this.constantURL(v668 + "://authserver." + v666 + ".com/authenticate"));
        v667.put("ROUTE_INVALIDATE", this.constantURL(v668 + "://authserver" + v666 + "com/invalidate"));
        v667.put("ROUTE_REFRESH", this.constantURL(v668 + "://authserver." + v666 + ".com/refresh"));
        v667.put("ROUTE_VALIDATE", this.constantURL(v668 + "://authserver." + v666 + ".com/validate"));
        v667.put("ROUTE_SIGNOUT", this.constantURL(v668 + "://authserver." + v666 + ".com/signout"));
        v667.forEach((a2, v1) -> {
            try {
                this.userAuthentication.setStaticField(a2, v1);
            }
            catch (Exception v669) {
                v669.printStackTrace();
            }
            return;
        });
        this.userAuthentication.setStaticField("BASE_URL", v668 + "://authserver." + v666 + ".com/");
        this.minecraftSession.setStaticField("BASE_URL", v668 + "://sessionserver." + v666 + ".com/session/minecraft/");
        this.minecraftSession.setStaticField("JOIN_URL", this.constantURL(v668 + "://sessionserver." + v666 + ".com/session/minecraft/join"));
        this.minecraftSession.setStaticField("CHECK_URL", this.constantURL(v668 + "://sessionserver." + v666 + ".com/session/minecraft/hasJoined"));
        this.minecraftSession.setStaticField("WHITELISTED_DOMAINS", new String[] { ".minecraft.net", ".mojang.com", ".thealtening.com" });
    }
    
    private URL constantURL(final String url) {
        try {
            return new URL(url);
        }
        catch (MalformedURLException v2) {
            return null;
        }
    }
    
    public enum EnumAltService
    {
        MOJANG("MOJANG", 0, "mojang"), 
        THEALTENING("THEALTENING", 1, "thealtening");
        
        String hostname;
        
        private EnumAltService(final String s, final int n, final String a2) {
            this.hostname = a2;
        }
    }
}
