package me.aidanmees.trivia.client.tools;

import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
/**
 * @author SaubereSache
 * @since 09.07.2017
 */
public class HastebinAPI {
    private static final Gson GSON = new Gson();

    public static String uploadToHastebin(String text) throws IOException {
        URL url = new URL("https://hastebin.com/documents");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent", "Fusion Config Exporter (+http://fusion.cool/)");
        con.setRequestProperty("Content-Type", "text/plain");

        try (OutputStream out = con.getOutputStream()) {
            out.write(text.getBytes());
        }

        Response response;
        try (InputStream in = con.getInputStream()) {
            response = GSON.fromJson(IOUtils.toString(in), Response.class);
        }
        String retUrl = "https://hastebin.com/" + response.key;

        return retUrl;
    }

  
    private class Response {
        private String key;
    }
}