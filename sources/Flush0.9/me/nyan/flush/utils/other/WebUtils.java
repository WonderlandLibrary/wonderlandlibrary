package me.nyan.flush.utils.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WebUtils {
    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36";

    public static ArrayList<String> getURLResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", USER_AGENT);
        return getInputStreamData(urlConnection.getInputStream());
    }

    public static ArrayList<String> getInputStreamData(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        ArrayList<String> lines = new ArrayList<>();

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            lines.add(inputLine);
        }

        return lines;
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            builder.append(inputLine).append("\n");
        }

        return builder.toString();
    }
}
