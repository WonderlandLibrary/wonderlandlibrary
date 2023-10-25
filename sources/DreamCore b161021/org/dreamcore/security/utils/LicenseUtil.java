package org.dreamcore.security.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LicenseUtil {

    public static String userName = "cracked";
    /*public static String uid;
    public static String role;

    public void check() {
        try {
            String key;
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL("https://adfjisiogdoi.xyz/license.php?hwid=" + HwidUtils.getHwid()).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((key = buffer.readLine()) != null) {
                uid = key.split(":")[0];
                userName = key.split(":")[1];
                role = key.split(":")[2];
            }
        } catch (Exception e) {
            Display.destroy();
            Minecraft.getInstance().shutdown();
            System.exit(-1);
            Runtime.getRuntime().halt(1);
        }
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    */
}
