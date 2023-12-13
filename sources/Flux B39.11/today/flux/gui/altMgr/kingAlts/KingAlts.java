package today.flux.gui.altMgr.kingAlts;

import com.google.gson.Gson;
import today.flux.utility.HttpUtil;

import java.io.IOException;
import java.net.URL;

public class KingAlts {
    public static String API_KEY = "";

    public static void setApiKey(String key) {
        API_KEY = key;
    }

    public static ProfileJson getProfile() {
        Gson gson = new Gson();
        String result = null;
        try {
            result = HttpUtil.performGetRequest(new URL("https://kinggen.info/api/v2/profile?key=" + API_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result, ProfileJson.class);
    }

    public static AltJson getAlt() {
        Gson gson = new Gson();
        String result = null;
        try {
            result = HttpUtil.performGetRequest(new URL("https://kinggen.info/api/v2/alt?key=" + API_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(result, AltJson.class);
    }
}