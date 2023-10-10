package vestige.util.network;

import vestige.util.IMinecraft;

public class ServerUtil implements IMinecraft {

    public static boolean isOnHypixel() {
        if(mc.getCurrentServerData() == null || mc.thePlayer == null)
            return false;

        return getCurrentServer().endsWith("hypixel.net");
    }

    public static String getCurrentServer() {
        if(mc.isSingleplayer()) {
            return "Singleplayer";
        } else {
            return mc.getCurrentServerData().serverIP.toLowerCase();
        }
    }

}