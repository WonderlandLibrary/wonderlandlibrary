// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.api.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.util.StringJoiner;
import dev.tenacity.intent.api.util.ConstructableEntry;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import dev.tenacity.intent.api.EnvironmentConstants;

public class Browser implements EnvironmentConstants
{
    public static String getResponse(final String getParameters) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://intent.store/api/" + getParameters).openConnection();
        connection.addRequestProperty("User-Agent", "Intent-API/1.0 Tenacity");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = reader.readLine()) != null) {
            response.append(lineBuffer);
        }
        return response.toString();
    }
    
    @SafeVarargs
    public static String postResponse(final String getParameters, final ConstructableEntry<String, String>... post) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://intent.store/api/" + getParameters).openConnection();
        connection.addRequestProperty("User-Agent", "Intent-API/1.0 Tenacity");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        final StringJoiner sj = new StringJoiner("&");
        for (final Map.Entry<String, String> entry : post) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        final byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        final int length = out.length;
        connection.setFixedLengthStreamingMode(length);
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.connect();
        try (final OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = reader.readLine()) != null) {
            response.append(lineBuffer);
        }
        return response.toString();
    }
    
    public static String postExternal(final String url, final String post, final boolean json) {
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            final byte[] out = post.getBytes(StandardCharsets.UTF_8);
            final int length = out.length;
            connection.setFixedLengthStreamingMode(length);
            connection.addRequestProperty("Content-Type", json ? "application/json" : "application/x-www-form-urlencoded; charset=UTF-8");
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            try (final OutputStream os = connection.getOutputStream()) {
                os.write(out);
            }
            final int responseCode = connection.getResponseCode();
            final InputStream stream = (responseCode / 100 == 2 || responseCode / 100 == 3) ? connection.getInputStream() : connection.getErrorStream();
            if (stream == null) {
                System.err.println(responseCode + ": " + url);
                return null;
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            final StringBuilder response = new StringBuilder();
            String lineBuffer;
            while ((lineBuffer = reader.readLine()) != null) {
                response.append(lineBuffer);
            }
            reader.close();
            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getBearerResponse(final String url, final String bearer) {
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Authorization", "Bearer " + bearer);
            if (connection.getResponseCode() == 200) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder response = new StringBuilder();
                String lineBuffer;
                while ((lineBuffer = reader.readLine()) != null) {
                    response.append(lineBuffer);
                }
                return response.toString();
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            final StringBuilder response = new StringBuilder();
            String lineBuffer;
            while ((lineBuffer = reader.readLine()) != null) {
                response.append(lineBuffer);
            }
            return response.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}
