package me.AquaVit.liquidSense.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.LiquidBounce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
    public static String getCurrentVersion() throws Exception {
        URL url = new URL("https://liquidsense.mingerxd.me/version.json");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        String result = getResultFormStream(urlConnection.getInputStream());
        JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        return json.getAsJsonObject(LiquidBounce.MINECRAFT_VERSION).get("version").getAsString();
    }

    public static String getResultFormStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }
        return builder.toString();
    }
}
