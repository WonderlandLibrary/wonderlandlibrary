// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.world;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import java.util.Map;

public class ServerUtils
{
    private static final Map<String, Long> serverIpPingCache;
    public static Minecraft mc;
    private static final String HYPIXEL = "hypixel.net";
    
    private ServerUtils() {
    }
    
    public static void update(final String ip, final long ping) {
        ServerUtils.serverIpPingCache.put(ip, ping);
    }
    
    public static long getPingToServer(final String ip) {
        return ServerUtils.serverIpPingCache.get(ip);
    }
    
    public static boolean isOnServer(final String ip) {
        return !ServerUtils.mc.isSingleplayer() && getCurrentServerIP().endsWith(ip);
    }
    
    public static String getCurrentServerIP() {
        if (ServerUtils.mc.isSingleplayer()) {
            return "Singleplayer";
        }
        return ServerUtils.mc.getCurrentServerData().serverIP;
    }
    
    public static boolean isOnHypixel() {
        return isOnServer("hypixel.net");
    }
    
    public static long getPingToCurrentServer() {
        if (ServerUtils.mc.isSingleplayer()) {
            return 0L;
        }
        return getPingToServer(getCurrentServerIP());
    }
    
    static {
        serverIpPingCache = new HashMap<String, Long>();
        ServerUtils.mc = Minecraft.getMinecraft();
    }
}
