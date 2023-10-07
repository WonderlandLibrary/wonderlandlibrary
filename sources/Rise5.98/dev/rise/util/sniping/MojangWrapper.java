package dev.rise.util.sniping;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class MojangWrapper {

    public static boolean isRealName(String name) {
        try {
            URL mojang = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpsURLConnection mojangConnection = (HttpsURLConnection) mojang.openConnection();
            mojangConnection.setRequestMethod("GET");

            if (mojangConnection.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
