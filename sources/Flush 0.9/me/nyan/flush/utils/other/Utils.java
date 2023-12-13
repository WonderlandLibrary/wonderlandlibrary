package me.nyan.flush.utils.other;

import java.util.List;
import java.util.Random;

public class Utils {
    public static <T> T getRandom(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <T> T getRandom(T[] list) {
        return list[new Random().nextInt(list.length)];
    }

    public static String getServerIP(String ip) {
        if (ip.equals("2187ge7821geu12he.ddns.net.")) {
            return "redesky.com";
        }
        if (ip.endsWith(".offline.funcraft.net")) {
            return "funcraft.net";
        }
        return ip;
    }
}
