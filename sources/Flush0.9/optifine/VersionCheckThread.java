package optifine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.ClientBrandRetriever;

public class VersionCheckThread extends Thread {
    public void run() {
        HttpURLConnection connection;

        try {
            Config.dbg("Checking for new version");
            URL url = new URL("http://optifine.net/version/1.8.8/HD_U.txt");
            connection = (HttpURLConnection) url.openConnection();

            if (Config.getGameSettings().snooperEnabled) {
                connection.setRequestProperty("OF-MC-Version", "1.8.8");
                connection.setRequestProperty("OF-MC-Brand", "" + ClientBrandRetriever.getClientModName());
                connection.setRequestProperty("OF-Edition", "HD_U");
                connection.setRequestProperty("OF-Release", "H8");
                connection.setRequestProperty("OF-Java-Version", "" + System.getProperty("java.version"));
                connection.setRequestProperty("OF-CpuCount", "" + Config.getAvailableProcessors());
                connection.setRequestProperty("OF-OpenGL-Version", "" + Config.openGlVersion);
                connection.setRequestProperty("OF-OpenGL-Vendor", "" + Config.openGlVendor);
            }

            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            try {
                InputStream inputstream = connection.getInputStream();
                String s = Config.readInputStream(inputstream);
                inputstream.close();
                String[] astring = Config.tokenize(s, "\n\r");

                if (astring.length >= 1) {
                    String s1 = astring[0].trim();
                    Config.dbg("Version found: " + s1);

                    if (Config.compareRelease(s1, "H8") <= 0)
                        return;

                    Config.setNewRelease(s1);
                }
            } finally {
                connection.disconnect();
            }
        } catch (Exception exception) {
            Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
}