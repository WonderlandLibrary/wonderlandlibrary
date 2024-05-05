package best.azura.client.util.other;

import me.errordev.http.resolver.ResolvedIP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class ServerUtil {

    public static String lastIP;
    public static int lastPort;
    public static ServerData lastData;
    public static long joinedTime;
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ResolvedIP lastResolvedIP;

    public static boolean isHypixel() {
        return isOnServer("hypixel.net");
    }

    public static boolean isOnServer(String server) {
        return mc.getCurrentServerData() != null
                && (lastIP.endsWith("." + server)
                || lastIP.endsWith("." + server + "."))
                && !mc.isSingleplayer() && (System.currentTimeMillis() - joinedTime <= 20000 || lastResolvedIP != null)
                && (lastResolvedIP == null || !lastResolvedIP.getResolvedCanonHost().contains("127.0.0.1") && !lastResolvedIP.getResolvedIP().contains("127.0.0.1"));
    }

}