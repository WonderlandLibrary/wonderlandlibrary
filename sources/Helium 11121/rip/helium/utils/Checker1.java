package rip.helium.utils;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Checker1 {

    public static boolean check() {
        int lines = 0;
        try {
            URL url = new URL("http://cheesemanprot.000webhostapp.com/uiashduf8u9vyhsd98fhas98dfyu89asdhjf89uashdfuiasd/" + HWID.getHWID());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            if (!url.toString().contains("http://cheesemanprot.000webhostapp.com/uiashduf8u9yhsd98fhas98dfyu89asdhjf89uashdfuiasd/"))
                return false;
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
