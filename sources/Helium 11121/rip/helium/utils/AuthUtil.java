package rip.helium.utils;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AuthUtil {

    public static boolean check() {
        int lines = 0;
        try {
            URL url = new URL("http://wedobegamingdoe.000webhostapp.com/hwids/" + HWID.getHWID());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            if (!url.toString().contains("http://wedobegamingdoe.000webhostapp.com/hwids/")) return false;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines += 1;
            }
        } catch (Exception ex) {

            return false;
        }
        return true;
    }

    public static void close() {
        Minecraft.getMinecraft().shutdown();
    }
}

