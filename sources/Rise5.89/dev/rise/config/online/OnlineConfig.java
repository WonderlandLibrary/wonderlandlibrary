package dev.rise.config.online;

import dev.rise.config.ConfigHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class OnlineConfig {
    public static void loadConfig(final String name) {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://raw.githubusercontent.com/7Sence/RiseConfigs/main/configs/" + name)
                            .openConnection();

            connection.setConnectTimeout(5000);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String current;
            final ArrayList<String> response = new ArrayList<>();

            while ((current = in.readLine()) != null) {
                response.add(current);
            }

            ConfigHandler.loadFromList(response);
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    public static List<String> getAvailableConfigs() {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://raw.githubusercontent.com/7Sence/RiseConfigs/main/index")
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String current;
            final ArrayList<String> response = new ArrayList<>();

            while ((current = in.readLine()) != null) {
                response.add(current);
            }

            return response;
        } catch (final Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}
