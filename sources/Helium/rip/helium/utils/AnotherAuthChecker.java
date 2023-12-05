package rip.helium.utils;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AnotherAuthChecker {

    public static boolean check() {
        int lines = 0;
        try {
            URL url = new URL("http://wedobegamingdoe.000webhostapp.com/hwids/" + HWID.getHWID());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines += 1;
            }
        } catch (Exception ex) {
            //ex.printStackTrace();

            return false;
        }
        //System.out.print("Has cape? " + player + ": " + lines);
        return true;
    }

    public static void close() {
        Minecraft.getMinecraft().shutdown();
    }


}
