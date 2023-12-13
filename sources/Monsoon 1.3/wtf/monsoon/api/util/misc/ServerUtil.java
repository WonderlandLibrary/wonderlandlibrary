package wtf.monsoon.api.util.misc;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

public final class ServerUtil {

    private static final Map<String, Long> serverIpPingCache = new HashMap<>();

    private static final String HYPIXEL = "hypixel.net";
    private static final String REDE = "redesky.com";
    private static final String MINEPLEX = "mineplex.com";

    public static void update(String ip, long ping) {
        serverIpPingCache.put(ip, ping);
    }

    public static long getPingToServer(String ip) {
        return serverIpPingCache.get(ip);
    }

    public static boolean isOnServer(String ip) {
        if (Minecraft.getMinecraft().isSingleplayer())
            return false;

        return getCurrentServerIP().endsWith(ip);
    }

    public static String getCurrentServerIP() {
        if (Minecraft.getMinecraft().isSingleplayer())
            return "Singleplayer";

        return Minecraft.getMinecraft().getCurrentServerData().serverIP;
    }

    public static boolean isHypixel() {
        return isOnServer(HYPIXEL);
    }

    public static boolean isRedesky() {
        return isOnServer(REDE);
        //return true;
    }

    public static boolean isMineplex() {
        return isOnServer(MINEPLEX);
    }

    public static long getPingToCurrentServer() {
        if (Minecraft.getMinecraft().isSingleplayer())
            return 0;

        return getPingToServer(getCurrentServerIP());
    }
}
