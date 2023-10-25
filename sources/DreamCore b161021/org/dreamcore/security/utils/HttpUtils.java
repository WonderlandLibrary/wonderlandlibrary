package org.dreamcore.security.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtils {

    /*public static String getDataFromUrl(String url, String text) {
        try {
            HttpsURLConnection httpsClient = (HttpsURLConnection) new URL(url + text).openConnection();
            httpsClient.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpsClient.getInputStream()));
            return buffer.readLine();
        } catch (Exception e) {
            System.exit(-1);
            Display.destroy();
            Minecraft.getInstance().shutdown();
            Runtime.getRuntime().halt(1);
        }
        return null;
    }*/

}
